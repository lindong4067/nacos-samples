package com.example.nacos.command;

public class RouterFactory {
    public Router generateRouter(RoutingPolicy policy) {
        if (RoutingPolicy.RANDOM.equals(policy)) {
            return new RandomRouter();
        }
        return new RandomRouter();
    }
}
