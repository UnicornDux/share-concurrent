package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class LockQueue {

    public static void main(String[] args) {
        Queue queue = new Queue(10);
        new Thread(new Producer(queue)).start();
        new Thread(new Consumer(queue)).start();
    }
}


class Producer implements Runnable {

    private final Queue queue;

    public Producer(Queue queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while(true) {
                // 每隔 1 秒生产一个随机数
                Thread.sleep(1000);
                queue.put(new Random().nextInt(1000));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

@Slf4j
class Consumer implements Runnable {

    private final Queue queue;

    public Consumer(Queue queue){
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);
                log.info("消费: {}", queue.take());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


/**
 * 队列封装
 */
@Slf4j
class Queue {

    private final Object[] items;

    int size = 0;
    int takeIndex;
    int putIndex;

    private final ReentrantLock lock;

    public Condition notEmpty;// 消费者线程阻塞唤醒条件，队列为空阻塞，生产者生产完唤醒
    public Condition notFull; // 生产者线程阻塞唤醒条件，队列满了阻塞，消费者消费完唤醒

    public Queue(int capacity) {
        this.items = new Object[capacity];
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
        notFull = lock.newCondition();
    }

    public void put(Object value) throws Exception {
        lock.lock();
        try {
            while(size == items.length) {
                notFull.await();
            }

            items[putIndex] = value;
            if (++putIndex == items.length) {
                putIndex = 0;
            }
            size++;
            notEmpty.signal(); // 生产完唤醒消费者
        }finally {
            log.info("生产者产出: {}", value);
            lock.unlock();
        }
    }

    public Object take() throws Exception {
        lock.lock();
        try {
            // 队列空了就让消费者等待
            while (size == 0) notEmpty.await();
            Object value = items[takeIndex];
            items[takeIndex] = null;
            if (++takeIndex == items.length) {
                takeIndex = 0;
            }
            size--;
            notFull.signal();
            return value;
        }finally {
            lock.unlock();
        }
    }
}
