package com.mike724.motoapi.interfaces;

public class DynamicInterfaceOption {
    public int slot;
    public String name;
    public String description;
    public int itemId;
    public short itemData;
    public boolean toggleable;

    public DynamicInterfaceOption(Integer slot, String name, String description, Integer itemId) {
        this(slot, name, description, itemId, (short) 0);
    }

    public DynamicInterfaceOption(Integer slot, String name, String description, Integer itemId, short itemData) {
        this(slot, name, description, itemId, itemData, false);
    }

    public DynamicInterfaceOption(Integer slot, String name, String description, Integer itemId, short itemData, boolean toggleable) {
        this.slot = slot;
        this.name = name;
        this.description = description;
        this.itemId = itemId;
        this.itemData = itemData;
        this.toggleable = toggleable;
    }

    public int getSlot() {
        return slot;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getItemId() {
        return itemId;
    }

    public short getItemData() {
        return itemData;
    }

    public boolean isToggleable() {
        return toggleable;
    }
}
