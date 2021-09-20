package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.persistence.PersistentDataType;

public class EventPlayerDeathRespawn implements Listener {
    private Bingo plugin;

    public EventPlayerDeathRespawn(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getKeepInventory()) return;
        event.getDrops().removeIf(item -> item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, "bingoCardOpener"), PersistentDataType.BYTE));
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (plugin.getGameManager().isGameInProgress()) {
            event.setRespawnLocation(Bukkit.getWorld("Bingo").getSpawnLocation());
            if (plugin.getConfig().getBoolean("giveBingoCardItem"))
                player.getInventory().addItem(new ItemBuilder(Material.PAPER)
                        .setDisplayName("&aYour Bingo card")
                        .addLoreLine("&7Right-click to open your Bingo card")
                        .setPersistentData(plugin, "bingoCardOpener", PersistentDataType.BYTE, (byte) 1)
                        .build());
        } else {
            event.setRespawnLocation(Bukkit.getWorld("world").getSpawnLocation());
        }
    }
}
