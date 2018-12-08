package com.shen.rpc.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 引用RPC服务
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RpcServiceAutowired {

    /**
     * 服务版本号
     */
    String version();
}
