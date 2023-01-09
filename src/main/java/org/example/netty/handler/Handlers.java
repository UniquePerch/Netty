package org.example.netty.handler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;
import io.netty.handler.ipfilter.RuleBasedIpFilter;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.InetSocketAddress;

public class Handlers {
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
                                .addLast(new RuleBasedIpFilter(new IpFilterRule() {
                                    @Override
                                    public boolean matches(InetSocketAddress inetSocketAddress) {
                                        return inetSocketAddress.getHostName().equals("127.0.0.1"); //只让主机访问
                                    }

                                    @Override
                                    public IpFilterRuleType ruleType() {
                                        return IpFilterRuleType.REJECT;
                                    }
                                }))
                                .addLast(new IdleStateHandler(10,10,0))
                                .addLast(new StringDecoder())
                                .addLast(new LoggingHandler(LogLevel.INFO)) //打印日志
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println(msg);
                                        ctx.channel().writeAndFlush(Unpooled.wrappedBuffer("收到".getBytes()));
                                    }
                                    @Override
                                    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                                        //没想到吧，这个方法原来是在这个时候用的
                                        if(evt instanceof IdleStateEvent) {
                                            IdleStateEvent event = (IdleStateEvent) evt;
                                            if(event.state() == IdleState.WRITER_IDLE) {
                                                System.out.println("好久都没写了，看视频的你真的有认真在跟着敲吗");
                                            } else if(event.state() == IdleState.READER_IDLE) {
                                                System.out.println("已经很久很久没有读事件发生了，好寂寞");
                                            }
                                        }
                                    }
                                })
                                .addLast(new StringEncoder());

                    }
                }) ;
        bootstrap.bind(8080);
    }
}
