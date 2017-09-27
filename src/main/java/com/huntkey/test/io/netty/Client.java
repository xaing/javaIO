package com.huntkey.test.io.netty;

import com.huntkey.test.io.nio.ClientHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

/**
 * Created by lulx on 2017/9/27 0027 上午 10:31
 */
public class Client implements Runnable {

    private static ClientHandler client = new ClientHandler();
    private String DEFAULT_HOST = "127.0.0.1";
    private int DEFAULT_PORT = 12345;

    public static void main(String[] args) throws Exception {
        new Thread(new Client()).start();
        @SuppressWarnings("resource")
        Scanner scanner = new Scanner(System.in);
        while(client.sendMsg(scanner.nextLine()));
    }

    public static ClientHandler start(){
        System.out.println("client is ok");
        new Thread(new Client()).start();
        return client;
    }


    @Override
    public void run() {
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(workerGroup);
            b.channel(NioSocketChannel.class);
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    socketChannel.pipeline().addLast(client);
                }
            });
            ChannelFuture f = b.connect(DEFAULT_HOST, DEFAULT_PORT).sync();
            f.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            workerGroup.shutdownGracefully();
        }
    }
}
