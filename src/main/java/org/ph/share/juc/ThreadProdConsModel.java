package org.ph.share.juc;


import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 生产者 消费者模型
 */
@Slf4j
public class ThreadProdConsModel {

    public static void main(String[] args) {

        ThreadProdConsModel model = new ThreadProdConsModel();

        Thread pthread = new Thread(() -> {
            try {
                for (int i = 0; i < 20; i++) {
                    model.producer(i);
                    Thread.sleep(100);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread cthread = new Thread(() -> {
            for (int i = 0; i < 20; i++) {
                try {
                    model.consumer();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        pthread.start();
        cthread.start();

        try {
            pthread.join();
            cthread.join();
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private final int MAX_SIZE = 10;
    private final Queue<Integer> queue = new LinkedList<>();


    public void producer(int item) throws InterruptedException {
        synchronized (queue) {
            while(queue.size() == MAX_SIZE) {
                queue.wait();
            }
            queue.add(item);
            log.info("Produced: {}", item);
            queue.notifyAll();
        }
    }

    public int consumer() throws InterruptedException {
        synchronized (queue) {
            while(queue.isEmpty()) {
                queue.wait();
            }
            int item = queue.remove();
            log.info("Consumed: {}", item);
            queue.notifyAll();
            return item;
        }
    }
}
