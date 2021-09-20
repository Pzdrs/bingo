package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.persistence.PersistentDataType;

public class EventPlayerDropItem implements Listener {
    private Bingo plugin;

    public EventPlayerDropItem(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "bingoCardOpener"), PersistentDataType.BYTE)) {
            event.getPlayer().sendMessage(Message.error("chat.cantDropCard"));
            event.setCancelled(true);
        }
    }
}
