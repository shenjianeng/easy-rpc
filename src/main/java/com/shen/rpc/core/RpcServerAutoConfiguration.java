package com.shen.rpc.core;

import com.shen.rpc.core.registry.ServiceRegistry;
import com.shen.rpc.core.registry.impl.ZkServiceRegistry;
import com.shen.rpc.core.server.RpcServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
public class RpcServerAutoConfiguration {

    @Value("${rpc.zk.addr}")
    private String connectString;

    @Value("${rpc.server.ip}")
    private String ip;

    @Value("${rpc.server.port}")
    private Integer port;

    @Bean
    public ServiceRegistry zkServiceRegistry() throws Exception {
        return new ZkServiceRegistry(connectString);
    }


    @Bean
    public RpcServer easyRpcServer(ServiceRegistry serviceRegistry) {
        return new RpcServer(ip, port, serviceRegistry);
    }
}
