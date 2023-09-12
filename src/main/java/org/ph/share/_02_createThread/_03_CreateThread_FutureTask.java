package org.ph.share._02_CreateThread;


import java.util.concurrent.*;

public class _03_CreateThread_FutureTask {
    public static void main(String[] args) {
        Callable<String> callable = () -> {
            System.out.println("我是子任务");
            return "sub task done";
        };
        FutureTask<String> task = new FutureTask(callable);
        Thread thread = new Thread(task);
        thread.start();
        System.out.println("子线程启动");
        try {
            // 阻塞等待结果，直到等到结果
            // task.get();
            // 阻塞等待，直到超时后不再等待
            String subResult = task.get(5, TimeUnit.MINUTES);

            System.out.println("子线程返回值：" + subResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        System.out.println("main 结束");
    }
}
