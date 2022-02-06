package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.File;
import com.piccodi.yodisk.repo.FileRepo;
import com.piccodi.yodisk.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

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

    public String getNameFile(Long id){
        return fileRepo.findById(id).get().getFileName();
    }

    public void saveFile(MultipartFile file, Principal principal) {
        try{
            //todo сделать обработку else для if на сохранение в бд
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
        catch (Exception e){ e.printStackTrace(); }
    }

    public Resource downloadFile(Long fileId, Principal principal){
//todo почему блять эксепшн че с ним делать?
        Resource resource = null;
        try {
            var file = fileRepo.findById(fileId).get();
            var user = userRepo.findByUsername(principal.getName()).get();
            resource = storageService.download(file.getFileName(), user.getId());
        } catch (Exception e){e.printStackTrace();}

        return resource;

    }

    public void delete(Long file_id){
        try {
            //todo обработать потом правильно удаление с возможным вылетом ошибки

            var file = fileRepo.findById(file_id).get();
            var user = userRepo.fildUserByFile(file_id).get();

            if (storageService.delete(file.getFileName(), user.getId())) {
                user.deleteFile(file);
                fileRepo.delete(file);
            }
        } catch(Exception e){e.printStackTrace();}

    }

    public static class SizeCalculator{
        public static String calculate(Long size){
            double d = size.doubleValue()/(1024D*1024D);
            return String.format("%.2f", d);
        }
    }

}
