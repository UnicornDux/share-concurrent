package org.ph.share._02_CreateThread;

/**
 * 主要用于观测指令重排序的现象
 */

public class _08_ThreadSerial {
    static _08_ThreadSerial serial;
    static Boolean isInit = false;

    public static void main(String[] args) {
        for (int i = 0; i < 1000000; i++) {
            isInit = false;
            serial = null;

            Thread t1 = new Thread(() -> {
                // 多次执行，可能会在 线程 1 和 线程 2 之间出现交叉执行的时候
                // 并且语句 1 和 语句 2 产生了重排序，此时线程 2 会抛出 NPE
                serial  = new _08_ThreadSerial(); // 语句1
                isInit = true;  // 语句2
            });

            Thread t2 = new Thread(() -> {
                if (isInit) {
                    serial.doSomeThing();
                }
            });
            t1.start();
            t2.start();

            try {
                t1.join();
                t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void doSomeThing(){
        System.out.println("doSomeThing...");
    }
}
