package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.User;
import com.piccodi.yodisk.exception.UserAlreadyExistException;
import com.piccodi.yodisk.permissions.Role;
import com.piccodi.yodisk.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepo userRepo;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void save(User user) throws UserAlreadyExistException {
        //todo разобраться как обрабатывать ошибки в шоблонах

        if(userRepo.findByUsername(user.getUsername()).isEmpty()){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(user.getPassword());
            user.setRole(Role.USER);
            userRepo.save(user);
        } else{
            throw new UserAlreadyExistException("username already used");
        }

    }

}
