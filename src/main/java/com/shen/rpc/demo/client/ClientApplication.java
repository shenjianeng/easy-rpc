package com.shen.rpc.demo.client;

import com.shen.rpc.core.annotation.EnableRpc;
import com.shen.rpc.core.annotation.RpcMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenjianeng
 * @date 2018-12-08
 */
@EnableRpc(mode = RpcMode.CLIENT)
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        System.setProperty("spring.profiles.active", "client");
        SpringApplication.run(ClientApplication.class, args);
    }
}
