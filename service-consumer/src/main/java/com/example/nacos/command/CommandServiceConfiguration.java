package com.example.nacos.command;

import com.example.nacos.feign.TL1CommandService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommandServiceConfiguration {
    @Bean
    public TL1CommandService tl1CommandService() {
        return new CommandServiceRouter()
                .chooseCommandService(
                        TL1CommandService.class,
                        CommandServiceRoutingPolicy.RANDOM,
                        restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
