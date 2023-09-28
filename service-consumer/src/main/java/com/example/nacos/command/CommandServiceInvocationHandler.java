package com.example.nacos.command;

import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class CommandServiceInvocationHandler implements InvocationHandler {

    private Class<?> clazz;
    private CommandServiceRoutingPolicy policy;
    private RestTemplate restTemplate;

    public <T> CommandServiceInvocationHandler(Class<T> clazz, CommandServiceRoutingPolicy policy, RestTemplate restTemplate) {
        this.clazz = clazz;
        this.policy = policy;
        this.restTemplate = restTemplate;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //print
        System.out.println("clazz: " + clazz);
        System.out.println("policy: " + policy);
        System.out.println("restTemplate: " + restTemplate);
        System.out.println("method: " + method);
        Annotation[] annotations = method.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            System.out.println("annotations[" + i + "]: " + annotations[i]);
        }
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            for (int i1 = 0; i1 < parameterAnnotations[i].length; i1++) {
                System.out.println("parameterAnnotations[" + i + "][" + i1 + "]" + parameterAnnotations[i][i1]);
            }
        }

        for (int i = 0; i < args.length; i++) {
            System.out.println("args[" + i + "]: " + args[i]);
        }

        if (method.isAnnotationPresent(GetMapping.class)) {
            return handleGetMethodAnnotation(method, args);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return handlePostMethodAnnotation(method, args);
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return handlePutMethodAnnotation(method, args);
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return handleDeleteMethodAnnotation(method, args);
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return handlePatchMethodAnnotation(method, args);
        } else {
            return method.invoke(proxy, args);
        }

//        if (method.equals(CommandServiceMethods.invoke)) {
//        RequestEntity<String> entity = new RequestEntity<>(HttpMethod.GET, )
//            return restTemplate.getForObject("http://10.242.28.83:18081/tl1/ADD-ONU", String.class);
//        }
    }

    private ClassMataData handleClassAnnotation(Class<?> clazz) {
        ClassMataData classMataData = new ClassMataData();
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            classMataData.requestMapping = clazz.getAnnotation(RequestMapping.class);
            classMataData.baseUrl = classMataData.requestMapping.name();
            System.out.println("requestMapping: " + classMataData.requestMapping);
        }
        return null;
    }

    private Object handleGetMethodAnnotation(Method method, Object[] args) {
        ClassMataData classMataData = handleClassAnnotation(clazz);
        return null;
    }



    private Object handlePostMethodAnnotation(Method method, Object[] args) {
        return null;
    }

    private Object handlePutMethodAnnotation(Method method, Object[] args) {
        return null;
    }

    private Object handleDeleteMethodAnnotation(Method method, Object[] args) {
        return null;
    }

    private Object handlePatchMethodAnnotation(Method method, Object[] args) {
        return null;
    }

    private static class ClassMataData {
        RequestMapping requestMapping;
        String baseUrl;
    }

    private static class MethodMataData {

    }
}
