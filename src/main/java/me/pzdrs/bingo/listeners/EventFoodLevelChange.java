package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class EventFoodLevelChange implements Listener {
    private Bingo plugin;

    public EventFoodLevelChange(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (!plugin.getGameManager().isGameInProgress() || plugin.getConfig().getBoolean("disableFood"))
            event.setCancelled(true);
    }
}
