package com.java.recruitment.service;

import com.java.recruitment.service.model.jobRequest.JobRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileService {

    String upload(MultipartFile file);

    String download(JobRequest jobRequest);

    void delete(List<String> filesNames);
}
