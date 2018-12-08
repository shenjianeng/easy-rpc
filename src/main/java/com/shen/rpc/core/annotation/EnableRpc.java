package com.shen.rpc.core.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启RPC服务
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcModeSelector.class})
public @interface EnableRpc {

    RpcMode mode();

}
