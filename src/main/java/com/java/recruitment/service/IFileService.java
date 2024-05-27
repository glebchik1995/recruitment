package com.java.recruitment.service;

import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;

public interface IFileService {


    String upload(JobRequestFileDTO files);
}
