package com.example.nacos.controller;

import com.example.nacos.command.CommandServiceRouter;
import com.example.nacos.feign.TL1CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {
//    @Autowired
    private TL1CommandService tl1CommandService;

    @GetMapping("/demo/{message}")
    public String invoke(@PathVariable String message) {
        return tl1CommandService.invoke(message);
    }

}
