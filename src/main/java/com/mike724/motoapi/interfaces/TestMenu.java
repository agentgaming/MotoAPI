package com.mike724.motoapi.interfaces;

public class TestMenu {

    @InterfaceOption(slot = 0, name="Option 1", description = "This is option 1!", itemId = 1)
    private static void Option1(InterfaceClick ic) {
        ic.getPlayer().sendMessage("Option 1 Clicked!");
    }

    @InterfaceOption(slot = 14, name="Option 2", description = "This is option 2!", itemId = 5, toggleable = true)
    private static void Option2(InterfaceClick ic) {
        ic.getPlayer().sendMessage(ic.isEnabled() ? "Option 2 Enabled!" : "Option 2 Disabled!");
    }
}
