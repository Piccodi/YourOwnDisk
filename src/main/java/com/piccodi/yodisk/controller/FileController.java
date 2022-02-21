package com.piccodi.yodisk.controller;

import com.piccodi.yodisk.exception.CustomResponseException;
import com.piccodi.yodisk.exception.ErrorResponse;
import com.piccodi.yodisk.exception.InvalidLinkException;
import com.piccodi.yodisk.service.FileService;
import com.piccodi.yodisk.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
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
    public String saveFile(MultipartFile file, Principal principal)
            throws CustomResponseException {
        try {
            if (!file.isEmpty()) {
                fileService.saveFile(file, principal);
            }
            return "redirect:/files";
        }catch (Exception e){
            throw new CustomResponseException(HttpStatus.BAD_REQUEST, ErrorResponse.messageForException(e));
        }
    }

    @PostMapping("/download")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@RequestParam("id") Long fileId, Principal principal)
            throws CustomResponseException {
        try {
            return ResponseEntity.ok().header(
                    HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; filename=\"" +
                    fileService.getNameFile(fileId) + "\"")
                    .body(fileService.downloadFileById(fileId, principal));
        } catch (Exception e) {
            throw new CustomResponseException(HttpStatus.NO_CONTENT, ErrorResponse.messageForException(e));
        }
    }
    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> downloadFIle(@PathVariable("key") String key )
            throws CustomResponseException{
        try {
            if (linkService.checkForValidity(key)) {
                var resource = fileService.downloadFileByKey(key);
                return ResponseEntity.ok().header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                        resource.getFilename() + "\"")
                        .body(resource);
            } else {
                throw new InvalidLinkException("Invalid link");
            }
        } catch (Exception e){
            throw new CustomResponseException(HttpStatus.NO_CONTENT, ErrorResponse.messageForException(e));
        }
    }

    @PostMapping("/share")
    public String shareFile(@RequestParam("id")Long fileId, Principal principal, Model model)
            throws CustomResponseException {
            try {
                model.addAttribute("ref", fileService.generateLink(fileId, principal));
                return "/links";
            } catch (Exception e){
                throw new CustomResponseException(HttpStatus.NO_CONTENT, ErrorResponse.messageForException(e));
            }
    }

    @PostMapping("/delete")
    public String deleteFile(@RequestParam("id") Long id) throws IOException {

        fileService.delete(id);
        return"redirect:/files";
    }
}
