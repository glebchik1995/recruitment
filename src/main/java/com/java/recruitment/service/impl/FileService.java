package com.java.recruitment.service.impl;

import com.java.recruitment.repositoty.FileRepository;
import com.java.recruitment.repositoty.exception.DataNotFoundException;
import com.java.recruitment.service.IFileManager;
import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.hiring.JobRequestFile;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import com.java.recruitment.web.mapper.impl.JobRequestFileMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class FileService implements IFileService {

    private final FileRepository fileRepository;

    private final IFileManager fileManager;

    private final JobRequestFileMapper jobRequestFileMapper;

    @Transactional(rollbackFor = {IOException.class})
    @Override
    public JobRequestFileDTO upload(MultipartFile file) {
        String key = generateKey(file.getOriginalFilename());
        JobRequestFile newFile = JobRequestFile.builder()
                .name(file.getOriginalFilename())
                .key(key)
                .size(file.getSize())
                .uploadDate(LocalDate.now())
                .build();
        JobRequestFile savedFile = fileRepository.save(newFile);
        try {
            fileManager.upload(file.getBytes(), key);
            log.info("Файл '{}' успешно загружен", file.getOriginalFilename());
        } catch (IOException e) {
            log.error("Ошибка при загрузке файла '{}': {}", file.getOriginalFilename(), e.getMessage());
            throw new RuntimeException(e);
        }

        return jobRequestFileMapper.toDto(savedFile);
    }

    @Transactional(readOnly = true)
    @Override
    public JobRequestFileDTO findById(Long fileId) {
        JobRequestFile file = fileRepository.findById(fileId).orElseThrow(() -> new DataNotFoundException("Файл не найден"));
        return jobRequestFileMapper.toDto(file);
    }

    @Override
    @Transactional
    public Resource download(String key) throws IOException {
        return fileManager.download(key);
    }


    @Override
    @Transactional(rollbackFor = {IOException.class})
    public void deleteFile(Long fileId) throws IOException {
        JobRequestFile file = fileRepository.findById(fileId).orElseThrow(() -> new DataNotFoundException("Файл не найден"));
        fileRepository.delete(file);
        fileManager.delete(file.getKey());
    }

    private String generateKey(String name) {
        return DigestUtils.md5Hex(name + LocalDateTime.now());
    }
}
