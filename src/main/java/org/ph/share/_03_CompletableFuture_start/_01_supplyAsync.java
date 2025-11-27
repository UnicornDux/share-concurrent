package org.ph.share._03_CompletableFuture_start;

import org.ph.share.SmallTool;

import java.awt.image.SampleModel;
import java.util.concurrent.CompletableFuture;

public class _01_supplyAsync {
    public static void main(String[] args) {
        //start();
        handle();
    }

    public static void start() {
        SmallTool.printTimeAndThread("小白进入餐厅");
        SmallTool.printTimeAndThread("小白点了 番茄炒蛋 + 一碗米饭");

        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            SmallTool.printTimeAndThread("厨师打饭");
            SmallTool.sleepMillis(100);
            return "番茄炒蛋 + 米饭 做好了";
        });
        CompletableFuture.runAsync(() -> {
            SmallTool.printTimeAndThread("服务员接过订单");
        });

        SmallTool.printTimeAndThread("小白在打王者");
        SmallTool.printTimeAndThread(String.format("%s ,小白开吃", cf1.join()));
    }

    public static void handle() {
        CompletableFuture<String> cf1 = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("厨师炒菜");
            SmallTool.sleepMillis(200);
            SmallTool.printTimeAndThread("厨师打饭");
            SmallTool.sleepMillis(100);
            // int A = 10 / 0;
            return "番茄炒蛋 + 米饭 做好了";
        });
        // 结果处理, 无论发生异常，都会触发该代码
        cf1.whenComplete((item, e) -> {
            // 通过参数, 在这里可以获取到结果，或者异常
            if (e == null) {
                SmallTool.printTimeAndThread("服务员上菜: " + item);
            }else {
                SmallTool.printTimeAndThread("发生了异常，服务员道歉: " + e.getMessage());
            }
        });

        // 异常处理 (任务中出现了异常将触发下面的代码
        cf1.exceptionally(e -> {
            SmallTool.printTimeAndThread("exception: " + e.getMessage());
            return "发生了异常";
        }).join();

        // 这里还可以通过 CompletableFuture  获取到结果
        SmallTool.printTimeAndThread("获取到结果:" + cf1.join());
    }
}
