package com.shen.rpc.core.commons;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 封装PRC请求
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Getter
@Setter
public class RpcRequest implements Serializable {

    private static final long serialVersionUID = -3268800302462783334L;

    private String traceId;

    private String serviceName;

    private String serviceVersion;

    private String methodName;

    private Class<?>[] parameterTypes;

    private Object[] parameters;

}
