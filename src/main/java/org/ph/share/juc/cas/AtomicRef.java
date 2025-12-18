package org.ph.share.juc.cas;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 原子更新引用类型
 */
@Slf4j
public class AtomicRef {

    static void updateReference() {
        User u1 = new User(1, "Alex");
        User u2 = new User(2, "Bryant");
        User u3 = new User(3, "Cindy");

        // 初始化 user1
        AtomicReference<User> atomicReference = new AtomicReference<>();
        atomicReference.set(u1);
        // 将 u1 更新为 u2
        if (atomicReference.compareAndSet(u1, u2)) {
            log.info("更新成功: {}", atomicReference.get());
        }else {
            log.info("更新成功: {}", atomicReference.get());
        }
        // 更新为 u3
        if (atomicReference.compareAndSet(u1, u3)) {
            log.info("更新成功: {}", atomicReference.get());
        }else {
            log.info("更新失败: {}", atomicReference.get());
        }
    }

    public static void main(String[] args) throws InterruptedException {
        // updateReference();
        updateScore();
    }

    static class Candidate {
        // 字段必须使用 volatile 修饰
        volatile int score = 0;
    }

    private static final AtomicIntegerFieldUpdater<Candidate> scoreUpdate
         = AtomicIntegerFieldUpdater.newUpdater(Candidate.class, "score");
    
    static void updateScore() throws InterruptedException {
        Candidate candidate = new Candidate();
        Thread[] t = new Thread[10000];
        for (int i = 0; i < 10000; i++) {
            t[i] = new Thread(() -> {
                if (Math.random() > 0.4) {
                    scoreUpdate.incrementAndGet(candidate);
                }
            });
            t[i].start();
        }
        for (int i = 0; i < 10000; i++) {
            t[i].join();
        }
        log.info("AtomicIntegerUpdater: {}", candidate.score);
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class User {
    private Integer id;
    private String name;
}
