package com.example.nacos.command;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.example.nacos.feign.TL1CommandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class CommandServiceConfiguration {

    @Autowired
    private NacosServiceManager serviceManager;
    @Bean
    public TL1CommandService tl1CommandService() {
        CommandServiceBuilder builder = new CommandServiceBuilder();
        builder.setServiceManager(serviceManager);
        builder.setPolicy(RoutingPolicy.RANDOM);
        builder.setRestTemplate(restTemplate());
        return builder.buildCommandService(TL1CommandService.class);
//        return new CommandServiceBuilder()
//                .buildCommandService(
//                        TL1CommandService.class,
//                        RoutingPolicy.RANDOM, // TODO get routing policy from configuration
//                        restTemplate());
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
