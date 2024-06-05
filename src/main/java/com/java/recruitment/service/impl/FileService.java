package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.repositoty.exception.DataUploadException;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.service.properties.MinioProperties;
import com.java.recruitment.web.dto.file.DeleteFileDTO;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    private final JobRequestRepository jobRequestRepository;

    @Override
    public String upload(final MultipartFile file) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new DataUploadException("Не удалось загрузить файл: " + e.getMessage());
        }

        if (file.isEmpty() || file.getOriginalFilename() == null) {
            throw new DataUploadException("Файл должен иметь название.");
        }
        String fileName = generateFileName(file);
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (Exception e) {
            throw new DataUploadException("Не удалось загрузить файл: " + e.getMessage());
        }
        saveImage(inputStream, fileName);

        return fileName;
    }

    @SneakyThrows
    @Transactional
    public String download(final Long id) {

        JobRequest jobRequest = jobRequestRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена!"));

        List<String> fileNames = jobRequest.getFiles();

        // Создание ссылки на скачивание файлов
        List<String> downloadLinks = new ArrayList<>();
        for (String fileName : fileNames) {
            String downloadUrl = minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(minioProperties.getBucket())
                            .object(fileName)
                            .expiry(60 * 60)
                            .build()
            );
            downloadLinks.add(downloadUrl);
        }

        return formatDownloadLinks(downloadLinks);
    }

    @Transactional
    public void delete(final DeleteFileDTO dto) {
        JobRequest jobRequest = jobRequestRepository.findById(dto.getJobRequestId())
                .orElseThrow(() -> new DataNotFoundException("Заявка не найдена!"));

        String fileName = dto.getFileName();
        List<String> files = jobRequest.getFiles();
        if (files.contains(fileName)) {
            try {
                minioClient.removeObject(
                        RemoveObjectArgs.builder()
                                .bucket(minioProperties.getBucket())
                                .object(fileName)
                                .build()
                );
                files.remove(fileName);
                jobRequestRepository.save(jobRequest);
            } catch (Exception e) {
                throw new RuntimeException("Не удалось создать архив", e);
            }
        } else {
            log.error("Файл {} не найден в запросе на работу {}", fileName, dto.getJobRequestId());
        }
    }

    @SneakyThrows
    private void createBucket() {
        boolean found = minioClient.bucketExists(BucketExistsArgs.builder()
                .bucket(minioProperties.getBucket())
                .build());
        if (!found) {
            minioClient.makeBucket(MakeBucketArgs.builder()
                    .bucket(minioProperties.getBucket())
                    .build());
        }
    }

    private String generateFileName(final MultipartFile file) {
        String extension = getExtension(file);
        return UUID.randomUUID() + "." + extension;
    }

    private String getExtension(final MultipartFile file) {
        return Objects.requireNonNull(file.getOriginalFilename())
                .substring(file.getOriginalFilename()
                        .lastIndexOf(".") + 1);
    }

    @SneakyThrows
    private void saveImage(
            final InputStream inputStream,
            final String fileName
    ) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }

    private String formatDownloadLinks(final List<String> downloadLinks) {
        StringBuilder formattedLinks = new StringBuilder();
        for (int i = 0; i < downloadLinks.size(); i++) {
            formattedLinks
                    .append("File ")
                    .append(i + 1)
                    .append(": ")
                    .append(downloadLinks.get(i))
                    .append("\n");
        }
        return formattedLinks.toString();
    }
}
