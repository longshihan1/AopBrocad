package com.longshihan.broca_annotation.cache;

/**
 * Created by LONGHE001.
 *
 * @time 2017/12/29 0029
 * @des
 * @function
 */

public @interface ServiceCache {
    int value() default -1;

    int expire() default 60;

    String key();

    String includeKeys();

    boolean sync() default false;

    boolean nullPattern() default true;

}
