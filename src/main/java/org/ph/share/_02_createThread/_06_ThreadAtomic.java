package org.ph.share._02_CreateThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.ph.share.SmallTool;
import org.ph.share._02_CreateThread.NotSafe.StockRunnable;

public class _06_ThreadAtomic {

  public static void main(String[] args) {
    ExecutorService threadPool = Executors.newCachedThreadPool();
    
    StockRunnable task = new StockRunnable();

    try {
      for (int i=0; i < 1000000; i ++) {
        threadPool.execute(task);
      }
    }catch(Exception e) {
      e.printStackTrace();
    }finally {
      threadPool.shutdown();
      // 等待关闭
      try {
        threadPool.awaitTermination(100, TimeUnit.SECONDS);
      }catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    SmallTool.printTimeAndThread("剩余库存：" + NotSafe.stock);
  }
}

class NotSafe {
  public static Integer stock = 1000000;

  static class StockRunnable implements Runnable {

    @Override
    // 使用 synchronized 来保证操作的原子性
    public synchronized void run() {
    // public void run() {
      if (stock > 0) {
        // stock = stock - 1
        stock--;
      }
    }
  }
}
