package org.ph.share._04_CompletableFuture_advance;


import org.ph.share.SmallTool;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class _04_AnyOf {

    static Random random = new Random();

    public static void main(String[] args) {

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(4));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "hello";
        });

        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(4));
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return "world";
        });

        // 运行任意个结束则结束
        CompletableFuture<Object> any = CompletableFuture.anyOf(f1, f2);
        SmallTool.printTimeAndThread("" + any.join());

        // 运行所有任务结束
        CompletableFuture.allOf(f1, f2);

        SmallTool.printTimeAndThread(f1.join() + " -> " + f2.join());
    }
}
