package org.ph.share._02_CreateThread;

import org.ph.share.model.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class _09_ThreadLocal {

    static ThreadLocal<User> threadLocal = new ThreadLocal<>();
    public static void main(String[] args) {
        // threadLocalUsage();
        threadLocalOom();
    }



    // threadLocal 中的 key 是弱引用，但是 value 是强引用，在垃圾回收的时候，由于线程池内的线程
    // 不会被回收，因此 value 这样的强引用也不会被垃圾回收，时间久了就会造成内存的泄露
    // 为了演示，我们在运行这段代码的时候添加堆栈参数 : -Xmx=25m -Xms=25m
    public static void threadLocalOom(){
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 30; i++) {
            threadPool.execute(() -> {
                // 单独使用 User 的时候不会内存溢出
                // new User();

                // 使用 threadLocal 并且使用后没有手动移除，导致内存溢出
                threadLocal.set(new User());

                // 使用完成后手动移除
//                 threadLocal.remove();
            });
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        threadPool.shutdown();
    }

    // threadlocal 的基本用法
    public static void threadLocalUsage(){
        new Thread(() -> {
            threadLocal.set(new User());
            User user = threadLocal.get();

            for (int i = 0; i < 9988; i++) {
                user.age++;
            }
            System.out.println("9988 loal age is : " + user.age);
        }).start();

        new Thread(() -> {
            // 自行取出使用，多线程之间相互不会影响
            threadLocal.set(new User());
            User user = threadLocal.get();
            for (int i = 0; i < 7788; i++) {
                user.age++;
            }
            System.out.println("7788 local age is : " + user.age);
        }).start();
    }
}
