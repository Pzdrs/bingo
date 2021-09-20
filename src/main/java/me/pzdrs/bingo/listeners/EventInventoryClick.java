package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.guis.GUI;
import me.pzdrs.bingo.listeners.events.ItemFoundEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class EventInventoryClick implements Listener {
    private Bingo plugin;

    public EventInventoryClick(Bingo plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        InventoryHolder holder = event.getInventory().getHolder();

        if (event.getClickedInventory() == null || itemStack == null)
            return;
        if (holder instanceof GUI) {
            event.setCancelled(true);
            GUI gui = (GUI) event.getInventory().getHolder();
            gui.handle(event);
        } else {
            Bukkit.getServer().getPluginManager().callEvent(new ItemFoundEvent(itemStack.getType(), player));
        }
    }

}
