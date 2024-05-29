package com.java.recruitment.service;

import org.springframework.web.multipart.MultipartFile;

public interface IFileService {


    String upload(MultipartFile file);

    String download(Long id);
}
