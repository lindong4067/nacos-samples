package com.example.nacos.controller;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import com.alibaba.nacos.api.naming.pojo.ServiceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
public class ServiceController {
    @Autowired
    private RestTemplate restTemplate;

//    @LoadBalanced
//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }
    @Autowired
    private NacosServiceManager serviceManager;

    private final Random random = new Random();

    @GetMapping("/service/tl1/{command}")
    public String invoke(@PathVariable String command) throws NacosException {
        List<ServiceInfo> serviceInfos = serviceManager.getNamingService().getSubscribeServices();
        for (ServiceInfo serviceInfo : serviceInfos) {
            String serviceName = serviceInfo.getName();
            List<Instance> instances = serviceManager.getNamingService().getAllInstances(serviceName);
            for (Instance instance : instances) {
                if (instance.containsMetadata("version")) {
                    String ver = instance.getMetadata().get("version");
                    if (instance.containsMetadata("command-code")) {
                        String codes = instance.getMetadata().get("command-code");
                        if (codes != null && codes.contains(command)) {
                            return restTemplate.getForObject("http://" + serviceName + "/tl1/" + command, String.class);
//                            return ver + "/" + serviceName;
                        }
                    }
                }
            }
        }
        return "command not support";
    }

    @GetMapping("/tl1/v2/{command}")
    public String invoke2(@PathVariable String command) throws NacosException {
        ListView<String> servicesOfServer = serviceManager.getNamingService().getServicesOfServer(1, 10);
        List<String> services = servicesOfServer.getData();
        List<String> ipAndPortList = new ArrayList<>();
        for (String service : services) {
            List<Instance> allInstances = serviceManager.getNamingService().getAllInstances(service, true);
            for (Instance instance : allInstances) {
                if (instance.containsMetadata("command-code") &&
                        instance.getMetadata().get("command-code").contains(command)) {
                    ipAndPortList.add(instance.getIp() + ":" + instance.getPort());
//                    return restTemplate.getForObject("http://" + serviceName + "/tl1/" + command, String.class);
                }
            }
        }
        if (ipAndPortList.isEmpty()) {
            return "unsupported command service";
        }

        int randomIndex = random.nextInt(ipAndPortList.size());
        String address = ipAndPortList.get(randomIndex);

        return restTemplate.getForObject("http://" + address + "/tl1/" + command, String.class);
    }
}
