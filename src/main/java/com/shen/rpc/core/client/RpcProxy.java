package com.shen.rpc.core.client;


import com.alibaba.fastjson.JSON;
import com.shen.rpc.core.commons.RpcRequest;
import com.shen.rpc.core.commons.RpcResponse;
import com.shen.rpc.core.registry.ServiceAddressVO;
import com.shen.rpc.core.registry.ServiceDiscovery;
import io.netty.util.internal.ThreadLocalRandom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.UUID;


/**
 * @author shenjianeng
 * @date 2018/12/4
 */
@Slf4j
public class RpcProxy implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    public static <T> T newProxyInstance(final Class<T> interfaceClass, final String serviceVersion) {
        Assert.isTrue(interfaceClass != null && interfaceClass.isInterface(), "interfaceClass is not interface");
        Assert.notNull(serviceVersion, "serviceVersion is null");

        Object proxyInstance = Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass},
                (proxy, method, parameters) -> {
                    final String serviceName = interfaceClass.getName();
                    final RpcRequest rpcRequest = new RpcRequest();
                    rpcRequest.setTraceId(UUID.randomUUID().toString());
                    rpcRequest.setServiceName(serviceName);
                    rpcRequest.setServiceVersion(serviceVersion);
                    rpcRequest.setMethodName(method.getName());
                    rpcRequest.setParameterTypes(method.getParameterTypes());
                    rpcRequest.setParameters(parameters);

                    ServiceDiscovery serviceDiscovery = ctx.getBean(ServiceDiscovery.class);

                    Assert.notNull(serviceDiscovery, "serviceDiscovery is null");

                    List<ServiceAddressVO> serviceAddressVOList = serviceDiscovery.discover(serviceName, serviceVersion);
                    if (CollectionUtils.isEmpty(serviceAddressVOList)) {
                        throw new RuntimeException("server:[ " + serviceName + " ] , version:[ " + serviceVersion + " ] address is empty");
                    }

                    int size = serviceAddressVOList.size();
                    ServiceAddressVO serviceAddressVO =
                            size == 1 ?
                                    serviceAddressVOList.get(0) :
                                    serviceAddressVOList.get(
                                            ThreadLocalRandom.current().nextInt(0, size - 1));

                    RpcResponse response = new RpcClient(serviceAddressVO.getIp(), serviceAddressVO.getPort()).send(rpcRequest);

                    if (response.isSuccess()) {
                        return response.getResult();
                    }

                    log.error(response.getException().getMessage());
                    throw new RuntimeException("rpc: " + JSON.toJSONString(rpcRequest) + " 调用失败");
                }
        );

        return (T) proxyInstance;
    }
}
