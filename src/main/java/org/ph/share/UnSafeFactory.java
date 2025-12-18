package org.ph.share;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnSafeFactory {


    /**
     * 获取 unsafe
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取字段的偏移量
     * @param unsafe
     * @param clazz
     * @param fieldName
     * @return
     */

    public static long getFieldOffset(Unsafe unsafe, Class clazz, String fieldName) {
        try  {
            return unsafe.objectFieldOffset(clazz.getDeclaredField(fieldName));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
