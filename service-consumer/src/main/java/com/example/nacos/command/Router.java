package com.example.nacos.command;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Optional;

public interface Router {
    Optional<Instance> route(List<Instance> instanceList);
}
