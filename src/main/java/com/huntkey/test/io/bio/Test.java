package com.huntkey.test.io.bio;

import java.io.IOException;
import java.util.Random;

/**
 * Created by lulx on 2017/9/26 0026 上午 10:38
 */
public class Test {
    public static void main(String[] args) throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //ServerNormal.start();
                    ServerBetter.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100);
        final char[] operators = {'+','-','*','/'};
        final Random random = new Random(System.currentTimeMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    String expression = random.nextInt(10) + "" +operators[random.nextInt(4)] + (random.nextInt(10) + 1);
                    System.out.println("expression : " + expression);
                    Client.send(expression);
                    try {
                        Thread.currentThread().sleep(random.nextInt(1000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
