package com.java.recruitment.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    String upload(MultipartFile file);

    String download(Long id);

    void delete(List<String> filesNames);
}
