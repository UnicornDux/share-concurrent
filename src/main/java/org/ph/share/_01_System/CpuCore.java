package org.ph.share._01_System;


/**
 * 系统中最佳的线程数的设置的计算公式
 * 
 *  最佳线程数 = CPU 可用的线程数 / （1  - 阻塞系统）
 *
 *  > 阻塞系数这个参数主要是由 系统 IO 耗时占用 CPU 总时间的比值
 *
 */
public class CpuCore {
  
  public static void main(String[] args) {
    // java 获取当前的系统可以运行的核心数量
    // 这个方法在 java8 之前具有 bug, 无法识别
    System.out.println(Runtime.getRuntime().availableProcessors());
  }
}

