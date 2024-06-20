package com.java.recruitment.service.impl;

import com.java.recruitment.aspect.log.LogError;
import com.java.recruitment.repositoty.JobRequestRepository;
import com.java.recruitment.repositoty.UserRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.repositoty.exception.DataUploadException;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.service.model.jobRequest.JobRequest_;
import com.java.recruitment.service.model.user.User;
import com.java.recruitment.service.model.user.User_;
import com.java.recruitment.service.properties.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.java.recruitment.service.model.user.Role.ADMIN;
import static com.java.recruitment.service.model.user.Role.HR;

@LogError
@RequiredArgsConstructor
@Service
public class FileService implements IFileService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    private final JobRequestRepository jobRequestRepository;

    private final UserRepository userRepository;

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
    @Override
    public String download(
            final Long userId,
            final Long recruiterId
    ) {

        JobRequest jobRequest = findJobRequestByUserIdAndJobRequestId(userId, recruiterId);

        List<String> fileNames = jobRequest.getFiles();
        List<String> downloadLinks = new ArrayList<>();
        if (!fileNames.isEmpty()) {
            // Создание ссылки на скачивание файлов
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

        } else {
            return "У заявки нет прикрепленных файлов";
        }
    }

    @Override
    public void delete(final List<String> filesNames) {
        if (!filesNames.isEmpty()) {
            for (String fileName : filesNames) {
                try {
                    minioClient.removeObject(
                            RemoveObjectArgs.builder()
                                    .bucket(minioProperties.getBucket())
                                    .object(fileName)
                                    .build()
                    );
                } catch (Exception e) {
                    throw new RuntimeException("Не удалось удалить файл из хранилища", e);
                }
            }
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

    private JobRequest findJobRequestByUserIdAndJobRequestId(
            final Long userId,
            final Long jobRequestId
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не найден"));

        Specification<JobRequest> jobRequestSp = (root, query, cb) ->
                cb.equal(root.get(JobRequest_.id), jobRequestId);

        if (user.getRole().equals(ADMIN)) {
            return jobRequestRepository.findById(jobRequestId)
                    .orElseThrow(() -> new DataNotFoundException("Заявка не найдена"));

        } else if (user.getRole().equals(HR)) {
            Specification<JobRequest> hrSp = (root, query, cb) ->
                    cb.equal(root.get(JobRequest_.hr).get(User_.id), userId);

            return jobRequestRepository.findOne(hrSp.and(jobRequestSp))
                    .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        } else {
            Specification<JobRequest> recruiterSp = (root, query, cb) ->
                    cb.equal(root.get(JobRequest_.recruiter).get(User_.id), userId);

            return jobRequestRepository.findOne(recruiterSp.and(jobRequestSp))
                    .orElseThrow(() -> new DataNotFoundException("Кандидат не найден"));
        }
    }
}
