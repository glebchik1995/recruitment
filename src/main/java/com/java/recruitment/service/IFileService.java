package com.java.recruitment.service;

import com.java.recruitment.service.model.hiring.JobRequestFile;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;

import java.util.List;

public interface IFileService {


    JobRequestFile loadFile(JobRequestFileDTO fileDTO);

    List<JobRequestFileDTO> downloadFile(Long id);

    void deleteFile(Long id);
}
