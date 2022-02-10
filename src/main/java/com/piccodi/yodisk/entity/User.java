package com.piccodi.yodisk.entity;

import com.piccodi.yodisk.permissions.Role;

import javax.persistence.*;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private Role role;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private Set<File> files;

    public void addFile(File file){
        files.add(file);
    }

    public void deleteFile(File file){
        files.remove(file);
    }

    public Set<File> getFileSet() {
        return files;
    }

    public void setFileSet(Set<File> fileSet) {
        this.files = fileSet;
    }

    public Long getId() {return id;}

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
