package com.example.nacos.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/demo")
public interface TL1CommandService {
    @GetMapping("/tl1/{command}")
    @ResponseBody
    String invoke(@PathVariable("command") String command);
}
