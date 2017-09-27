package com.huntkey.test.io.bio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lulx on 2017/9/26 0026 上午 11:42
 */
public class ServerBetter {
    private static int DEFAULT_PORT = 12345;
    private static ServerSocket server;
    private static ExecutorService executorService = Executors.newFixedThreadPool(60);

    public static void start() throws IOException {
        start(DEFAULT_PORT);
    }

    public static void start(int defaultPort) throws IOException {
        if(server != null) return;
        try {
            server = new ServerSocket(DEFAULT_PORT);
            System.out.println(" server start success, port : " + DEFAULT_PORT);
            while (true){
                Socket socket = server.accept();
                executorService.execute(new ServerHandle(socket));
            }
        }finally {
            if(server != null){
                server.close();
                System.out.println("server shut down");
            }
        }
    }

}
