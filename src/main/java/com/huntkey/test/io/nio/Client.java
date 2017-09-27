package com.huntkey.test.io.nio;

/**
 * Created by lulx on 2017/9/26 0026 下午 4:27
 */
public class Client {
    private static String DEFAULT_HOST = "127.0.0.1";
    private static int DEFAULT_PORT = 12345;
    private static ClientHandle clientHandle;
    public static void start(){
        start(DEFAULT_HOST, DEFAULT_PORT);
    }

    private static synchronized void start(String ip, int port) {
        if(clientHandle != null){
            clientHandle.stop();
        }
        clientHandle = new ClientHandle(ip, port);
        new Thread(clientHandle, "Server").start();
    }

    public static boolean sendMsg(String msg) throws Exception {
        if(msg.equalsIgnoreCase("q")) return false;
        clientHandle.sendMsg(msg);
        return true;
    }
}
