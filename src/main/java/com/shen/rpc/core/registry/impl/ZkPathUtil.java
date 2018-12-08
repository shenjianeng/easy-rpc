package com.shen.rpc.core.registry.impl;


import com.shen.rpc.core.registry.ServiceAddressVO;
import org.springframework.util.StringUtils;

/**
 * /registry/serviceName:serviceVersion/ip:port
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
abstract class ZkPathUtil {

    static final String REGISTRY_PATH = "/registry";

    /**
     * /registry/serviceName:serviceVersion
     */
    static String getServicePath(String serviceName, String version) {
        return REGISTRY_PATH + "/" + serviceName + ":" + version;
    }

    /**
     * /registry/serviceName:serviceVersion/ip:port
     */
    static String getAddressPath(String serviceName, String version, String ip, Integer port) {
        return getServicePath(serviceName, version) + "/" + ip + ":" + port + ":";
    }

    static ServiceAddressVO getServiceAddressVO(String zkAddress) {
        if (StringUtils.hasText(zkAddress)) {
            String[] split = zkAddress.split(":");
            ServiceAddressVO vo = new ServiceAddressVO();
            vo.setIp(split[0]);
            vo.setPort(Integer.valueOf(split[1]));
            return vo;
        }
        return null;
    }
}
