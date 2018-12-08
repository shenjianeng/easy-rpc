package com.shen.rpc.core.server;

import com.alibaba.fastjson.JSON;
import com.shen.rpc.core.commons.RpcRequest;
import com.shen.rpc.core.commons.RpcResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;


/**
 * 获取本server所提供的服务,启动服务
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {

        final RpcResponse response = new RpcResponse();
        response.setTraceId(rpcRequest.getTraceId());

        try {

            response.setResult(findMethodAndInvoke(rpcRequest));
            response.setSuccess(true);

        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            response.setSuccess(false);
            response.setException(e);
        }

        channelHandlerContext.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error(cause.getMessage(), cause);
        ctx.close();
    }


    private Object findMethodAndInvoke(RpcRequest rpcRequest) {
        String serviceName = rpcRequest.getServiceName();
        String serviceVersion = rpcRequest.getServiceVersion();

        Object target = RpcServerContext.get(serviceName, serviceVersion);

        Assert.notNull(target, "can not find service  by : " + JSON.toJSON(rpcRequest));

        Method method = ReflectionUtils.findMethod(target.getClass(), rpcRequest.getMethodName(), rpcRequest.getParameterTypes());

        Assert.notNull(method, "can not find service by : " + JSON.toJSON(rpcRequest));

        return ReflectionUtils.invokeMethod(method, target, rpcRequest.getParameters());

    }
}
