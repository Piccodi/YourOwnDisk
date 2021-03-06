package com.piccodi.yodisk.repo;

import com.piccodi.yodisk.entity.Link;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LinkRepo extends CrudRepository<Link, Long> {

    @Query(nativeQuery = true,
    value = "select exists(select * from link where file_id = ?1)")
    int checkForExistance (long fileId);

    @Query(nativeQuery = true,
    value = "select * from link where file_id = ?1")
    Optional<Link> findByFile(Long fileId);

    Optional<Link> findLinkByName(String key);

    @Query(nativeQuery = true,
    value = "select file_id from link where name = ?1")
    Optional<Long> getFileIdByKey(String name);

    @Query(nativeQuery = true,
    value = "select * from link")
    Optional<List<Link>> getAllLinks();
}
