package com.sf.what.thread;

import org.junit.Test;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * Created by 80002093 on 2018/5/10.
 */
public class ThreadPoolExecutorTest {
    @Test
    public void test1() throws InterruptedException {

        Executor threadPool = Executors.newFixedThreadPool(2);

        while (true) {
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("一：" + Thread.currentThread().getName());
                }
            });
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("二：" + Thread.currentThread().getName());
                }
            });
            TimeUnit.SECONDS.sleep(1);
        }
    }

    @Test
    public void test2() throws Exception {

    }
}
