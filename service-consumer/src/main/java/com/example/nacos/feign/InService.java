package com.example.nacos.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

//@FeignClient("in")
public interface InService {
    @GetMapping("/tl1/{command}")
    String invoke(@PathVariable("command") String command);
}
