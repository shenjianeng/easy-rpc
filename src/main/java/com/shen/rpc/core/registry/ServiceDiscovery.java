package com.shen.rpc.core.registry;

import java.util.List;

/**
 * 服务发现
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
public interface ServiceDiscovery {

    List<ServiceAddressVO> discover(String serviceName, String serviceVersion);
}
