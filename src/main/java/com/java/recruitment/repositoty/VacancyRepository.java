package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.vacancy.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VacancyRepository extends
        JpaRepository<Vacancy, Long>,
        JpaSpecificationExecutor<Vacancy> {

    @Query(value = """
             SELECT exists(
                           SELECT 1
                           FROM vacancy
                           WHERE recruiter_id = :userId
                             AND id = :recruiterId)
            """, nativeQuery = true)
    boolean isVacancyOwner(
            @Param("userId") Long userId,
            @Param("recruiterId") Long recruiterId
    );
}
