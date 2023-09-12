package org.ph.share._02_CreateThread;

import java.util.concurrent.TimeUnit;

/**
 * 主要用于介绍一些 CPU 中的方法
 *  > Thread.sleep : CPU 休眠，主动让出时间片
 *  > Thread.yield : 主动让出CPU资源，重新参与CPU竞争
 *  > Thread.interrupted : 返回线程是否具有中断信号, 如果有同时清除中断信号
 *  > interrupt : 给线程标记中断信号(只是标记，如果线程内部没有处理中断，则不会中断)
 *  > isInterrupted : 判断线程是否有中断信号，不会清除中断信号(与静态方法interrupted不同)
 *  > join : 让调用这个方法的线程等待被调用线程执行完成后再往下执行
 *  > getName / setName : 获取或者设置线程名称
 *  > isAlive : 判断线程是否存活
 *  > setDaemon : 设置线程为守护线程
 *  > isDaemon : 判断一个线程是否为守护线程
 *  > getState : 获取线程状态
 */
public class _04_ThreadMethod {
  
  public static void main(String[] args) {

    Thread t1 = new Thread(() -> {
      System.out.println("t1 线程执行");
      try {
        // Thread 中的静态方法，获取线程是否打断撞断，并取消这个打断信号
        // 取消后下文不会抛出打断异常
        System.out.println("T1 interrupted1 " + Thread.interrupted());
        // 再次获取的时候就无法获取到打断信号了
        System.out.println("T1 interrupted2 " + Thread.interrupted());

        TimeUnit.SECONDS.sleep(2);
      }catch(InterruptedException e) {
        System.out.println("T1 接受到打断信号");
      }
    });
    // 设置线程的名称
    t1.setName("alibaba");
    System.out.println(t1.getName());
    t1.setDaemon(true);
    System.out.println("t1 is daemon : " + t1.isDaemon());

    // 子线程启动
    t1.start();
    t1.interrupt();

    // 主动让出线程占用的 CPU 资源
    Thread.yield();

    Thread t2 = new Thread(() -> {
      System.out.println("t2 线程执行");
      try {
        // 获取打断信号
        System.out.println("T2 interrupted1 " + Thread.currentThread().isInterrupted());
        // 获取打断信号
        System.out.println("T2 interrupted2 " + Thread.currentThread().isInterrupted());
        TimeUnit.SECONDS.sleep(1);
      }catch(InterruptedException e) {
        System.out.println("T2 接受到打断信号");
      }
    });

    t2.start();
    // 执行打断
    t2.interrupt();
    System.out.println("t2 " + t2.isAlive());
    try {
      t2.join();
      System.out.println("t2 after join isAlive: " + t2.isAlive() + ", t2 state: " + t2.getState());
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    try {
      // 线程休眠, 多长时间内不再参与 CPU 资源的使用竞争
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      // 线程中断
      e.printStackTrace();
    }
  }
}

