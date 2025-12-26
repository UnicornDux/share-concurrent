package org.ph.share.juc.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.StringJoiner;
import java.util.concurrent.PriorityBlockingQueue;

@Slf4j
public class PriorityQueueUsage {

    public static void main(String[] args) throws InterruptedException {

        // 创建优先级阻塞队列 (使用默认的排序规则)
        // PriorityBlockingQueue priority = new PriorityBlockingQueue(3);

        // 自定义排序规则 (传入一个比较器， 指定排序规则)
        PriorityBlockingQueue<Integer> priority = new PriorityBlockingQueue<>(
                3,
                (o1, o2) -> o2 - o1
        );

        StringJoiner joiner = new StringJoiner(" ");
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int item = random.nextInt(100);
            joiner.add(item + "");
            priority.put(item);
        }
        log.info("put\t: {}", joiner);

        joiner = new StringJoiner(" ");

        for (int i = 0; i < 5; i++) {
            joiner.add(priority.take() + "");
        }
        log.info("take\t: {}", joiner);
    }
}
