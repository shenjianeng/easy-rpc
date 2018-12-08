package com.shen.rpc.core.annotation;

import com.shen.rpc.core.client.RpcProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
public class RpcServiceReferenceProcessor implements BeanPostProcessor, Ordered {

    private Map<Class<?>, Object> PROXY_CACHE = new ConcurrentHashMap<>(128);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Field[] fields = AopUtils.getTargetClass(bean).getDeclaredFields();
        for (Field field : fields) {
            RpcServiceAutowired rpcServiceAutowired = AnnotationUtils.findAnnotation(field, RpcServiceAutowired.class);
            if (rpcServiceAutowired != null) {
                Class<?> typeClazz = field.getType();
                Assert.isTrue(typeClazz.isInterface(), "RpcServiceAutowired 只能使用在接口类型的字段上");
                field.setAccessible(true);
                Object proxyInstance = PROXY_CACHE.computeIfAbsent(typeClazz, clazz -> RpcProxy.newProxyInstance(typeClazz, rpcServiceAutowired.version()));
                ReflectionUtils.setField(field, bean, proxyInstance);
            }
        }
        return bean;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;

    }
}
