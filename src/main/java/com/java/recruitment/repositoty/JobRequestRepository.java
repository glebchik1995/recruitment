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
                             AND id = :job_request_id)
            """, nativeQuery = true)
    boolean isJobRequestOwner(
            @Param("userId") Long userId,
            @Param("job_request_id") Long job_request_id
    );

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM job_request jr
                           JOIN vacancy v ON jr.vacancy_id = v.id
                           WHERE v.recruiter_id = :userId
                             AND jr.id = :job_request_id)
            """, nativeQuery = true)
    boolean isRecruiterForJobRequest(
            @Param("userId") Long userId,
            @Param("job_request_id") Long job_request_id
    );

    @Query(value = """
             SELECT jr
             FROM job_request jr
             JOIN jr.vacancy v
             WHERE v.recruiterId = :recruiterId
            """)
    Page<JobRequest> findJobRequestsForRecruiter(
            @Param("recruiterId") Long recruiterId,
            Pageable pageable,
            Specification<JobRequest> specification
    );

    @Query(value = """
             SELECT jr
             FROM job_request jr
             JOIN jr.vacancy v
             WHERE v.recruiterId = :recruiterId
            """)
    Page<JobRequest> findJobRequestsForRecruiter(
            @Param("recruiterId") Long recruiterId,
            Pageable pageable
    );
}

