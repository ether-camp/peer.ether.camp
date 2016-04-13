package com.ethercamp.peer;

import com.ethercamp.peer.rest.JsonRpcServiceImpl;
import org.ethereum.config.DefaultConfig;
import org.ethereum.jsonrpc.JsonRpcImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
public class Application {

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context =
                SpringApplication.run(new Object[]{DefaultConfig.class, Application.class}, args);
        JsonRpcImpl rpc = context.getBean(JsonRpcServiceImpl.class);
    }
}
