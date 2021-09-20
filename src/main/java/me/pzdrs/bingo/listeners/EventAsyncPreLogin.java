package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class EventAsyncPreLogin implements Listener {
    private Bingo plugin;
    private GameManager gameManager;

    public EventAsyncPreLogin(Bingo plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onAsyncPreLogin(AsyncPlayerPreLoginEvent event) {
        if (!plugin.getConfig().getBoolean("allowSpectators"))
            if (gameManager.isGameInProgress())
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "The game has already started.");
    }
}
