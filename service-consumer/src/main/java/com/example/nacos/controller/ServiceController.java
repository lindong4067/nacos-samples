package com.example.nacos.controller;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServiceController {
//    @LoadBalanced
//    @Autowired
//    private RestTemplate restTemplate;

//    @LoadBalanced
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    @Autowired
    private NacosServiceManager serviceManager;

    @GetMapping("/tl1/v1/{command}")
    public String invoke(@PathVariable String command) throws NacosException {
        List<ServiceInfo> serviceInfos = serviceManager.getNamingService().getSubscribeServices();
        for (ServiceInfo serviceInfo : serviceInfos) {
            String serviceName = serviceInfo.getName();
            List<Instance> instances = serviceManager.getNamingService().getAllInstances(serviceName);
            for (Instance instance : instances) {
                if (instance.containsMetadata("version")) {
                    String ver = instance.getMetadata().get("version");
                    if (instance.containsMetadata("command-codes")) {
                        String codes = instance.getMetadata().get("command-codes");
                        if (codes != null && codes.contains(command)) {
//                            return restTemplate.getForObject("http://" + serviceName + "/tl1/" + command, String.class);
                            return ver + "/" + serviceName;
                        }
                    }
                }
            }
        }
        return "command not support";
    }

    @GetMapping("/tl1/v2/{command}")
    public String invoke2(String command) throws NacosException {
        List<Instance> allInstances = serviceManager.getNamingService().getAllInstances("", true); //Param 'serviceName' is illegal, serviceName is blank
        for (Instance instance : allInstances) {
            if (instance.containsMetadata("version") &&
                    instance.containsMetadata("command-codes") &&
                    instance.getMetadata().get("command-codes").contains(command)) {
                String name = instance.getServiceName();
                String ver = instance.getMetadata().get("version");

                return ver + "/" + name;
            }
        }
        return "command not support";
    }
}
