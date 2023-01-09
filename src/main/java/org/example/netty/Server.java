package org.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

public class Server {
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
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println(buf.toString(StandardCharsets.UTF_8));
                                        ChannelPromise promise = ctx.newPromise();
                                        System.out.println(promise.isSuccess());
                                        ctx.writeAndFlush(Unpooled.wrappedBuffer("收到".getBytes()),promise);
                                        promise.sync();
                                        System.out.println(promise.isSuccess());
                                    }
                                });
                    }
                }) ;
        ChannelFuture future = bootstrap.bind(8080);
        future.addListener(f->{//人物完成之后才会启动这个Listener
//            System.out.println(f.isDone());
//            System.out.println("完成之后");
        });
    }
}
