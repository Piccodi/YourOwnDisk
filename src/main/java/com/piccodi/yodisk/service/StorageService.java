package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.User;
import com.piccodi.yodisk.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private static Path fileDestination;

    @PostConstruct
    private void construct() throws IOException {
        fileDestination = Paths.get("/home/" + System.getProperty("user.name") + "/Downloads/yodisk");
        System.out.println(fileDestination);
        if(!Files.exists(fileDestination)){
            Files.createDirectory(fileDestination);
            System.out.println(fileDestination);
        }
    }

    public void upload(MultipartFile file, User user) throws IOException {


        Path filesPath = Paths.get(fileDestination + "/" + user.getId());
        System.out.println(filesPath);
        if(!Files.exists(filesPath)){
            Files.createDirectory(filesPath);
            System.out.println(filesPath);
        }

        Path filePath = Paths.get(filesPath + "/" + file.getOriginalFilename());
        if(!Files.exists(filePath)){
            Files.createFile(filePath);
            System.out.println(filePath);
        }

        file.transferTo(filePath);


    }

}
