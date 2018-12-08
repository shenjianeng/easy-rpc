package com.shen.rpc.core.commons.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author shenjianeng
 * @date 2018/12/4
 */
public class RpcObjectDecoder extends LengthFieldBasedFrameDecoder {

    public RpcObjectDecoder() {
        super(1024 * 1024, 0, 4, 0, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }
        Input input = new Input(new ByteBufInputStream(frame));
        Kryo kryo = new Kryo();
        return kryo.readClassAndObject(input);
    }
}
