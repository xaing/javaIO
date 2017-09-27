package com.huntkey.test.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lulx on 2017/9/26 0026 下午 3:03
 */
public class ServerHandle implements Runnable{

    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean started;

    public ServerHandle(int port) {
        try{
            // 创建选择器
            selector = Selector.open();
            // 打开监听通道
            serverSocketChannel = ServerSocketChannel.open();
            // 如果为 true，则此通道将被置于阻塞模式；如果为 false，则此通道将被置于非阻塞模式
            serverSocketChannel.configureBlocking(false); // 开启非阻塞模式
            // 绑定端口 backlog设为1024
            serverSocketChannel.socket().bind(new InetSocketAddress(port), 1024);
            // 监听客户端连接请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 标记服务器已开启
            started = true;
            System.out.println(" server start success, port : " + port);
        }catch (Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        // 循环遍历selector
        while (started){
            try {
                // 无论是否发生读写时间，selector每个1s被唤醒一次
                selector.select(1000);
                // 阻塞，只有当至少一个注册的事情发生的时候才会继续
                // selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while (it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        hanleInput(key);
                    }catch (Exception e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (Throwable t){
                t.printStackTrace();
            }
        }
        // selector关闭后会自动释放里面管理的资源
        if(selector != null){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void hanleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            if(key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                // 通过ServerSocketChannel的accept创建SocketChannel实例
                // 完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
                SocketChannel sc = ssc.accept();
                // 设置为非阻塞
                sc.configureBlocking(false);
                // 注册为读
                sc.register(selector, SelectionKey.OP_READ);
            }
            if(key.isReadable()){
                SocketChannel sc = (SocketChannel) key.channel();
                // 创建ByteBuffer，并开辟一个1M的缓冲区
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                // 读取请求码流，返回读取到的字节数
                int readBytes = sc.read(buffer);
                // 对字节进行解码
                if(readBytes > 0){
                    // 将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
                    buffer.flip();
                    // 根据缓冲区字节数创建字节数组
                    byte[] bytes = new byte[buffer.remaining()];
                    // 将缓冲区可读字节数组复制到新建的数组中
                    buffer.get(bytes);
                    String expression = new String(bytes, "UTF-8");
                    System.out.println("server get msg : " + expression);
                    doWrite(sc, " server return msg for : " + expression);
                }else if(readBytes < 0) {
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doWrite(SocketChannel channel, String response) throws IOException {
        byte[] bytes = response.getBytes();
        ByteBuffer writerBuffer = ByteBuffer.allocate(bytes.length);
        writerBuffer.put(bytes);
        writerBuffer.flip();
        // 发生缓冲区的字节数组
        channel.write(writerBuffer);
    }
}
