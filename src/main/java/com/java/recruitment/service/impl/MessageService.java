package com.java.recruitment.service.impl;

import com.java.recruitment.service.IMessageService;
import com.java.recruitment.service.model.message.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class MessageService implements IMessageService {
    @Override
    public Message createMessage(Message message) {
        return null;
    }

    @Override
    public Page<Message> getAllMessages(Pageable pageable) {
        return null;
    }

    @Override
    public Message getMessageById(Long id) {
        return null;
    }

    @Override
    public Message updateMessage(Long id, Message message) {
        return null;
    }

    @Override
    public void deleteMessage(Long id) {

    }
}
