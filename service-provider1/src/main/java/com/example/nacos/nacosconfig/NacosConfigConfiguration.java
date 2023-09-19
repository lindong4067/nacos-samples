package com.example.nacos.nacosconfig;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.StringReader;
import java.util.Properties;

@Configuration
@EnableConfigurationProperties(User.class)
public class NacosConfigConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(NacosConfigConfiguration.class);

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @Autowired
    private User user;

    @Value("${user.name}")
    private String userName;

    @Value("${user.age}")
    private int userAge;

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            String dataId = "user.properties";
            String group = "DEFAULT_GROUP";
            nacosConfigManager.getConfigService().addListener(dataId, group, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    LOGGER.info("[Listener] {}", configInfo);
                    LOGGER.info("[Before User] {}", user);

                    Properties properties = new Properties();
                    try {
                        properties.load(new StringReader(configInfo));
                        String name = properties.getProperty("user.name");
                        int age = Integer.parseInt(properties.getProperty("user.age"));
                        user.setName(name);
                        user.setAge(age);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    LOGGER.info("[After User] " + user);
                }
            });
        };
    }


    @PostConstruct
    public void init() {
        LOGGER.info("[init] user name : {} , age : {}", userName, userAge);
    }

    @PreDestroy
    public void destroy() {
        LOGGER.info("[destroy] user name : {} , age : {}", userName, userAge);
    }
}
