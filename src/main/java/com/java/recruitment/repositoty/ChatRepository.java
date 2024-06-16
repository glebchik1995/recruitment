package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.jobRequest.JobRequest;
import com.java.recruitment.service.model.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<JobRequest> {

    @Query(value = """
             SELECT cm
             FROM chat_message cm
             WHERE cm.recruiterId = :recruiterId
            """)
    Page<ChatMessage> findAllMessageForRecruiter(
            @Param("recruiterId") Long recruiterId,
            Pageable pageable
    );

    @Query(value = """
             SELECT jr
             FROM job_request jr
             JOIN jr.vacancy v
             WHERE v.recruiterId = :recruiterId
            """)
    Page<ChatMessage> findAllMessageForRecruiterByCriteria(
            @Param("recruiterId") Long recruiterId,
            Specification<ChatMessage> specification,
            Pageable pageable
    );
}
