package com.shen.rpc.core.registry;

/**
 * 服务注册
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
public interface ServiceRegistry {

    void register(String serviceName, String serviceVersion, String ip, Integer port);


}
