package com.example.nacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
//@EnableFeignClients
public class ServiceProvider2Application {

    public static void main(String[] args) {
        SpringApplication.run(ServiceProvider2Application.class, args);
    }

}
