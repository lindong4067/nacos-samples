package com.example.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
public class ServiceProvider1Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProvider1Application.class, args);
    }

}
