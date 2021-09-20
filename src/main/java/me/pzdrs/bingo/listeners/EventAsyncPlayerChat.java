package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class EventAsyncPlayerChat implements Listener {
    private Bingo plugin;

    public EventAsyncPlayerChat(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!plugin.getPlayers().containsKey(event.getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(Message.error("chat.spectatorChat"));
        }
    }
}
