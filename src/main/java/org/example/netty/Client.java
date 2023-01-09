package org.example.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.netty.encodedecode.StringCodec;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        bootstrap
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        System.out.println("接收到消息:"+msg);
                                    }
                                })
//                                .addLast(new LengthFieldPrepender(4))
                                .addLast(new StringEncoder());
                    }
                });
        Channel channel = bootstrap.connect("localhost",8080).channel();
        try (Scanner scanner = new Scanner(System.in)){
            while (true){
                System.out.println("请输入要发送的内容：");
                String msg = scanner.nextLine();
                if(msg.isEmpty()) continue;
                if(msg.equals("exit")){
                    ChannelFuture future = channel.close();
                    future.sync();
                    break;
                }
                channel.writeAndFlush(msg);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
