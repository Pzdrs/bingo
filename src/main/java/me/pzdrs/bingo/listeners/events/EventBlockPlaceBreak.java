package me.pzdrs.bingo.listeners.events;

import me.pzdrs.bingo.Bingo;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class EventBlockPlaceBreak implements Listener {
    private Bingo plugin;

    public EventBlockPlaceBreak(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!plugin.getGameManager().isGameInProgress()) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!plugin.getGameManager().isGameInProgress()) event.setCancelled(true);
    }
}
