package org.example.netty.encodedecode;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class StringCodec extends MessageToMessageCodec<ByteBuf,String>{
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, List<Object> list) throws Exception {
        list.add(Unpooled.wrappedBuffer(s.getBytes()));
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        String str = byteBuf.toString(StandardCharsets.UTF_8);
        list.add(str);
    }
}
