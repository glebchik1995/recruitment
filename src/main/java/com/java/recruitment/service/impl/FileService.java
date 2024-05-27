package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.exception.DataUploadException;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.properties.MinioProperties;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Objects;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    @Override
    public String upload(final JobRequestFileDTO files) {
        try {
            createBucket();
        } catch (Exception e) {
            throw new DataUploadException("Не удалось загрузить файл: " + e.getMessage());
        }
        MultipartFile file = files.getFile();
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
    private void saveImage(final InputStream inputStream, final String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
