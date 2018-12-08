package com.shen.rpc.core.commons;

import lombok.Getter;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
@Getter
@Setter
public class RpcResponse implements Serializable {

    private static final long serialVersionUID = -1055211052940511362L;

    private String traceId;

    private boolean success;

    @Nullable
    private Object result;

    @Nullable
    private Throwable exception;
}
