package com.piccodi.yodisk.service;

import com.piccodi.yodisk.entity.Link;
import com.piccodi.yodisk.repo.FileRepo;
import com.piccodi.yodisk.repo.LinkRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class LinkService {

    private LinkRepo linkRepo;

    private Pbkdf2PasswordEncoder linkEncoder;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    public void setLinkEncoder(Pbkdf2PasswordEncoder linkEncoder) {
        this.linkEncoder = linkEncoder;
    }

    @Autowired
    public void setLinkRepo(LinkRepo linkRepo) {
        this.linkRepo = linkRepo;
    }

    public String createLink(long fileId, String username){

        //todo генерировать норм ссылки, а не обрубки

        Link link;

        if(linkRepo.findByFile(fileId).isPresent()){
            link = linkRepo.findByFile(fileId).get();
        } else {
            link = new Link();
            link.setFileId(fileId);
        }

        var birthTime = System.currentTimeMillis();
        link.setKey(linkEncoder.encode(fileId + username + birthTime));
        link.setDeathTime(birthTime + (60 * 60) * 1000);
        linkRepo.save(link);
        System.out.println(request.getHeader("host"));
        return link.getKey();
    }
}
