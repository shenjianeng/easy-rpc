package com.shen.rpc.core.annotation;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
public enum RpcMode {

    /**
     * 服务端
     */
    SERVER,

    /**
     * 客户端
     */
    CLIENT,

    /**
     * 即为是服务端又是客户端
     */
    SERVER_AND_CLIENT
}
