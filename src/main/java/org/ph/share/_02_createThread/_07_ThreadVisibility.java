package org.ph.share._02_CreateThread;

import org.ph.share.SmallTool;

/**
 * 多线程下的共享资源在子线程之间相互之间不可见问题
 * ---------------------------------------------------------
 * > 
 */
public class _07_ThreadVisibility {

  static  Boolean always = true;

  public static void main(String[] args) throws InterruptedException {

    new Thread(() -> {
      while(always) {
        // 线程运行
        // 1. synchronized 在解锁的时候会将 CPU 缓存的变量
        // 写入到主内存中并重新读取
        synchronized (Thread.currentThread()) {}

        // System.out.println() 中也具有 synchronized,
        // 也可以达到同步的目的, 说明只要代码中存在同步代码块，则一定会触发工作内存向主内存回写
        // SmallTool.printTimeAndThread("程序执行");

        // 3.可以将同步变量设置 volatile 关键字达到同步的目的
      }
    }).start();

    Thread.sleep(2000);
    // 主线程修改了变量的值，子线程中还在持续的输出    
    always = false;
  }
}


