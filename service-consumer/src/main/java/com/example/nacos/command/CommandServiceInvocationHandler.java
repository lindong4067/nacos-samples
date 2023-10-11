package com.example.nacos.command;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.api.naming.pojo.ListView;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

public class CommandServiceInvocationHandler implements InvocationHandler {

    private Class<?> clazz;
    private RoutingPolicy policy;
    private RestTemplate restTemplate;
    private NacosServiceManager serviceManager;

    public <T> CommandServiceInvocationHandler(Class<T> clazz, RoutingPolicy policy, RestTemplate restTemplate, NacosServiceManager serviceManager) {
        this.clazz = clazz;
        this.policy = policy;
        this.restTemplate = restTemplate;
        this.serviceManager = serviceManager;
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
            return handleMethodAnnotation(method, args, RequestMethod.GET);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return handleMethodAnnotation(method, args, RequestMethod.POST);
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return handleMethodAnnotation(method, args, RequestMethod.PUT);
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return handleMethodAnnotation(method, args, RequestMethod.DELETE);
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return handleMethodAnnotation(method, args, RequestMethod.PATCH);
        } else {
            return method.invoke(proxy, args);
        }

    }

    private ClassMataData handleClassAnnotation(Class<?> clazz) {
        ClassMataData classMataData = new ClassMataData();
        if (clazz.isAnnotationPresent(RequestMapping.class)) {
            classMataData.requestMapping = clazz.getAnnotation(RequestMapping.class);
            classMataData.baseUrl = classMataData.requestMapping.value()[0];
            System.out.println("requestMapping: " + classMataData.requestMapping);
        }
        return classMataData;
    }

    private MethodMataData handleMethodAnnotation(Method method) {
        MethodMataData methodMataData = new MethodMataData();
        Annotation annotation = method.getAnnotation(GetMapping.class);

        //TODO
        return null;
    }

    private Object handleMethodAnnotation(Method method, Object[] args, RequestMethod requestMethod) {
        ClassMataData classMataData = handleClassAnnotation(clazz);
        MethodMataData methodMataData = new MethodMataData();
        methodMataData.requestMethod = requestMethod;
        methodMataData.parameterAnnotations = method.getParameterAnnotations();
        methodMataData.returnType = method.getReturnType();
        methodMataData.args = args;
        if (requestMethod.equals(RequestMethod.GET)) {
            methodMataData.getMapping = method.getAnnotation(GetMapping.class);
        } else if (requestMethod.equals(RequestMethod.POST)) {
            methodMataData.postMapping = method.getAnnotation(PostMapping.class);
        } else if (requestMethod.equals(RequestMethod.PUT)) {
            methodMataData.putMapping = method.getAnnotation(PutMapping.class);
        } else if (requestMethod.equals(RequestMethod.DELETE)) {
            methodMataData.deleteMapping = method.getAnnotation(DeleteMapping.class);
        } else if (requestMethod.equals(RequestMethod.PATCH)) {
            methodMataData.patchMapping = method.getAnnotation(PatchMapping.class);
        }
        System.out.println("classMataData: " + classMataData);
        return doHandleMethodAnnotation(classMataData, methodMataData);
    }

    private Object doHandleMethodAnnotation(ClassMataData classMataData, MethodMataData methodMataData) {
        StringBuilder uri = new StringBuilder();
        Map<String, Object> uriVariables = new HashMap<>();
        Object[] uriValues = new Object[1];
        if (classMataData.baseUrl != null) {
            uri.append(classMataData.baseUrl);
        }
        Class<?> returnType = methodMataData.returnType;
        if (methodMataData.requestMethod.equals(RequestMethod.GET)) {
            uri.append(methodMataData.getMapping.value()[0]);
            Object[] args = methodMataData.args;
            Annotation[][] parameterAnnotations = methodMataData.parameterAnnotations;
            for (int i = 0; i < parameterAnnotations.length; i++) {
                for (int j = 0; j < parameterAnnotations[i].length; j++) {
                    if (parameterAnnotations[i][j] instanceof PathVariable) {
                        PathVariable pathVariable = (PathVariable) parameterAnnotations[i][j];
                        if (!"".equals(pathVariable.value())) {
                            uriVariables.put(pathVariable.value(), args[i]);
                        } else {
                            uriValues[i] = args[i];
                        }
                    } else if (parameterAnnotations[i][j] instanceof RequestParam) {
                        RequestParam requestParam = (RequestParam) parameterAnnotations[i][j];
                        if ("".equals(requestParam.value())) {
                            uri.append("?").append(requestParam.value()).append(args[i]);
                        }
                    }
                }
            }
            String command = (String) args[0];
            Optional<Instance> instance = findCommandServiceInstanceByRoutingPolicy(policy, command);
            if (!instance.isPresent()) {
                return "Command not support";
            }
            StringBuilder url = new StringBuilder();
            url.append("http://").append(instance.get().getIp()).append(":").append(instance.get().getPort()).append(uri);
            if (!uriVariables.isEmpty()) {
                return restTemplate.getForObject(url.toString(), returnType, uriVariables);
            } else if (uriValues[0] != null) {
                return restTemplate.getForObject(url.toString(), returnType, uriValues);
            } else {
                return restTemplate.getForObject(URI.create(url.toString()), returnType);
            }
        }
        return "Unsupported method type";
    }

    private Optional<Instance> findCommandServiceInstanceByRoutingPolicy(RoutingPolicy policy, String command) {
        List<Instance> availableInstanceList = new ArrayList<>();
        try {
            ListView<String> servicesOfServer = serviceManager.getNamingService().getServicesOfServer(1, 100);
            List<String> services = servicesOfServer.getData();
            for (String service : services) {
                List<Instance> allInstances = serviceManager.getNamingService().getAllInstances(service, false);
                for (Instance instance : allInstances) {
                    if (instance.containsMetadata("command-code") &&
                            instance.getMetadata().get("command-code").contains(command)) {
                        availableInstanceList.add(instance);
                    }
                }
            }
        } catch (NacosException e) {
            throw new RuntimeException(e); //cannot throw exception
        }

        RouterFactory factory = new RouterFactory();
        Router router = factory.generateRouter(policy);
        return router.route(availableInstanceList);
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
        RequestMethod requestMethod;
        Class<?> returnType;
        Object[] args;
        GetMapping getMapping;
        PostMapping postMapping;
        PutMapping putMapping;
        DeleteMapping deleteMapping;
        PatchMapping patchMapping;
        Annotation[][] parameterAnnotations;
    }
}
