package com.shen.rpc.core.server;

import com.shen.rpc.core.annotation.RpcService;
import com.shen.rpc.core.commons.codec.RpcObjectDecoder;
import com.shen.rpc.core.commons.codec.RpcObjectEncoder;
import com.shen.rpc.core.registry.ServiceRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 获取本server所提供的服务,启动服务
 *
 * @author shenjianeng
 * @date 2018/12/4
 */
@Slf4j
@RequiredArgsConstructor
public class RpcServer implements ApplicationListener<ContextRefreshedEvent> {

    private final String ip;

    private final Integer port;

    private final ServiceRegistry serviceRegistry;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        setupAndRegister(contextRefreshedEvent.getApplicationContext());

    }

    /**
     * 启动服务
     */
    private void setupAndRegister(ApplicationContext ctx) {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new RpcObjectDecoder())
                                .addLast(new RpcObjectEncoder())
                                .addLast(new RpcServerHandler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        try {

            ChannelFuture future = serverBootstrap.bind(ip, port).sync();

            findEasyRpcService(ctx);

            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);

        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 找到rpc服务实现类
     */
    private void findEasyRpcService(ApplicationContext ctx) {
        Map<String, Object> beans = ctx.getBeansWithAnnotation(RpcService.class);
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }

        for (Object serviceBean : beans.values()) {
            RpcService rpcService = AnnotationUtils.findAnnotation(serviceBean.getClass(), RpcService.class);
            Assert.notNull(rpcService, "rpcService is null");

            String serviceName = rpcService.value().getName();
            String serviceVersion = rpcService.version();

            RpcServerContext.add(serviceName, serviceVersion, serviceBean);

            serviceRegistry.register(serviceName, serviceVersion, ip, port);
        }
    }
}
