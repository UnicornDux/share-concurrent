package org.ph.share.juc.event;

import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderEventProducer {

    private RingBuffer<OrderEvent> ringBuffer;

    public OrderEventProducer(RingBuffer<OrderEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(long value, String name){

        // 获取下一个可用的 slot
        long sequence = ringBuffer.next();
        try {
            // 获取消息载体 (事件)
            OrderEvent event = ringBuffer.get(sequence);
            // 写入消息
            event.setValue(value);
            event.setName(name);
        } finally {
            log.info(
                "身产者: {} 发送数据 value: {} - name: {} ",
                Thread.currentThread().getName(),
                value,
                name
            );
            // 发布事件
            ringBuffer.publish(sequence);
        }
    }
}
