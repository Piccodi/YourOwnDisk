package com.piccodi.yodisk.controller;

import com.piccodi.yodisk.exception.InvalidLinkException;
import com.piccodi.yodisk.service.FileService;
import com.piccodi.yodisk.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.LinkOption;
import java.security.Principal;

@Controller
@RequestMapping("/files")
public class FileController {

    private FileService fileService;

    private LinkService linkService;

    @Autowired
    public void setLinkService(LinkService linkService) {this.linkService = linkService;}

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping()
    public String getFiles(Model model, Principal principal){

        model.addAttribute("files", fileService.get(principal));
        return "files";
    }

    @PostMapping()
    public String saveFile(MultipartFile file, Principal principal) throws IOException {

        if(!file.isEmpty()){
            fileService.saveFile(file, principal);
        }
        return"redirect:/files";
    }

    @PostMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("id") Long fileId, Principal principal)
            throws FileNotFoundException, MalformedURLException {

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                fileService.getNameFile(fileId) + "\"").body(fileService.downloadFileById(fileId, principal));
    }

    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> download(@PathVariable("key") String key )
            throws FileNotFoundException, MalformedURLException, InvalidLinkException {

        if (linkService.checkForValidity(key)) {
            var resource = fileService.downloadFileByKey(key);
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
                    resource.getFilename() + "\"").body(resource);
        }else{
            throw new InvalidLinkException("Invalid link");
        }
    }

    @PostMapping("/share")
    public String shareFile(@RequestParam("id")Long fileId, Principal principal, Model model)
            throws FileNotFoundException{

             model.addAttribute("ref", fileService.generateLink(fileId, principal));
        return"/links";
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam("id") Long id) throws IOException {

        fileService.delete(id);
        return"redirect:/files";
    }


}
