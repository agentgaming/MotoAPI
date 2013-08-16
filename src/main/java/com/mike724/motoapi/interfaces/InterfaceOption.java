package com.mike724.motoapi.interfaces;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface InterfaceOption {
    public int slot();

    public String name();

    public String description();

    public int itemId();

    public short itemData() default 0;

    public boolean toggleable() default false;
}
