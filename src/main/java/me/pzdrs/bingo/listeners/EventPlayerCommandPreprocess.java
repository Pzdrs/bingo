package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EventPlayerCommandPreprocess implements Listener {
    private Bingo plugin;

    public EventPlayerCommandPreprocess(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getGameManager().isGameInProgress() && !player.hasPermission("bingo.lobby.commands")) {
            event.setCancelled(true);
        }
        if (plugin.getSpectators().contains(player.getUniqueId())) {
            event.setCancelled(true);
            player.sendMessage(Message.error("chat.spectatorChat"));
        }
    }
}
