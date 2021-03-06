package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.File;
import com.piccodi.yodisk.repo.FileRepo;
import com.piccodi.yodisk.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.Principal;
import java.util.ArrayList;

@Service
public class FileService {

    private FileRepo fileRepo;

    private UserRepo userRepo;

    private LinkService linkService;

    private StorageService storageService;

    @Autowired
    public void setLinkService(LinkService linkService) {this.linkService = linkService;}

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

    public String getNameFile(Long id){
        return fileRepo.findById(id).get().getFileName();
    }

    public void saveFile(MultipartFile file, Principal principal) throws IOException {

            var user = userRepo.findByUsername(principal.getName()).get();
            var dbFIle = new File();
            dbFIle.setFileName(file.getOriginalFilename());
            dbFIle.setSize( SizeCalculator.calculate(file.getSize()) + " MB");
            if(storageService.upload(file, user)) {
                fileRepo.save(dbFIle);
                user.addFile(dbFIle);
                userRepo.save(user);
            }
    }

    public Resource downloadFileById(Long fileId, Principal principal) throws FileNotFoundException, MalformedURLException {

        return storageService.download(
                fileRepo.findFileNameById(fileId).get(),
                userRepo.findUserIdByUsername(principal.getName()).get());
    }

    public Resource downloadFileByKey(String key) throws FileNotFoundException, MalformedURLException {

            var fileId = linkService.getFileIdByKey(key);
            var resource = storageService.download(
                    fileRepo.findFileNameById(fileId).get(),
                    userRepo.findUserIdByFileId(fileId).get());
            linkService.deleteLink(key);
        return resource;
    }

    public String generateLink(Long fileId, Principal principal) throws FileNotFoundException {
        if(userRepo.checkForOwning(fileId, principal.getName()) == 1){
            return linkService.createLink(fileId, principal.getName());
        }
        else{
            throw new FileNotFoundException();
        }
    }

    public void delete(Long file_id) throws IOException {

            var file = fileRepo.findById(file_id).get();
            var user = userRepo.findUserByFileId(file_id).get();
            if (storageService.delete(file.getFileName(), user.getId())) {
                user.deleteFile(file);
                fileRepo.delete(file);
            }
    }

    public static class SizeCalculator{
        public static String calculate(Long size){
            double d = size.doubleValue()/(1024D*1024D);
            return String.format("%.2f", d);
        }
    }

}
