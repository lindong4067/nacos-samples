package com.example.nacos.feign;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface TL1CommandService {
    @GetMapping("/tl1/{command}")
    String invoke(@PathVariable("command") String command);
}
