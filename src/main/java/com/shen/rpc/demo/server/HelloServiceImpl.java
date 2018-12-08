package com.shen.rpc.demo.server;

import com.shen.rpc.core.annotation.RpcService;
import com.shen.rpc.demo.api.HelloService;
import org.springframework.stereotype.Service;

/**
 * @author shenjianeng
 * @date 2018-12-08
 */
@RpcService(value = HelloService.class, version = "1.0")
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String str) {
        return "hello : " + str;
    }
}
