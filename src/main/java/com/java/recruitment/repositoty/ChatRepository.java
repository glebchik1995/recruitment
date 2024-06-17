package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.chat.ChatMessage;
import com.java.recruitment.service.model.jobRequest.JobRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<JobRequest> {

    Page<ChatMessage> findAllBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            Long sender_id,
            Long recipient_id,
            Long recipient_id2,
            Long sender_id2,
            Pageable pageable
    );

    Page<ChatMessage> findAllBySenderIdAndRecipientIdOrRecipientIdAndSenderId(
            Long sender_id,
            Long recipient_id,
            Long recipient_id2,
            Long sender_id2,
            Pageable pageable,
            Specification<ChatMessage> specification
    );

    @Query(value = """
             SELECT cm
             FROM ChatMessage cm
             JOIN User u
             WHERE cm.id = :chatMessageId AND u.id = :userHrId AND u.role = 'HR'
            """)
    ChatMessage findChatMessageByIdAndUserHrId(
            @Param("chatMessageId") Long chatMessageId,
            @Param("userHrId") Long userHrId
    );

    @Query(value = """
             SELECT cm
             FROM ChatMessage cm
             JOIN User u
             WHERE cm.id = :chatMessageId AND u.id = :userRecruiterId AND u.role = 'RECRUITER'
            """)
    ChatMessage findChatMessageByIdAndUserRecruiterId(
            @Param("chatMessageId") Long chatMessageId,
            @Param("userRecruiterId") Long userRecruiterId
    );

}
