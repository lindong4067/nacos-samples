package com.example.nacos.command;

import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CommandServiceInvocationHandler implements InvocationHandler {

    public <T> CommandServiceInvocationHandler(Class<T> clazz, CommandServiceRoutingPolicy policy, RestTemplate restTemplate) {

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.equals(CommandServiceMethods.invoke)) {

        }
        return null;
    }

}
