package com.mike724.motoapi.interfaces;

public @interface InterfaceOption {
    public int slot();
    public String name();
    public String description();
    public int itemId();
    public byte itemData();
    public boolean toggleable();
}
