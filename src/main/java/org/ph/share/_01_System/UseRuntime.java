package org.ph.share._01_System;


/**
 * 系统中最佳的线程数的设置的计算公式
 * 
 *  最佳线程数 = CPU 可用的线程数 / （1  - 阻塞系统）
 *
 *  > 阻塞系数这个参数主要是由 系统 IO 耗时占用 CPU 总时间的比值
 *
 */
public class UseRuntime {
  
  public static void main(String[] args) {
    // java 获取当前的系统可以运行的核心数量
    // 这个方法在 java8 之前具有 bug, 无法识别
    System.out.println(Runtime.getRuntime().availableProcessors());
    // jvm 中当前空闲内存
    System.out.println(Runtime.getRuntime().freeMemory());
    // jvm 实例总共用了多少内存
    System.out.println(Runtime.getRuntime().totalMemory());
    // jvm 最大可以用系统多少内存
    System.out.println(Runtime.getRuntime().maxMemory());

    // 加载系统中指定库名的动态库，包含本地代码的文件是从本地文件系统从通常获取库文件(可以看作某个环境变量配置)
    // 的地方加载的，此过程的细节取决于系统平台的实现，从库名到文件名的映射是以系统特定的方式完成的.
    System.out.println("Load Library....");
    Runtime.getRuntime().loadLibrary("crypt32");
    System.out.println("Library loaded.");
  }
}

