package org.ph.share.juc.lock;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class ExchangeUsage {

    private static final Exchanger<String> exchanger = new Exchanger<>();
    static String goods = "电脑";
    static String price = "$2000";

    public static void main(String[] args) throws InterruptedException {

        log.info("准备交易，一手交钱，一手交货");

        // 模拟卖家
        new Thread(() -> {
            try {
                String money = exchanger.exchange(goods);
                log.info("卖家收到钱: {} ", money);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        Thread.sleep(1000);

        //
        new Thread(() -> {
           try {
               log.info("买家到了，已经准备好钱: {}", price);
               String goods = exchanger.exchange(price);
               log.info("买家收到货: {}", goods);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }).start();
    }
}


@Slf4j
class ExchangeCheck { // 两个线程对账
    private static final Exchanger<String> exchanger = new Exchanger<>();

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) {
        threadPool.execute( () -> {
           try {
               String A = "asdjaldsjadlkadjsaa";
               exchanger.exchange(A);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        });

        threadPool.execute(() -> {
            try {
                String B = "sxcasdaasdadsaadd";
                String A = exchanger.exchange(B);
                if (!A.equals(B)){
                    log.error("{}, {} 无法匹配", A, B);
                }else {
                    log.info("匹配一致");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        threadPool.shutdown();
    }
}

// 模拟交互队列
@Slf4j
class ChangeQueue {


    static ArrayBlockingQueue<String> emptyQueue = new ArrayBlockingQueue<>(5);
    static ArrayBlockingQueue<String> fullQueue = new ArrayBlockingQueue<>(5);
    static Exchanger<ArrayBlockingQueue<String>> exchanger = new Exchanger<>();


    public static void main(String[] args) {
        new Thread(new Prod()).start();
        new Thread(new Cons()).start();
    }

    static class Prod implements Runnable {
        @Override
        public void run() {
            ArrayBlockingQueue<String> current = emptyQueue;
            try {
                while(current != null) {
                   String uuid = UUID.randomUUID().toString();
                   try {
                       current.add(uuid);
                       log.info("producer 生产了一个序列: {}, >>>> 加入到交换区", uuid);
                       Thread.sleep(2000);
                   }catch(IllegalStateException ee) {
                       log.error("交换区已满");
                       current = exchanger.exchange(fullQueue);
                   }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class Cons implements Runnable {
        @Override
        public void run() {
            ArrayBlockingQueue<String> current = fullQueue;
            try {
                while (current != null) {
                    if (!current.isEmpty()) {
                        String str = current.poll();
                        log.info("consumer 消耗一个序列: {}", str);
                        Thread.sleep(1000);
                    }else {
                        log.info("队列空, 换一个满的");
                        current = exchanger.exchange(current);
                        log.info("更换成功....");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
