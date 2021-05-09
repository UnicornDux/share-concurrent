package org.ph.share._06_threadPool_gettingStarted;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class _01_ThreadPool {
    public static void main(String[] args) {

        Executors.newSingleThreadExecutor();

        Executors.newCachedThreadPool();

        ExecutorService executorService = Executors.newFixedThreadPool(3);
//        executorService.

    }
}
