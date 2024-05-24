package com.java.recruitment.service.impl;

import com.java.recruitment.service.IFileService;
import com.java.recruitment.service.model.hiring.JobRequestFile;
import com.java.recruitment.web.dto.hiring.JobRequestFileDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService implements IFileService {
    @Override
    public JobRequestFile loadFile(JobRequestFileDTO fileDTO) {
        return null;
    }

    @Override
    public List<JobRequestFileDTO> downloadFile(Long id) {
        return List.of();
    }

    @Override
    public void deleteFile(Long id) {

    }
}
