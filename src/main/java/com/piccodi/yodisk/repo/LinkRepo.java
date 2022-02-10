package com.piccodi.yodisk.repo;

import com.piccodi.yodisk.entity.Link;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LinkRepo extends CrudRepository<Link, Long> {

    @Query(nativeQuery = true,
    value = "select exists(select * from link where file_id = ?1)")
    int checkForExistance (long fileId);

    @Query(nativeQuery = true,
    value = "select id, death_time, name from link where file_id = ?1")
    Optional<Link> findByFileId(Long fileId);
}
