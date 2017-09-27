package com.huntkey.test.io.nio;

import java.util.Scanner;

/**
 * Created by lulx on 2017/9/26 0026 下午 5:46
 */
public class Test {
    public static void main(String[] args) throws Exception {
        Server.start();
        Thread.sleep(1000);
        Client.start();
        while(Client.sendMsg(new Scanner(System.in).nextLine()));
    }
}
