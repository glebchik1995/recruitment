package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.jobRequest.JobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface JobRequestRepository extends JpaRepository<JobRequest, Long>, JpaSpecificationExecutor<JobRequest> {

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM job_request
                           WHERE hr_id = :userId
                           AND id = :jobRequestId)
            """, nativeQuery = true)
    boolean isJobRequestOwner(
            @Param("userId") Long userId,
            @Param("jobRequestId") Long jobRequestId
    );

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM job_request
                           WHERE recruiter_id = :userId
                           AND id = :jobRequestId)
            """, nativeQuery = true)
    boolean isJobRequestConsumer(
            @Param("userId") Long userId,
            @Param("jobRequestId") Long jobRequestId
    );

    @Query(value = """
             SELECT jr
             FROM job_request jr
             JOIN jr.vacancy v
             WHERE v.recruiterId = :recruiterId
            """)
    Page<JobRequest> findAllJobRequestsByRecruiterIdAndCriteria(
            @Param("recruiterId") Long recruiterId,
            Specification<JobRequest> specification,
            Pageable pageable
    );

    @Query(value = """
             SELECT jr
             FROM job_request jr
             JOIN jr.vacancy v
             WHERE v.recruiterId = :recruiterId
            """)
    Page<JobRequest> findAllJobRequestsByRecruiterId(
            @Param("recruiterId") Long recruiterId,
            Pageable pageable
    );
}

