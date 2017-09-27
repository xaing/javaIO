package com.huntkey.test.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lulx on 2017/9/26 0026 上午 9:54
 */
public final class ServerNormal {
    private static int DEFAULT_PORT = 12345;
    private static ServerSocket server;
    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public synchronized static void start(int port) throws IOException {
        if(server != null) return;
        try {
            server = new ServerSocket(port);
            System.out.println(" server start success ； port : " + port);
            Socket socket;
            // 通过循环监听客户端连接
            while (true){
                // 如果没有客户端接入，将阻塞在accept操作上
                socket = server.accept();
                // 当有新的客户端接入时，创建一个新的线程处理这条Socket链路
                new Thread(new ServerHandle(socket)).start();
            }
        }finally {
            // 关闭
            if(server != null){
                server.close();
                System.out.println(" server is shutdown");
            }
        }

    }
}
