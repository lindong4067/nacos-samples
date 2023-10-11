package com.example.nacos.command;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RandomRouter implements Router {
    private final Random random = new Random();
    @Override
    public Optional<Instance> route(List<Instance> instanceList) {
        if (instanceList == null || instanceList.isEmpty()) {
            return Optional.empty();
        }
        int randomIndex = random.nextInt(instanceList.size());
        return Optional.ofNullable(instanceList.get(randomIndex));
    }
}
