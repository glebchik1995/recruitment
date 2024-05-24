package com.java.recruitment.service;

import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {


    JobRequestFileDTO upload(MultipartFile resource);

    JobRequestFileDTO findById(Long fileId);

    Resource download(String key) throws IOException;

    void deleteFile(Long id) throws IOException;
}
