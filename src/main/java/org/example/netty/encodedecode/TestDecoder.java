package org.example.netty.encodedecode;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestDecoder extends MessageToMessageDecoder<ByteBuf> {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("正在解码...");
        String str = byteBuf.toString(StandardCharsets.UTF_8);
        list.add(str);
    }
}
