package com.piccodi.yodisk.repo;

import com.piccodi.yodisk.entity.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface FileRepo extends CrudRepository<File, Long> {
    @Query(nativeQuery = true,
    value = "SELECT file.id, file.file_name, file.size FROM user_files INNER JOIN file ON files_id = file.id WHERE user_id = ?1")
    Optional<ArrayList<File>> getUserFiles(Long user_id);



}
