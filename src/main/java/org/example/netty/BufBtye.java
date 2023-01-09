package org.example.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class BufBtye {
//    public static void main(String[] args) {
//        //创建一个初始容量为10的ByteBuf缓冲区，这里的Unpooled是用于快速生成ByteBuf的工具类
//        //至于为啥叫Unpooled是池化的意思，ByteBuf有池化和非池化两种，区别在于对内存的复用，我们之后再讨论
//        ByteBuf buf = Unpooled.buffer(10);
//        System.out.println("初始状态："+ Arrays.toString(buf.array()));
//        buf.writeInt(-888888888);   //写入一个Int数据
//        System.out.println("写入Int后："+Arrays.toString(buf.array()));
//        buf.readShort();   //无需翻转，直接读取一个short数据出来
//        System.out.println("读取Short后："+Arrays.toString(buf.array()));
//        buf.discardReadBytes();   //丢弃操作，会将当前的可读部分内容丢到最前面，并且读写指针向前移动丢弃的距离
//        System.out.println("丢弃之后："+Arrays.toString(buf.array()));
//        buf.clear();    //清空操作，清空之后读写指针都归零
//        System.out.println("清空之后："+Arrays.toString(buf.array()));
//    }
//    public static void main(String[] args) {
//        ByteBuf buf = Unpooled.wrappedBuffer("abcdef".getBytes());
//        System.out.println(Arrays.toString(buf.array()));
//        buf.readByte();
//        ByteBuf slice = buf.slice();
//        System.out.println(slice.arrayOffset());
//        System.out.println(slice.readByte());
//        System.out.println(Arrays.toString(slice.array()));
//    }
//    public static void main(String[] args) {
//        CompositeByteBuf buf = Unpooled.compositeBuffer();
//        buf.addComponent(Unpooled.copiedBuffer("123".getBytes()));
//        buf.addComponent(Unpooled.copiedBuffer("456".getBytes()));
//        for(int i=0;i<buf.capacity();i++){
//            System.out.println((char) buf.getByte(i));
//        }
//    }

//    public static void main(String[] args) {
//        ByteBufAllocator allocator = PooledByteBufAllocator.DEFAULT;
//        ByteBuf buf = allocator.directBuffer(10);
//        buf.writeChar('C');
//        System.out.println(buf.readChar());
//        buf.release();
//
//        ByteBuf buf2 = allocator.directBuffer(10);
//        System.out.println(buf2 == buf);
//    }
}