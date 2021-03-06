package com.xzy.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Lock;

public class CLHLockTest {

    private static int count = 0;

    public static void main(String[] args) {
        final CyclicBarrier cb = new CyclicBarrier(10, () -> System.out.println(count));

        CLHLock clhLock = new CLHLock();
        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                testLock(clhLock);
                try {
                    cb.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();

        }
    }


    public static void testLock(Lock lock) {
        System.out.println(Thread.currentThread().getName() + ": ready to lock");
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + ": locked");
            for (int i = 0; i < 100000; i++) ++count;
        } finally {
            System.out.println(Thread.currentThread().getName() + ": unlocked");
            lock.unlock();
        }
    }
}
