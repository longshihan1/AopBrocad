package com.longshihan.broca_annotation.cache;

/**
 * Created by LONGHE001.
 *
 */

public @interface ServiceCache {
    int value() default -1;

    int expire() default 60;

    String key();

    String includeKeys();

    boolean sync() default false;

    boolean nullPattern() default true;

}
