//package com.example.nacos.feign;
//
//import feign.DeclarativeContract;
//import feign.MethodMetadata;
//import feign.Request;
//import org.springframework.web.bind.annotation.*;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Parameter;
//import java.lang.reflect.Type;
//import java.util.*;
//import java.util.function.Predicate;
//
//import static java.util.stream.Collectors.mapping;
//
//public class SpringContract {
//    static final String ACCEPT = "Accept";
//    static final String CONTENT_TYPE = "Content-Type";
//    private final List<GuardedAnnotationProcessor> classAnnotationProcessors = new ArrayList<>();
//    private final List<GuardedAnnotationProcessor> methodAnnotationProcessors = new ArrayList<>();
//    private final Map<Class<Annotation>, DeclarativeContract.ParameterAnnotationProcessor<Annotation>> parameterAnnotationProcessors =
//            new HashMap<>();
//    public SpringContract() {
//        registerClassAnnotation(RequestMapping.class, (requestMapping, data) -> {
//            appendMappings(data, requestMapping.value());
//
//            if (requestMapping.method().length == 1)
//                data.template().method(Request.HttpMethod.valueOf(requestMapping.method()[0].name()));
//
//            handleProducesAnnotation(data, requestMapping.produces());
//            handleConsumesAnnotation(data, requestMapping.consumes());
//        });
//
//        registerMethodAnnotation(RequestMapping.class, (requestMapping, data) -> {
//            appendMappings(data, mapping(requestMapping.path(), requestMapping.value()));
//
//            if (requestMapping.method().length == 1)
//                data.template().method(Request.HttpMethod.valueOf(requestMapping.method()[0].name()));
//
//            handleProducesAnnotation(data, requestMapping.produces());
//            handleConsumesAnnotation(data, requestMapping.consumes());
//        });
//
//
//        registerMethodAnnotation(GetMapping.class, (mapping, data) -> {
//            appendMappings(data, mapping(mapping.path(), mapping.value()));
//            data.template().method(Request.HttpMethod.GET);
//            handleProducesAnnotation(data, mapping.produces());
//            handleConsumesAnnotation(data, mapping.consumes());
//        });
//
//        registerMethodAnnotation(PostMapping.class, (mapping, data) -> {
//            appendMappings(data, mapping(mapping.path(), mapping.value()));
//            data.template().method(Request.HttpMethod.POST);
//            handleProducesAnnotation(data, mapping.produces());
//            handleConsumesAnnotation(data, mapping.consumes());
//        });
//
//        registerMethodAnnotation(PutMapping.class, (mapping, data) -> {
//            appendMappings(data, mapping(mapping.path(), mapping.value()));
//            data.template().method(Request.HttpMethod.PUT);
//            handleProducesAnnotation(data, mapping.produces());
//            handleConsumesAnnotation(data, mapping.consumes());
//        });
//
//        registerMethodAnnotation(DeleteMapping.class, (mapping, data) -> {
//            appendMappings(data, mapping(mapping.path(), mapping.value()));
//            data.template().method(Request.HttpMethod.DELETE);
//            handleProducesAnnotation(data, mapping.produces());
//            handleConsumesAnnotation(data, mapping.consumes());
//        });
//
//        registerMethodAnnotation(PatchMapping.class, (mapping, data) -> {
//            appendMappings(data, mapping(mapping.path(), mapping.value()));
//            data.template().method(Request.HttpMethod.PATCH);
//            handleProducesAnnotation(data, mapping.produces());
//            handleConsumesAnnotation(data, mapping.consumes());
//        });
//
//        registerMethodAnnotation(ResponseBody.class, (body, data) -> {
//            handleProducesAnnotation(data, "application/json");
//        });
//        registerMethodAnnotation(ExceptionHandler.class, (ann, data) -> {
//            data.ignoreMethod();
//        });
//        registerParameterAnnotation(PathVariable.class, pathVariableParameterAnnotationProcessor());
//
//        registerParameterAnnotation(RequestBody.class, (body, data, paramIndex) -> {
//            handleConsumesAnnotation(data, "application/json");
//        });
//        registerParameterAnnotation(RequestParam.class, requestParamParameterAnnotationProcessor());
//        registerParameterAnnotation(RequestPart.class, requestPartParameterAnnotationProcessor());
//        registerParameterAnnotation(RequestHeader.class, requestHeaderParameterAnnotationProcessor());
//    }
//
//    private DeclarativeContract.ParameterAnnotationProcessor<PathVariable> pathVariableParameterAnnotationProcessor() {
//        return (parameterAnnotation, data, paramIndex) -> {
//            Parameter parameter = data.method().getParameters()[paramIndex];
//            String name = parameterName(parameterAnnotation.name(), parameterAnnotation.value(),
//                    parameter);
//            nameParam(data, name, paramIndex);
//        };
//    }
//
//    private String[] mapping(String[] path, String[] value) {
//        return new String[0];
//    }
//
//    protected <E extends Annotation> void registerClassAnnotation(Class<E> annotationType,
//                                                                  DeclarativeContract.AnnotationProcessor<E> processor) {
//        registerClassAnnotation(
//                annotation -> annotation.annotationType().equals(annotationType),
//                processor);
//    }
//
//    private DeclarativeContract.ParameterAnnotationProcessor<RequestPart> requestPartParameterAnnotationProcessor() {
//        return (parameterAnnotation, data, paramIndex) -> {
//            Parameter parameter = data.method().getParameters()[paramIndex];
//            String name = parameterName(parameterAnnotation.name(), parameterAnnotation.value(),
//                    parameter);
//            data.template().methodMetadata().formParams().add(name);
//            nameParam(data, name, paramIndex);
//        };
//    }
//
//    private boolean isUserPojo(Type type) {
//        String typeName = type.toString();
//        return !typeName.startsWith("class java.");
//    }
//
//
//    private void appendMappings(MethodMetadata data, String[] mappings) {
//        for (int i = 0; i < mappings.length; i++) {
//            String methodAnnotationValue = mappings[i];
//            if (!methodAnnotationValue.startsWith("/") && !data.template().url().endsWith("/")) {
//                methodAnnotationValue = "/" + methodAnnotationValue;
//            }
//            if (data.template().url().endsWith("/") && methodAnnotationValue.startsWith("/")) {
//                methodAnnotationValue = methodAnnotationValue.substring(1);
//            }
//
//            data.template().uri(data.template().url() + methodAnnotationValue);
//        }
//    }
//
//    private void handleProducesAnnotation(MethodMetadata data, String... produces) {
//        if (produces.length == 0)
//            return;
//        data.template().removeHeader(ACCEPT); // remove any previous produces
//        data.template().header(ACCEPT, produces[0]);
//    }
//
//    private void handleConsumesAnnotation(MethodMetadata data, String... consumes) {
//        if (consumes.length == 0)
//            return;
//        data.template().removeHeader(CONTENT_TYPE); // remove any previous consumes
//        data.template().header(CONTENT_TYPE, consumes[0]);
//    }
//
//    protected Collection<String> addTemplatedParam(Collection<String> possiblyNull, String name) {
//        if (possiblyNull == null) {
//            possiblyNull = new ArrayList<>();
//        }
//        possiblyNull.add(String.format("{%s}", name));
//        return possiblyNull;
//    }
//
//
//
//    /**
//     * Called while class annotations are being processed
//     *
//     * @param predicate to check if the annotation should be processed or not
//     * @param processor function that defines the annotations modifies {@link MethodMetadata}
//     */
//    protected <E extends Annotation> void registerClassAnnotation(Predicate<E> predicate,
//                                                                  DeclarativeContract.AnnotationProcessor<E> processor) {
//        this.classAnnotationProcessors.add(new GuardedAnnotationProcessor(predicate, processor));
//    }
//
//    /**
//     * Called while method annotations are being processed
//     *
//     * @param annotationType to be processed
//     * @param processor function that defines the annotations modifies {@link MethodMetadata}
//     */
//    protected <E extends Annotation> void registerMethodAnnotation(Class<E> annotationType,
//                                                                   DeclarativeContract.AnnotationProcessor<E> processor) {
//        registerMethodAnnotation(
//                annotation -> annotation.annotationType().equals(annotationType),
//                processor);
//    }
//
//    /**
//     * Called while method annotations are being processed
//     *
//     * @param predicate to check if the annotation should be processed or not
//     * @param processor function that defines the annotations modifies {@link MethodMetadata}
//     */
//    protected <E extends Annotation> void registerMethodAnnotation(Predicate<E> predicate,
//                                                                   DeclarativeContract.AnnotationProcessor<E> processor) {
//        this.methodAnnotationProcessors.add(new GuardedAnnotationProcessor(predicate, processor));
//    }
//
//    protected <E extends Annotation> void registerParameterAnnotation(Class<E> annotation,
//                                                                      DeclarativeContract.ParameterAnnotationProcessor<E> processor) {
//        this.parameterAnnotationProcessors.put((Class) annotation,
//                (DeclarativeContract.ParameterAnnotationProcessor) processor);
//    }
//}
