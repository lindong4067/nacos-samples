package com.example.nacos.controller;

import com.example.nacos.nacosconfig.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RefreshScope
public class UserController {
    @Autowired
    private User user;

    @RequestMapping("/user")
    public String user() {
        return "[HTTP] " + user;
    }
}
