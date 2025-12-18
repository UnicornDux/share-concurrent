package org.ph.share.juc.cas;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class AtomIntegerApi {
    public static void main(String[] args) {

        AtomicInteger atomicInteger = new AtomicInteger(1);
        atomicInteger.compareAndSet(1, 20);
        atomicInteger.compareAndSet(2, 10);

        log.info("success value : {}", atomicInteger.get());

        // 先获取然后自增，类似 i++
        log.info("getAndIncrement: {} ", atomicInteger.getAndIncrement());
        // 先自增然后获取, 类似 ++i
        log.info("incrementAndGet: {} " , atomicInteger.incrementAndGet());
        // 先获取然后自减，类似 i--
        log.info("getAndDecrement: {} ", atomicInteger.getAndDecrement());
        // 先自减然后获取，类似 --i
        log.info("decrementAndGet: {} ", atomicInteger.decrementAndGet());
        // 获取并加值
        log.info("getAndAdd: {} ", atomicInteger.getAndAdd(-10));
        // 加值并获取
        log.info("addAndGet: {} ", atomicInteger.addAndGet(10));
        // 获取并更新 (其中函数可以保证擦欧总的原子性，但需要无副作用)
        log.info("getAndUpdate: {}", atomicInteger.getAndUpdate(p -> p - 2));
        // 更新并获取 (其中函数可以保证擦欧总的原子性，但需要无副作用)
        log.info("updateAndGet: {}", atomicInteger.updateAndGet(p -> p + 2));

        // 获取并计算 (保证函数内操作的原子性)
        // getAndUpdate 如果在 lambda 中引入了局部变量，需要保证该局部变量是 final 的
        // getAndAccumulate 可以通过参数 1 传入外部的局部比那辆，但因为其不在 lambda 中因此不必是 final
        log.info("getAndAccumulate: {}", atomicInteger.getAndAccumulate(10, (p, x) -> p + x));
        //
        log.info("accumulateAndGet: {}", atomicInteger.accumulateAndGet(-5, (p, x) -> p + x));
    }
}
