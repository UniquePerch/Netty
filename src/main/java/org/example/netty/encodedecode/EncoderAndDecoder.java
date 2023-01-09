package org.example.netty.encodedecode;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EncoderAndDecoder {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup boosGroup = new NioEventLoopGroup(), eventGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(boosGroup,eventGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
//                                .addLast(new FixedLengthFrameDecoder(10))
//                                .addLast(new DelimiterBasedFrameDecoder(1024,Unpooled.wrappedBuffer("!".getBytes())))
//                                .addLast(new LengthFieldBasedFrameDecoder(1024,0,4))
                                .addLast(new StringDecoder())
                                .addLast(new LoggingHandler(LogLevel.INFO))
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                        ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("收到".getBytes()));
                                    }
                                })
                                .addLast(new StringEncoder());

                    }
                }) ;
        bootstrap.bind(8080);
    }
}
