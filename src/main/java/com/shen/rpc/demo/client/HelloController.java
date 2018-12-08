package com.shen.rpc.demo.client;

import com.shen.rpc.core.annotation.RpcServiceAutowired;
import com.shen.rpc.demo.api.HelloService;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjianeng
 * @date 2018-12-08
 */
@RestController
public class HelloController {

    @RpcServiceAutowired(version = "1.0")
    private HelloService helloService1;

    @RpcServiceAutowired(version = "1.0")
    private HelloService helloService2;

    @GetMapping("/hello")
    public String hello1(String name) {
        Assert.isTrue(helloService1 == helloService2);
        return helloService1.sayHello(name);
    }
}
