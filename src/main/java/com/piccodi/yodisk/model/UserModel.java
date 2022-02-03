package com.piccodi.yodisk.model;

import com.piccodi.yodisk.entity.User;

public class UserModel {
    private Long id;
    private String username;

    public UserModel(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public static UserModel takeDetails(User user){
        return new UserModel(user.getId(), user.getUsername());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
