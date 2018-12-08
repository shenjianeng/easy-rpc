package com.shen.rpc.core;

import com.shen.rpc.core.annotation.RpcServiceReferenceProcessor;
import com.shen.rpc.core.client.RpcProxy;
import com.shen.rpc.core.registry.ServiceDiscovery;
import com.shen.rpc.core.registry.impl.ZkServiceDiscovery;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
public class RpcClientAutoConfiguration {

    @Value("${rpc.zk.addr}")
    private String connectString;

    @Bean
    public ServiceDiscovery zkServiceDiscovery() {
        return new ZkServiceDiscovery(connectString);
    }

    @Bean
    public RpcProxy rpcProxy() {
        return new RpcProxy();
    }

    @Bean
    public RpcServiceReferenceProcessor rpcServiceReferencesProcessor() {
        return new RpcServiceReferenceProcessor();
    }
}
