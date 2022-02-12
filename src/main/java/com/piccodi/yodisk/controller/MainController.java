package com.piccodi.yodisk.controller;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class MainController {
    @GetMapping()
    public String getMain(){
        return "main";
    }

    @GetMapping("/share/{key}")
    public void download(@PathVariable("key") String key ){

       // return " ";
    }
}
