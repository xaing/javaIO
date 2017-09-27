package com.huntkey.test.io.nio;


/**
 * Created by lulx on 2017/9/26 0026 下午 3:02
 */
public class Server {
    private static int DEFAULT_PORT = 12345;
    private static ServerHandle serverHandle;

    public static void start(){
        start(DEFAULT_PORT);
    }

    public static void start(int port) {
        if(serverHandle != null)
            serverHandle.stop();
        serverHandle = new ServerHandle(port);
        new Thread(serverHandle,"Server").start();
    }

    public static void main(String[] args) {
        start();
    }
}
