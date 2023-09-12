package org.ph.share._06_ThreadPool_gettingStarted;

import java.util.concurrent.*;

/**
 * 线程池中提交任务的时候有两个方法可以使用, 那么者两者治安有什么不同呢 ?
 * > submit
 *    - 参数为 Callable, 也可以是 runnable
 *    - 返回值为 Future
 *    - 产生的异常会被暂存起来，主线程在调用 get 方法获取结果的时候会被抛出,可以捕获处理
 * > execute
 *    - 参数为 Runnable
 *    - 返回值 void
 *    - 产生的异常直接在子线程中被抛出，主线程中无法捕获,
 */
public class _03_ExecuteAndSubmit {


    public static void main(String[] args){
//       testExecute();
       testSubmit();
    }

    // 测试 Execute 方式提交任务
    public static void testExecute(){
        ExecutorService threadPool = null;
        try {
            threadPool  = Executors.newFixedThreadPool(3);
            threadPool.execute(() -> {
                System.out.println("execute 执行......");
                int x = 1 / 0;
                // 异常直接在子线程中被抛出,需要子线程自己处理，无法被传送到主线程中
            });
        }catch (Exception e){
            // 主线程无法捕获子线程的异常
           System.out.println("捕获了 execute 异常" + e.getMessage());
        }finally {
            if (threadPool != null) {
                threadPool.shutdown();
            }
        }
    }

    // 测试 submit 方式提交任务
    public static void testSubmit() {
        ExecutorService threadPool = null;
        try {
            threadPool = Executors.newFixedThreadPool(3);
            Future<String> future = threadPool.submit((Callable<String>) () -> {
                System.out.println("submit 执行......");
                int x = 1 / 0;
                return "ok";
            });
            // 注释掉这行，看不到异常，说明异常被存储，直到 get 调用获取结果才在主线程中被抛出
            future.get();
        } catch (Exception e) {
            System.out.println("error : " + e.getMessage());
        }finally {
            if (threadPool != null) {
                threadPool.shutdown();
            }
        }
    }
}
