package com.shen.rpc.core.server;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
@Slf4j
abstract class RpcServerContext {

    /**
     * Map<serviceName,serviceBean>
     */
    private static final Map<String, Object> SERVICE_BEAN_MAP = new ConcurrentHashMap<>(128);

    private static String getKey(String serviceName, String serviceVersion) {
        return serviceName + ":" + serviceVersion;
    }

    static void add(String serviceName, String serviceVersion, Object service) {
        SERVICE_BEAN_MAP.putIfAbsent(getKey(serviceName, serviceVersion), service);
    }

    static Object get(String serviceName, String serviceVersion) {
        return SERVICE_BEAN_MAP.get(getKey(serviceName, serviceVersion));
    }


}
