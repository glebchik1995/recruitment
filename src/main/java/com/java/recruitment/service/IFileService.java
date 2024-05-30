package com.java.recruitment.service;

import com.java.recruitment.web.dto.file.DeleteFileDTO;
import org.springframework.web.multipart.MultipartFile;

public interface IFileService {


    String upload(MultipartFile file);

    String download(Long id);

    void delete(DeleteFileDTO dto);
}
