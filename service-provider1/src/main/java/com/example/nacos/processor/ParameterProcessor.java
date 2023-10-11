package com.example.nacos.processor;

/**
 * subclass:
 * PlainParameterProcessor
 * MetadataParameterProcessor
 * ContextParameterProcessor
 * DeviceInfoParameterProcessor
 * ScriptParameterProcessor
 */
public interface ParameterProcessor {

    Class<? extends Parameter> getParameterType();
    boolean processArgument(ParameterContext context);
    void setParameterName(String name);
    int getParameterIndex();
}
