package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.hiring.JobRequestFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<JobRequestFile, Long> {
}
