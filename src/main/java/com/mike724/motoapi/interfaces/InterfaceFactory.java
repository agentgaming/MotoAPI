package com.mike724.motoapi.interfaces;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class InterfaceFactory implements Listener {
    private JavaPlugin plugin;
    private String interfaceName;
    private EventPriority eventPriority;

    private int inventorySize;
    private Inventory inventory;

    private ArrayList<Integer> enabledOptions = new ArrayList<>();

    private HashMap<Integer, InterfaceOption> options;
    private HashMap<Integer, Method> methods;

    private HashMap<Integer, DynamicInterfaceOption> dynamicOptions;
    private HashMap<Integer, DynamicInterfaceRunnable> dynamicRunnables;

    private Boolean dynamic;

    public InterfaceFactory(JavaPlugin plugin, Class interfaceClass, String interfaceName) {
        this(plugin, interfaceClass, interfaceName, EventPriority.NORMAL);
    }

    public InterfaceFactory(JavaPlugin plugin, String interfaceName, HashMap<Integer, DynamicInterfaceOption> options, HashMap<Integer, DynamicInterfaceRunnable> runnables) {
        this(plugin, interfaceName, options, runnables, EventPriority.NORMAL);
    }

    public InterfaceFactory(JavaPlugin plugin, String interfaceName, HashMap<Integer, DynamicInterfaceOption> options, HashMap<Integer, DynamicInterfaceRunnable> runnables, EventPriority eventPriority) {
        this.dynamic = true;

        this.plugin = plugin;
        this.interfaceName = interfaceName;
        this.eventPriority = eventPriority;

        this.dynamicOptions = options;
        this.dynamicRunnables = runnables;

        int maxSlot = 0;

        for (Integer key : options.keySet())
            if (key > maxSlot) maxSlot = key;

        inventorySize = (int) (Math.ceil(maxSlot / 9.0) * 9.0);

        refreshInterface();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public InterfaceFactory(JavaPlugin plugin, Class interfaceClass, String interfaceName, EventPriority eventPriority) {
        this.dynamic = false;

        this.plugin = plugin;
        this.interfaceName = interfaceName;
        this.eventPriority = eventPriority;

        options = new HashMap<>();
        methods = new HashMap<>();

        int maxSlot = 0;

        for (Method m : interfaceClass.getDeclaredMethods()) {
            InterfaceOption io = m.getAnnotation(InterfaceOption.class);
            if (Modifier.isStatic(m.getModifiers()) && m.getParameterTypes()[0].equals(InterfaceClick.class) && io != null) {
                m.setAccessible(true);
                if (io.slot() > maxSlot) maxSlot = io.slot();
                options.put(io.slot(), io);
                methods.put(io.slot(), m);
            }
        }

        inventorySize = (int) (Math.ceil(maxSlot / 9.0) * 9.0);

        refreshInterface();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void refreshInterface() {
        Inventory inv = this.plugin.getServer().createInventory(null, this.inventorySize, this.interfaceName);

        for (Integer i : dynamic ? dynamicOptions.keySet() : options.keySet()) {
            if (dynamic) {
                DynamicInterfaceOption io = dynamicOptions.get(i);
                ItemStack item = new ItemStack(io.getItemId(), 1, io.getItemData());

                ItemMeta im = item.getItemMeta();
                im.setDisplayName(io.getName());

                ArrayList<String> lore = new ArrayList<>();
                if (io.isToggleable())
                    lore.add(enabledOptions.contains(io.getSlot()) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled");
                lore.add(ChatColor.GRAY + io.getDescription());
                im.setLore(lore);

                item.setItemMeta(im);

                inv.setItem(i, item);
            } else {
                InterfaceOption io = options.get(i);
                ItemStack item = new ItemStack(io.itemId(), 1, io.itemData());

                ItemMeta im = item.getItemMeta();
                im.setDisplayName(io.name());

                ArrayList<String> lore = new ArrayList<>();
                if (io.toggleable())
                    lore.add(enabledOptions.contains(io.slot()) ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled");
                lore.add(ChatColor.GRAY + io.description());
                im.setLore(lore);

                item.setItemMeta(im);

                inv.setItem(i, item);
            }
        }

        inventory = inv;
    }

    public Inventory getInventory() {
        return inventory;
    }

    private void setEnabled(ItemStack item, Boolean e) {
        ItemMeta im = item.getItemMeta();
        ArrayList<String> lore = (ArrayList) im.getLore();
        lore.set(0, e ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled");
        im.setLore(lore);
        item.setItemMeta(im);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void monitorPriority(InventoryClickEvent e) {
        if (eventPriority == EventPriority.MONITOR) {
            handleClick(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void highestPriority(InventoryClickEvent e) {
        if (eventPriority == EventPriority.HIGHEST) {
            handleClick(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void highPriority(InventoryClickEvent e) {
        if (eventPriority == EventPriority.HIGH) {
            handleClick(e);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void normalPriority(InventoryClickEvent e) {
        if (eventPriority == EventPriority.NORMAL) {
            handleClick(e);
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void lowPriority(InventoryClickEvent e) {
        if (eventPriority == EventPriority.LOW) {
            handleClick(e);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void lowestPriority(InventoryClickEvent e) {
        if (eventPriority == EventPriority.LOWEST) {
            handleClick(e);
        }
    }

    public void handleClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        Player p = (Player) e.getWhoClicked();

        if (this.getInventory().getViewers().contains(p)) {
            if (dynamic) {
                if (dynamicOptions.containsKey(e.getRawSlot()) && dynamicRunnables.containsKey(e.getRawSlot())) {
                    boolean enabled = false;
                    if (dynamicOptions.get(e.getRawSlot()).isToggleable()) {
                        if (enabledOptions.contains(e.getRawSlot())) {
                            enabled = false;
                            enabledOptions.remove(new Integer(e.getRawSlot()));
                            setEnabled(e.getCurrentItem(), false);
                        } else {
                            enabled = true;
                            enabledOptions.add(e.getRawSlot());
                            setEnabled(e.getCurrentItem(), true);
                        }
                    }

                    //TODO: find a way to pass inventory click
                    InterfaceClick ic = new InterfaceClick(p, enabled);
                    DynamicInterfaceRunnable r = dynamicRunnables.get(e.getRawSlot());
                    r.run(ic);
                }
                e.setCancelled(true);
            } else {
                if (options.containsKey(e.getRawSlot()) && methods.containsKey(e.getRawSlot())) {
                    boolean enabled = false;
                    if (options.get(e.getRawSlot()).toggleable()) {
                        if (enabledOptions.contains(e.getRawSlot())) {
                            enabled = false;
                            enabledOptions.remove(new Integer(e.getRawSlot()));
                            setEnabled(e.getCurrentItem(), false);
                        } else {
                            enabled = true;
                            enabledOptions.add(e.getRawSlot());
                            setEnabled(e.getCurrentItem(), true);
                        }
                    }

                    InterfaceClick ic = new InterfaceClick(p, enabled);
                    Method m = methods.get(e.getRawSlot());
                    try {
                        m.invoke(null, new Object[]{ic});
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                e.setCancelled(true);
            }
        }
    }
}
