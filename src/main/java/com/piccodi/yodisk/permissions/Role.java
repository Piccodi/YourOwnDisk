package com.piccodi.yodisk.permissions;

public enum Role {
    USER("user"),
    ADMIN("admin");

    private final String name;

    Role(String roleName){
        this.name=roleName;
    }
    public String getName(){
        return name;
    }

}
