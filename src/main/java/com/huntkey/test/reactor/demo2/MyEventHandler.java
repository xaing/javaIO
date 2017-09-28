package com.huntkey.test.reactor.demo2;

/**
 * Created by lulx on 2017/9/28 0028 上午 11:44
 */
public class MyEventHandler {
    public int handleSignal(int signum){
        switch (signum){
            case 1:
                System.out.println("signum" + signum);
            case 2:
                System.out.println("signum" + signum);
            break;
        }

        return 0;
    }
}
