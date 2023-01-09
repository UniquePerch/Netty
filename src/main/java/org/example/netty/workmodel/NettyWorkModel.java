package org.example.netty.workmodel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;

public class NettyWorkModel {
    public static void main(String[] args) {
        //这里我们使用NioEventLoopGroup实现类即可，创建BossGroup和WorkerGroup
        //当然还有EpollEventLoopGroup，但是仅支持Linux，这是Netty基于Linux底层Epoll单独编写的一套本地实现，没有使用NIO那套
        EventLoopGroup bossGroup = new NioEventLoopGroup(), workerGroup = new NioEventLoopGroup();
        EventLoopGroup eventLoopGroup = new DefaultEventLoop();
        //创建服务端启动引导类
        ServerBootstrap bootstrap = new ServerBootstrap();
        //可链式，就很棒
        bootstrap
                .group(bossGroup, workerGroup)   //指定事件循环组
                .channel(NioServerSocketChannel.class)   //指定为NIO的ServerSocketChannel
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new ChannelInboundHandlerAdapter() {
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println(buf.toString(StandardCharsets.UTF_8));
                                        ctx.fireChannelRead(msg);
                                    }
                                })
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        ByteBuf buf = (ByteBuf) msg;
                                        System.out.println(buf.toString(StandardCharsets.UTF_8));
                                        ctx.channel().writeAndFlush("收到");
                                    }
                                })
                                .addLast(new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("二号出站："+msg);
                                        ctx.writeAndFlush(Unpooled.wrappedBuffer(msg.toString().getBytes()));
                                    }
                                })
                                .addLast(new ChannelOutboundHandlerAdapter(){
                                    @Override
                                    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                        System.out.println("一号出站："+msg);
                                        ctx.writeAndFlush(msg);
                                    }
                                });
                    }
                })
                .bind(8080);
        //最后绑定端口，启动
    }
}
