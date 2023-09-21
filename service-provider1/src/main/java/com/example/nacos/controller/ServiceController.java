package com.example.nacos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {

    @GetMapping("/tl1/{command}")
    public String invoke(@PathVariable("command") String command) {

        return "[service-provider1] received [" + command + "] success!";
    }
}
