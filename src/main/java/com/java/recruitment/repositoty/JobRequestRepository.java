package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.hiring.JobRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface JobRequestRepository extends JpaRepository<JobRequest, Long>, JpaSpecificationExecutor<JobRequest> {

}
