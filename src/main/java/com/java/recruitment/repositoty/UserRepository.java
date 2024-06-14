package com.java.recruitment.repositoty;

import com.java.recruitment.service.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

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
