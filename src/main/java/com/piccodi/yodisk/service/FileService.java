package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.File;
import com.piccodi.yodisk.repo.FileRepo;
import com.piccodi.yodisk.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;

@Service
public class FileService {

    private FileRepo fileRepo;

    private UserRepo userRepo;

    private StorageService storageService;

    @Autowired
    public void setStorageService(StorageService storageService) {this.storageService = storageService;}

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Autowired
    public void setFileRepo(FileRepo fileRepo) {
        this.fileRepo = fileRepo;
    }

    public ArrayList<File> get(Principal principal){

        return fileRepo.getUserFiles(userRepo.findUserIdByUsername(principal.getName()).get()).get();
    }

    public void saveFile(MultipartFile file, Principal principal) {
        try{
            //todo проверить на повторяющиеся имена
            var dbFIle = new File();
            dbFIle.setFileName(file.getOriginalFilename());
            dbFIle.setSize( SizeCalculator.calculate(file.getSize()) + " MB");
            fileRepo.save(dbFIle);
            var user = userRepo.findByUsername(principal.getName()).get();
            user.addFile(dbFIle);
            userRepo.save(user);
            storageService.upload(file, user);
        }
        catch (Exception e){ e.printStackTrace(); }
    }

    public void delete(Long file_id){

        var file = fileRepo.findById(file_id).get();
        var user = userRepo.fildUserByFile(file_id).get();
        user.deleteFile(file);
        fileRepo.delete(file);

    }

    public class SizeCalculator{
        public static String calculate(Long size){
            double d = size.doubleValue()/(1024D*1024D);
            return String.format("%.2f", d);
        }
    }

}
