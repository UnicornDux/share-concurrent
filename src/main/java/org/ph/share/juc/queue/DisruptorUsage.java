package org.ph.share.juc.queue;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import org.ph.share.juc.event.OrderEvent;
import org.ph.share.juc.event.OrderEventHandler;
import org.ph.share.juc.event.OrderEventProducer;

import java.util.concurrent.Executors;

public class DisruptorUsage {

    public static void main(String[] args) {
        Disruptor<OrderEvent> disruptor = new Disruptor<>(
                OrderEvent::new,
                1024 * 1024,
                Executors.defaultThreadFactory(),
                ProducerType.SINGLE, // 单生产者
                new YieldingWaitStrategy() // 等待策略
        );

        // 1.设置消费者用于消费 ringBuffer 的事件
        // disruptor.handleEventsWith(new OrderEventHandler());
        // 2. 设置多个消费者
        // disruptor.handleEventsWith(new OrderEventHandler(),new OrderEventHandler());

        // 3. 设置多个消费者，消费者要实现 WorkHandler 接口，一条消息只会被一个消费者消费
        disruptor.handleEventsWithWorkerPool(new OrderEventHandler(), new OrderEventHandler());

        // 启动 disruptor
        disruptor.start();

        // 创建 ringBuffer 容器
        RingBuffer<OrderEvent> ringBuffer = disruptor.getRingBuffer();

        new Thread(() -> {
            // 创建生产者
            OrderEventProducer producer = new OrderEventProducer(ringBuffer);

            for (int i = 0; i < 100; i++) {
                producer.onData(i, "Fox-" + i);
            }
        }, "producer1").start();

        new Thread(() -> {
            // 创建生产者
            OrderEventProducer producer = new OrderEventProducer(ringBuffer);

            for (int i = 0; i < 100; i++) {
                producer.onData(i, "monkey-" + i);
            }
        }, "producer2").start();

       //  disruptor.shutdown();
    }
}
