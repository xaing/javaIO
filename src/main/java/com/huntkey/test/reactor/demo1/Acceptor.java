package com.huntkey.test.reactor.demo1;

import java.nio.channels.SocketChannel;

/**
 * Created by lulx on 2017/9/27 0027 下午 5:39
 */
public class Acceptor implements Runnable {
    private Reactor reactor;

    public Acceptor(Reactor reactor) {
        this.reactor = reactor;
    }

    @Override
    public void run() {
        try {
            SocketChannel socketChannel = reactor.serverSocketChannel.accept();
            if(socketChannel != null){
                new SocketReadHandler(reactor.selector, socketChannel);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
