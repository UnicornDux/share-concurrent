package org.ph.share.juc.event;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderEventHandler implements EventHandler<OrderEvent>, WorkHandler<OrderEvent> {

    //
    // 实现的是 EventHandler 接口， 一个消息可被多个消费者消费(每个消费者都会收到)
    //
    @Override
    public void onEvent(OrderEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 处理消费逻辑
        log.info("EventHandler 消费者: {}, 获取到数据value: {} - name: {}",
                Thread.currentThread().getName(),
                event.getValue(),
                event.getName()
        );

    }

    //
    // 实现的是 workHandler 接口， 一个消息被一个消费者消费
    //
    @Override
    public void onEvent(OrderEvent event) throws Exception {
        log.info("WorkerHandler消费者: {}, 获取到数据value: {} - name: {}",
        Thread.currentThread().getName(),
            event.getValue(),
            event.getName()

        );
    }
}
