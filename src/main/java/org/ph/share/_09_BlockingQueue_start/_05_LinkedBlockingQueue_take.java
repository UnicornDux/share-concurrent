package org.ph.share._09_BlockingQueue_start;

import org.ph.share.SmallTool;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class _05_LinkedBlockingQueue_take {
    public static void main(String[] args) {
        BlockingQueue<String> blockingQueue = new LinkedBlockingQueue<>(3);

        try {
            blockingQueue.take();
        } catch (InterruptedException e) {
            SmallTool.printTimeAndThread("取元素被中断");
        }
    }
}
