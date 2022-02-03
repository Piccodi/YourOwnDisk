package com.piccodi.yodisk.controller;

import com.piccodi.yodisk.entity.File;
import com.piccodi.yodisk.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.ArrayList;

@Controller
@RequestMapping("/files")
public class FileController {

    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public String getFiles(Model model, Principal principal){

        var files = fileService.get(principal);
        model.addAttribute("files", files);
        return "files";
    }

    @PostMapping()
    public String saveFile(MultipartFile file, Principal principal){
        if(!file.isEmpty()){
            fileService.saveFile(file, principal);
        }
        return"redirect:/files";
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam("id") Long id){

        fileService.delete(id);
        return"redirect:/files";
    }
}
