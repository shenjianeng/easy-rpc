package com.shen.rpc.core.client;

import com.shen.rpc.core.commons.RpcRequest;
import com.shen.rpc.core.commons.RpcResponse;
import com.shen.rpc.core.commons.codec.RpcObjectDecoder;
import com.shen.rpc.core.commons.codec.RpcObjectEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
@Slf4j
@RequiredArgsConstructor
public class RpcClient {

    private final String ip;
    private final int port;

    public RpcResponse send(RpcRequest rpcRequest) throws Exception {
        RpcClientHandler rpcClientHandler = new RpcClientHandler();


        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
                                .addLast(new RpcObjectDecoder())
                                .addLast(new RpcObjectEncoder())
                                .addLast(rpcClientHandler);
                    }
                });

        try {
            ChannelFuture sync = bootstrap.connect(ip, port).sync();

            sync.channel().writeAndFlush(rpcRequest);

            sync.channel().closeFuture().sync();

            return rpcClientHandler.getRpcResponse();

        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }


}
