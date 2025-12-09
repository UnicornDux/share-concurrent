package org.ph.share._04_CompletableFuture_advance;

import org.ph.share.SmallTool;

import java.awt.image.SampleModel;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class _05_Tea {

    public static void main(String[] args) {

        CompletableFuture<String> work = CompletableFuture.supplyAsync(() -> {
            sleep(2, TimeUnit.SECONDS);
            SmallTool.printTimeAndThread("洗水壶...");

            sleep(10, TimeUnit.SECONDS);
            SmallTool.printTimeAndThread("烧水");

            return "开水";
        });

        CompletableFuture<String> follow = CompletableFuture.supplyAsync(() -> {
            sleep(2, TimeUnit.SECONDS);
            SmallTool.printTimeAndThread("洗茶壶...");

            sleep(2, TimeUnit.SECONDS);
            SmallTool.printTimeAndThread("洗茶杯...");

            sleep(1, TimeUnit.SECONDS);
            SmallTool.printTimeAndThread("拿茶叶...");
            return "龙井";
        });

        work.thenAcceptBoth(follow, (water, tea) -> {
            SmallTool.printTimeAndThread("使用" + water + "和" + tea + "泡茶");
        }).join();
    }

    static void sleep(int t, TimeUnit u) {
        try {
           u.sleep(t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
