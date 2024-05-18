package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.attachment.AttachedFile;
import com.java.recruitment.service.model.message.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<AttachedFile, Long> {
}
