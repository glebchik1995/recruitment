package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

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

//    @Query(value = """
//            SELECT u.id as id,
//            u.name as name,
//            u.username as username,
//            u.password as password
//            FROM users_tasks ut
//            JOIN users u ON ut.user_id = u.id
//            WHERE ut.task_id = :taskId
//            """, nativeQuery = true)
//    Optional<User> findTaskAuthor(
//            @Param("taskId") Long taskId
//    );
}
