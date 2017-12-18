package com.longshihan.broca_api;

/**
 * 反射的数据接口
 */
public interface ViewInject<T> {
    void inject(T t, Object source);
}
