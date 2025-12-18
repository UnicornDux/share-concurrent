package org.ph.share.juc.cas;

import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.atomic.AtomicIntegerArray;

@Slf4j
public class AtomicArray {

    static int[] value = new int[] { 1, 2, 3, 4, 5};
    static AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(value);

    public static void main(String[] args) {
        // 设置索引位置 0 的 值 100
        atomicIntegerArray.set(0 ,100);
        log.info(" atom array value: {}", atomicIntegerArray.get(0));

        // 以原子更新的方式将索引位置为 1 的值与传入的数值相加
        atomicIntegerArray.getAndAdd(1, 12);
        log.info("array: {}", atomicIntegerArray);
    }
}