package com.util;

import com.google.inject.Module;

import java.lang.annotation.*;


@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited

public @interface Modules {
    Class<? extends Module>[] value();
}
