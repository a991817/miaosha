package com.dgut;

import org.junit.Test;

import java.util.Scanner;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class bingfaTest {
    int phoneNumbers = 100;
    int userNum = 1000;
    CountDownLatch latch = new CountDownLatch(userNum);
    CyclicBarrier barrier = new CyclicBarrier(userNum+1);

    @Test
    public void bingfa1() throws InterruptedException {
        long start = System.currentTimeMillis();
      for(int i = 0;i<userNum;i++){
          new Thread(new MyThread()).start();
          latch.countDown();
      }
        try {
            barrier.await();
            long end = System.currentTimeMillis();
            System.out.println("花费了："+(end - start));
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
//      Thread.sleep(5000);
    }
    class MyThread implements Runnable {
        @Override
        public void run() {
            try {
//                等待所有线程到齐
                latch.await();
                Thread.sleep(80);
                buyPhone();
                try {
                    barrier.await();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public synchronized void buyPhone(){
        if(phoneNumbers<=0){
            return;
        }
        phoneNumbers--;
        System.out.println(Thread.currentThread().getName()+"购买了，还剩："+phoneNumbers);
    }

    @Test
    public void testBarray(){
        CyclicBarrier barrier = new CyclicBarrier(1);
        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
        System.out.println(123);
    }
    @Test
    public void abc(){
        System.out.println(15.2%5);
    }
}
