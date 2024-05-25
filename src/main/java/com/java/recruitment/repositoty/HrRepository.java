package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.hr.HR;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HrRepository extends JpaRepository<HR, Long> {
}
