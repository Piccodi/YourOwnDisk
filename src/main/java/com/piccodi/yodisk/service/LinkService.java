package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.Link;
import com.piccodi.yodisk.repo.FileRepo;
import com.piccodi.yodisk.repo.LinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LinkService {

    private LinkRepo linkRepo;

    private FileRepo fileRepo;

    private Pbkdf2PasswordEncoder linkEncoder;

    @Autowired
    public void setFileRepo(FileRepo fileRepo) {this.fileRepo = fileRepo;}

    @Autowired
    public void setLinkEncoder(Pbkdf2PasswordEncoder linkEncoder) {
        this.linkEncoder = linkEncoder;
    }

    @Autowired
    public void setLinkRepo(LinkRepo linkRepo) {
        this.linkRepo = linkRepo;
    }

    public String createLink(long fileId, String username){
//todo сделать проверку на существование и перезапись новой вместо старой

        Link link;
        if(linkRepo.findByFileId(fileId).isPresent()){
            link = linkRepo.findByFileId(fileId).get();
        } else {
            link = new Link();
            link.setFile(fileRepo.findById(fileId).get());
        }

        System.out.println(System.currentTimeMillis());
        var birthTime = System.currentTimeMillis();
        link.setKey(linkEncoder.encode(fileId + username + birthTime));
        link.setDeathTime(birthTime + (60 * 60) * 1000);
        linkRepo.save(link);
        System.out.println(link.getId());
        return link.getKey();
    }
}
