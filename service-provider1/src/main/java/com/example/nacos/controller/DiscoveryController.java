package com.example.nacos.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DiscoveryController {
    @NacosInjected
    private NamingService namingService; // need add nacos-spring-boot-starter to pom.xml

    @GetMapping("/discovery")
    public List<Instance> get() throws NacosException {
        List<Instance> instanceList = new ArrayList<>();
        List<ServiceInfo> subscribeServices = namingService.getSubscribeServices();
        for (ServiceInfo serviceInfo : subscribeServices) {
            String serviceName = serviceInfo.getName();
            instanceList.addAll(namingService.getAllInstances(serviceName));
        }
        return instanceList;
    }
}
