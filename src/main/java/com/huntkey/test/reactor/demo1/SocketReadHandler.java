package com.huntkey.test.reactor.demo1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by lulx on 2017/9/27 0027 下午 6:03
 */
public class SocketReadHandler implements Runnable {

    private SocketChannel socketChannel;

    public SocketReadHandler(Selector selector, SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);

        SelectionKey selectionKey = socketChannel.register(selector, 0);

        selectionKey.attach(this);

        selectionKey.interestOps(SelectionKey.OP_READ);
        selector.wakeup();
    }

    @Override
    public void run() {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.clear();
        try {
            socketChannel.read(byteBuffer);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
