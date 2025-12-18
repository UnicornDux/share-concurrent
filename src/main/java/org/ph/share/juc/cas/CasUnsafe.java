package org.ph.share.juc.cas;

import org.ph.share.UnSafeFactory;
import org.ph.share.model.Entity;
import sun.misc.Unsafe;


public class CasUnsafe {

    public static void main(String[] args) {
        // modify object field value
        Entity entity = new Entity();
        Unsafe unsafe = UnSafeFactory.getUnsafe();

        long offset = UnSafeFactory.getFieldOffset(unsafe, Entity.class, "value");

        boolean ok = unsafe.compareAndSwapInt(entity, offset, 0, 3);
        System.out.println("ok: " + ok + "\t" + entity.value);

        ok = unsafe.compareAndSwapInt(entity, offset, 3, 5);
        System.out.println("ok: " + ok + "\t" + entity.value);

        ok = unsafe.compareAndSwapInt(entity, offset, 3, 10);
        System.out.println("ok: " + ok + "\t" + entity.value);
    }
}
