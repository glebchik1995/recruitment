package com.java.recruitment.service;

import com.java.recruitment.service.model.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMessageService {

    Message createMessage(Message message);

    Page<Message> getAllMessages(Pageable pageable);

    Message getMessageById(Long id);

    Message updateMessage(Long id, Message message);

    void deleteMessage(Long id);
}
