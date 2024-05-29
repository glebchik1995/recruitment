package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.repositoty.exception.DataUploadException;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.hiring.JobRequest;
import com.java.recruitment.service.properties.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


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

//    @SneakyThrows
//    public List<InputStreamResource> download(Long id) {
//
//        JobRequest jobRequest = jobRequestRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Заявка не найдена!"));
//
//        List<InputStreamResource> resources = new ArrayList<>();
//        for (String fileName : jobRequest.getFiles()) {
//            InputStream inputStream = minioClient.getObject(
//                    GetObjectArgs.builder()
//                            .bucket(minioProperties.getBucket())
//                            .object(fileName)
//                            .build()
//            );
//            resources.add(new InputStreamResource(inputStream));
//        }
//        return resources;
//    }

//    @SneakyThrows
//    public InputStreamResource download(Long id) {
//        JobRequest jobRequest = jobRequestRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Заявка не найдена!"));
//        List<String> fileNames = jobRequest.getFiles();
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try (ZipOutputStream zos = new ZipOutputStream(baos)) {
//            for (String fileName : fileNames) {
//                InputStream inputStream = minioClient.getObject(
//                        GetObjectArgs.builder()
//                                .bucket(minioProperties.getBucket())
//                                .object(fileName)
//                                .build()
//                );
//                zos.putNextEntry(new ZipEntry(fileName));
//                byte[] bytes = inputStream.readAllBytes();
//                zos.write(bytes, 0, bytes.length);
//                zos.closeEntry();
//                inputStream.close();
//            }
//        }
//        return new InputStreamResource(new ByteArrayInputStream(baos.toByteArray()));
//    }

//    @SneakyThrows
//    public List<String> download(Long id) {
//        JobRequest jobRequest = jobRequestRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Заявка не найдена!"));
//        List<String> fileNames = jobRequest.getFiles();
//        List<String> returnNames = new ArrayList<>();
//        String bucketName = minioProperties.getBucket();
////        String objectName = "files.zip";
//        try {
//            // Создаем временную ссылку на скачивание файла
//
//            for (String fileName : fileNames) {
//                String url = minioClient.getPresignedObjectUrl(
//                        GetPresignedObjectUrlArgs.builder()
//                                .method(Method.GET)
//                                .bucket(bucketName)
//                                .object(fileName)
//                                .expiry(60 * 60) // Ссылка действительна в течение 1 часа
//                                .build()
//                );
//
//                returnNames.add(url);
//            }
//            return returnNames;
//        } catch (Exception e) {
//            throw new RuntimeException("Не удалось создать ссылку на скачивание файла", e);
//        }
//    }
        @SneakyThrows
    public String download(Long id) {
        JobRequest jobRequest = jobRequestRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Заявка не найдена!"));
        List<String> fileNames = jobRequest.getFiles();
     try {
        // Создаем временный файл для архива
        File tempFile = File.createTempFile("files", ".zip");

        // Создаем поток для записи в архив
        FileOutputStream fos = new FileOutputStream(tempFile);
        ZipOutputStream zos = new ZipOutputStream(fos);

        // Добавляем каждый файл в архив
        for (String fileName : fileNames) {
            File file = new File(fileName);
            FileInputStream fis = new FileInputStream(file);
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zos.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zos.write(bytes, 0, length);
            }

            fis.close();
        }

        // Закрываем потоки
        zos.closeEntry();
        zos.close();
        fos.close();

        // Возвращаем ссылку на архив
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioProperties.getBucket())
                        .object(tempFile.getName())
                        .expiry(60 * 60) // Ссылка действительна в течение 1 часа
                        .build()
        );
    } catch (Exception e) {
        throw new RuntimeException("Не удалось создать архив", e);
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
    private void saveImage(final InputStream inputStream, final String fileName) {
        minioClient.putObject(PutObjectArgs.builder()
                .stream(inputStream, inputStream.available(), -1)
                .bucket(minioProperties.getBucket())
                .object(fileName)
                .build());
    }
}
