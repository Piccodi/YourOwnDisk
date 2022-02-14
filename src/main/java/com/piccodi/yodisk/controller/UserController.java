package com.piccodi.yodisk.controller;

import com.piccodi.yodisk.entity.User;
import com.piccodi.yodisk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@SessionAttributes("userDetails")
public class UserController {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/auth")
    public String authorize(@ModelAttribute("user") User user ){
        return "auth";
    }

    @GetMapping("/new")
    public String sighUp(@ModelAttribute("user") User user){

        return "registration";
    }

    @PostMapping("/new")
    public String saveUser(@ModelAttribute("user") User user){
        userService.save(user);
        return "redirect:/";
    }



}
