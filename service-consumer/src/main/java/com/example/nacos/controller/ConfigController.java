package com.example.nacos.controller;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigChangeEvent;
import com.alibaba.nacos.api.config.ConfigChangeItem;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.listener.impl.AbstractConfigChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Collection;

//@RestController
//@RequestMapping("/config")
//@RefreshScope
public class ConfigController {

    public static final Logger LOGGER = LoggerFactory.getLogger(ConfigController.class);

    @Value("${version:v1.0}")
    private String version;

    @Autowired
    private NacosConfigManager nacosConfigManager;

    @RequestMapping("/version")
    public String get() {
        return version;
    }

    @RequestMapping("/service")
    public String service() throws NacosException {
        return nacosConfigManager.getConfigService().getConfig("service.xml", "DEFAULT_GROUP", 5000);
    }

    @PostConstruct
    public void init() throws NacosException {
        this.nacosConfigManager.getConfigService().addListener("service.xml", "DEFAULT_GROUP", new AbstractConfigChangeListener() {
            @Override
            public void receiveConfigChange(ConfigChangeEvent event) {
                Collection<ConfigChangeItem> items = event.getChangeItems();
                for (ConfigChangeItem item : items) {
                    LOGGER.info(item.getKey());
                }
            }

            @Override
            public void receiveConfigInfo(final String configInfo) {
                LOGGER.info(configInfo);
            }
        });
    }
}
