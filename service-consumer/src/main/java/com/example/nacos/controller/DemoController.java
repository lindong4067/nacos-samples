package com.example.nacos.controller;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import com.example.nacos.feign.InService;
import com.example.nacos.feign.SaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//@RestController
public class DemoController {
    @Autowired
    private SaService saService;

    @Autowired
    private InService inService;

    @GetMapping("/tl1/v1/{message}")
    public String invoke(@PathVariable String message) {
        if (message.startsWith("ADD-ONU")) {
            return saService.invoke(message);
        } else if (message.startsWith("LST-ONU")) {
            return inService.invoke(message);
        } else {
            return "Unsupported command";
        }
    }
}
