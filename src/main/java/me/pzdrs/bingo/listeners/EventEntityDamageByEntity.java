package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EventEntityDamageByEntity implements Listener {
    private Bingo plugin;

    public EventEntityDamageByEntity(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!plugin.getGameManager().isGameInProgress()) {
            event.setCancelled(true);
            return;
        }
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player)
            if (!plugin.getConfig().getBoolean("pvp")) {
                event.setCancelled(true);
                event.getDamager().sendMessage(Message.info("chat.noPvp"));
            }
    }
}
