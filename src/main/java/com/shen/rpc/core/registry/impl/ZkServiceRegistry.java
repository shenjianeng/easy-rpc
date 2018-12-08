package com.shen.rpc.core.registry.impl;

import com.shen.rpc.core.registry.ServiceRegistry;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.util.Assert;

/**
 * 基于ZK的服务注册
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
public class ZkServiceRegistry implements ServiceRegistry {

    private final String connectString;

    private CuratorFramework zkClient;


    public ZkServiceRegistry(String connectString) throws Exception {
        this.connectString = connectString;
        init();
    }

    private void init() throws Exception {
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(connectString)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();

        zkClient.start();

        //持久化节点REGISTRY_PATH 如果不存在,就创建
        Stat stat = zkClient.checkExists().forPath(ZkPathUtil.REGISTRY_PATH);
        if (stat == null) {
            zkClient.create()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath(ZkPathUtil.REGISTRY_PATH);
        }
    }

    /**
     * /registry/serviceName:serviceVersion/ip + port
     */
    @Override
    public void register(String serviceName, String serviceVersion, String ip, Integer port) {
        Assert.hasLength(serviceName, "serviceName is null");
        Assert.hasLength(serviceVersion, "serviceAddress is null");
        Assert.hasLength(ip, "ip is null");
        Assert.notNull(port, "port is null");

        try {
            //创建 持久节点  /registry/serviceName:serviceVersion/
            String servicePath = ZkPathUtil.getServicePath(serviceName, serviceVersion);

            Stat stat = zkClient.checkExists().forPath(servicePath);
            if (stat == null) {
                zkClient.create()
                        .creatingParentsIfNeeded()
                        .withMode(CreateMode.PERSISTENT)
                        .forPath(servicePath);

            }


            //创建 临时顺序 存储 服务地址
            zkClient.create()
                    .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                    .forPath(ZkPathUtil.getAddressPath(serviceName, serviceVersion, ip, port));

        } catch (Exception e) {

            throw new RuntimeException(e);
        }

    }
}
