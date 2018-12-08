package com.shen.rpc.core.registry;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 服务地址
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Getter
@Setter
public class ServiceAddressVO implements Serializable {

    private static final long serialVersionUID = 3955024858309149758L;

    /**
     * ip
     */
    private String ip;

    /**
     * 端口
     */
    private int port;

}
