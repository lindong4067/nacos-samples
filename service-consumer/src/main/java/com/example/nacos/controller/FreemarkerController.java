package com.example.nacos.controller;


import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FreemarkerController {
    private static final Logger logger = LoggerFactory.getLogger(FreemarkerController.class);

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @GetMapping("/ftl/{message}")
    public String invoke(@PathVariable String message) throws IOException, TemplateException {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("index.ftl");
        Map<String, Object> model = new HashMap<>();
        model.put("message", message);
        String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        logger.info(result);
        return result;
    }

    @GetMapping("/ftl")
    public String getTemplate(@RequestParam String intentTarget) throws IOException, TemplateException {
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("test/test.ftl");
        Map<String, Object> model = new HashMap<>();
        model.put("intentTarget", intentTarget);
        String result = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        logger.info(result);
        return result;
    }
}
