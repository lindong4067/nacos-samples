package com.example.nacos.command;

import org.springframework.web.client.RestTemplate;

public class CommandServiceRouter {

    public <T> T chooseCommandService(Class<T> clazz, CommandServiceRoutingPolicy policy, RestTemplate restTemplate) {
        CommandServiceInvocationHandler invocationHandler = new CommandServiceInvocationHandler(clazz, policy, restTemplate);

//        return (T) Proxy.newProxyInstance(TL1CommandService.getClassLoader(), new Class[]{clazz}, invocationHandler);

        return null;
    }
}
