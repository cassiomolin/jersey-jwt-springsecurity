package com.cassiomolin.example.user.repository;

import com.cassiomolin.example.user.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Provides operations for persisting {@link User}s.
 *
 * @author cassiomolin
 */
public interface UserRepository extends CrudRepository<User, Long> {

    User findOne(Long id);

    @Query("SELECT u FROM User u WHERE u.username = ?1 OR u.email = ?1")
    User findByUsernameOrEmail(String identifier);
}