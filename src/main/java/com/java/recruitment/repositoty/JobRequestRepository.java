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

    @Query(value = "SELECT EXISTS(" +
            "                     SELECT 1 " +
            "                     FROM job_request " +
            "                     WHERE hr_id = :userId " +
            "                     AND id = :jobRequestId)", nativeQuery = true)
    boolean isJobRequestOwner(
            @Param("userId") Long userId,
            @Param("jobRequestId") Long jobRequestId
    );

    @Query(value = "SELECT EXISTS(" +
            "                     SELECT 1 " +
            "                     FROM job_request jr " +
            "                     JOIN vacancy v ON jr.vacancy_id = v.id " +
            "                     WHERE jr.id = :jobRequestId " +
            "                     AND v.recruiter_id = :userId)", nativeQuery = true)
    boolean isJobRequestConsumer(
            @Param("userId") Long userId,
            @Param("jobRequestId") Long jobRequestId
    );

    @Query(value = "SELECT EXISTS(" +
            "                     SELECT 1 " +
            "                     FROM job_request jr " +
            "                     JOIN vacancy v ON jr.vacancy_id = v.id " +
            "                     WHERE v.recruiter_id = :recruiterId AND :specification)", nativeQuery = true)
    Page<JobRequest> findAllJobRequestsByRecruiterIdAndCriteria(
            @Param("recruiterId") Long recruiterId,
            @Param("specification") Specification<JobRequest> specification,
            Pageable pageable
    );

    @Query(value = "SELECT EXISTS(" +
            "                     SELECT 1 " +
            "                     FROM job_request jr " +
            "                     JOIN vacancy v ON jr.vacancy_id = v.id " +
            "                     WHERE v.recruiter_id = :recruiterId)", nativeQuery = true)
    Page<JobRequest> findAllJobRequestsByRecruiterId(
            @Param("recruiterId") Long recruiterId,
            Pageable pageable
    );
}

