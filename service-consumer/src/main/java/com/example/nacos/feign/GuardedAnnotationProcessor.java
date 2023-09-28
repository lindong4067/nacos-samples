package com.example.nacos.feign;

import feign.DeclarativeContract;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

public class GuardedAnnotationProcessor {
    public <E extends Annotation> GuardedAnnotationProcessor(Predicate<E> predicate, DeclarativeContract.AnnotationProcessor<E> processor) {

    }
}
