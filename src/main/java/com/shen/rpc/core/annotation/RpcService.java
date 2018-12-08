package com.shen.rpc.core.annotation;

import java.lang.annotation.*;

/**
 * 暴露RPC服务
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcService {

    /**
     * 服务实现接口
     */
    Class<?> value();

    /**
     * 服务版本号
     */
    String version();
}
