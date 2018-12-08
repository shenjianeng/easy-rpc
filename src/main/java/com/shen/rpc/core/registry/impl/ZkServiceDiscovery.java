package com.shen.rpc.core.registry.impl;

import com.shen.rpc.core.registry.ServiceAddressVO;
import com.shen.rpc.core.registry.ServiceDiscovery;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 基于ZK的服务发现
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
public class ZkServiceDiscovery implements ServiceDiscovery {

    private final String connectString;

    private CuratorFramework zkClient;

    public ZkServiceDiscovery(String connectString) {
        this.connectString = connectString;
        init();
    }

    private void init() {
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        zkClient.start();
    }


    @Override
    public List<ServiceAddressVO> discover(String serviceName, String serviceVersion) {


        String servicePath = ZkPathUtil.getServicePath(serviceName, serviceVersion);

        //判断 service 节点是否存在
        Stat stat;

        try {
            stat = zkClient.checkExists().forPath(servicePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (stat == null) {
            throw new RuntimeException(servicePath + " 不存在!");
        }

        //获取 服务 地址
        List<String> serviceList;
        try {
            serviceList = zkClient.getChildren().forPath(servicePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (CollectionUtils.isEmpty(serviceList)) {
            throw new RuntimeException(serviceName + " 无可用服务");
        }

        return serviceList.stream()
                .map(ZkPathUtil::getServiceAddressVO)
                .collect(Collectors.toList());
    }
}
