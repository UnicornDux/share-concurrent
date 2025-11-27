package org.ph.share._04_CompletableFuture_advance;

import org.ph.share.SmallTool;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class _02_applyToEither {
    public static void main(String[] args) {
        applyEither();
        runEither();
        acceptEither();
        runBoth();
    }

    // apply 获取到结果，并做处理，最终在 CompletableFuture 可以获取到结果
    public static void applyEither() {
        SmallTool.printTimeAndThread("张三走出餐厅，来到公交站");
        SmallTool.printTimeAndThread("等待 700路 或者 800路 公交到来");

        CompletableFuture<String> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("700路公交正在赶来");
            SmallTool.sleepMillis(100);
            return "700路到了";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("800路公交正在赶来");
            SmallTool.sleepMillis(200);
            return "800路到了";
        }), firstComeBus -> firstComeBus + ", 获取到座位");

        SmallTool.printTimeAndThread(String.format("%s,小白坐车回家", bus.join()));
    }


    // 两人比赛，只要有人过了终点，比赛结束，至于谁赢了，赛制并不关心
    public static void runEither(){
        CompletableFuture<Void> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("A正在冲刺");
            SmallTool.sleepMillis(100);
            return "A赢了";
        }).runAfterEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("B正在冲刺");
            SmallTool.sleepMillis(200);
            return "B赢了";
        }), () -> SmallTool.printTimeAndThread("比赛结束"));
    }

    // 考试等待所有人都考试结束，才算结束 (不关注具体做的结果，关心是否做完)
    public static void runBoth() {
        CompletableFuture<Void> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("A正在冲刺");
            SmallTool.sleepMillis(100);
            return "A交卷了";
        }).runAfterBoth(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("B正在冲刺");
            SmallTool.sleepMillis(200);
            return "B交卷了";
        }), () -> SmallTool.printTimeAndThread("考试结束"));
    }

    // 两人比赛，只要有人过了终点，比赛结束，直接消耗掉结果
    public static void acceptEither(){
        CompletableFuture<Void> bus = CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("A正在冲刺");
            SmallTool.sleepMillis(100);
            return "A赢了";
        }).acceptEither(CompletableFuture.supplyAsync(() -> {
            SmallTool.printTimeAndThread("B正在冲刺");
            SmallTool.sleepMillis(200);
            return "B赢了";
        }), (value) -> SmallTool.printTimeAndThread("比赛结束" + value));
    }
}
