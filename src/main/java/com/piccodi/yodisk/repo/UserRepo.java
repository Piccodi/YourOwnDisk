package com.piccodi.yodisk.repo;

import com.piccodi.yodisk.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(nativeQuery = true,
    value = "SELECT id FROM user WHERE username = ?1")
    Optional<Long> findUserIdByUsername(String username);

    @Query(nativeQuery = true,
    value = "SELECT user.id, user.username, user.password, user.role FROM user INNER JOIN user_files WHERE files_id = ?1")
    Optional<User> fildUserByFile(Long file_id);

}
