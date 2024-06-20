package com.java.recruitment.repositoty;

import com.java.recruitment.service.filter.GenericSpecification;
import com.java.recruitment.service.model.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepository extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {

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

    Page<ChatMessage> findAllBySenderIdAndRecipientId(
            Long senderId,
            Long recipientId,
            GenericSpecification<ChatMessage> jobRequestGenericSpecification,
            Pageable pageable
    );

}
