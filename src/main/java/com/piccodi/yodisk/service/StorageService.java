package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.User;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private static Path fileDestination;

    @PostConstruct
    private void construct() throws IOException {

        fileDestination = Paths.get("/home/" + System.getProperty("user.name") + "/Downloads/yodisk");
        if(!Files.exists(fileDestination)){
            Files.createDirectory(fileDestination);
            System.out.println(fileDestination);
        }
    }

    public boolean upload(MultipartFile file, User user) throws IOException {

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

        return true;

    }

    public Resource download(String fileName, Long userId) throws FileNotFoundException, MalformedURLException {


            Path pathFile = Paths.get(fileDestination + "/" + userId + "/" + fileName);
            if(Files.exists(pathFile)){
                return new UrlResource(pathFile.toUri());
            }
            else{
                throw new FileNotFoundException();
            }
    }

    public boolean delete(String fileName, Long userId) throws IOException {
        Path filePath = Paths.get(fileDestination + "/" + userId + "/" + fileName);
        if (Files.exists(filePath)){
            Files.delete(filePath);
            System.out.println(filePath);
        }

        return true;
    }

}
