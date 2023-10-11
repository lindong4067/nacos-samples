package com.example.nacos.command;

import com.alibaba.cloud.nacos.NacosServiceManager;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Proxy;

public class CommandServiceBuilder {
    private RoutingPolicy policy;
    private RestTemplate restTemplate;
    private NacosServiceManager serviceManager;

    public RoutingPolicy getPolicy() {
        return policy;
    }

    public void setPolicy(RoutingPolicy policy) {
        this.policy = policy;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public NacosServiceManager getServiceManager() {
        return serviceManager;
    }

    public void setServiceManager(NacosServiceManager serviceManager) {
        this.serviceManager = serviceManager;
    }

    public <T> T buildCommandService(Class<T> clazz, RoutingPolicy policy, RestTemplate restTemplate) {
        CommandServiceInvocationHandler invocationHandler = new CommandServiceInvocationHandler(clazz, policy, restTemplate, serviceManager);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz}, invocationHandler);
    }


    public  <T> T  buildCommandService(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz},
                new CommandServiceInvocationHandler(clazz, policy, restTemplate, serviceManager));
    }
}
