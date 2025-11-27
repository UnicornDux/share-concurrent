package org.ph.share._05_CompletableFuture_expand;

import org.ph.share.SmallTool;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class _03thenAccept {
    public static void main(String[] args) {
        // run();
        accept();
        // both();
    }

    public static void both() {

        CompletableFuture<Integer> f1 = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(5) + 1;
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SmallTool.printTimeAndThread("第一阶段: " + number);
            return number;
        });

        CompletableFuture<Integer> f2 = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(5) + 1;
            try {
                TimeUnit.SECONDS.sleep(number);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            SmallTool.printTimeAndThread("第二阶段: " + number);
            return number;
        });

        f1.thenAcceptBoth(f2, (s1, s2) -> {
            SmallTool.printTimeAndThread("获取到结果: " + (s1 + s2));
        }).join();

    }

    public static void accept() {
        CompletableFuture<Void> thenAccept = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(5) + 1;
            SmallTool.printTimeAndThread("第一阶段：" + number);
            return number;
        }).thenAccept(number -> {
           SmallTool.printTimeAndThread("第二阶段: " + number + 5);
        });
        // Future 中返回的数据是 Null, 本身返回的就是 Void 泛型
        SmallTool.printTimeAndThread("works: " + thenAccept.join());
    }

    public static void run() {
        CompletableFuture<Void> thenRunWorks = CompletableFuture.supplyAsync(() -> {
            int number = new Random().nextInt(10);
            SmallTool.printTimeAndThread("第一阶段" + number);
            return number;
        }).thenRun(() -> {
            // 这里不接收上一个异步任务的结果，但是会消耗掉这个结果
            SmallTool.printTimeAndThread("Then run works");
        });

        SmallTool.printTimeAndThread("works" + thenRunWorks.join());
    }

}
