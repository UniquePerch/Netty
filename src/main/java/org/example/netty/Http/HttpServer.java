package org.example.netty.Http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.*;

import java.nio.charset.StandardCharsets;

public class HttpServer {
    public static void main(String[] args) {
        EventLoopGroup bossEventLoop = new NioEventLoopGroup(),workEventLoop = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap
                .group(bossEventLoop,workEventLoop)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {

                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                .addLast(new HttpRequestDecoder())   //Http请求解码器
                                .addLast(new HttpObjectAggregator(Integer.MAX_VALUE))
                                .addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        FullHttpRequest request = (FullHttpRequest) msg;
                                        System.out.println("收到客户端的数据："+request.headers());  //看看是个啥类型
                                        //收到浏览器请求后，我们需要给一个响应回去
                                        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);  //HTTP版本为1.1，状态码就OK（200）即可
                                        //直接向响应内容中写入数据
                                        response.content().writeCharSequence("Hello World!", StandardCharsets.UTF_8);
                                        ctx.channel().writeAndFlush(response);   //发送响应
                                        ctx.channel().close();   //HTTP请求是一次性的，所以记得关闭
                                    }
                                })
                                .addLast(new HttpResponseEncoder());   //响应记得也要编码后发送哦
                    }
                })
                .bind(8080);
    }
}
