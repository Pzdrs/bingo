package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.guis.GuiBingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.time.LocalTime;

public class EventPlayerInteractEntity implements Listener {
    private Bingo plugin;

    public EventPlayerInteractEntity(Bingo plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();
        Player target = (Player) event.getRightClicked();
        if (plugin.getGameManager().isGameInProgress() && !plugin.getPlayers().containsKey(player.getUniqueId()) && event.getHand().equals(EquipmentSlot.HAND)) {
            player.sendMessage(Message.info("chat.spectatorCard")
                    .replace("$player", target.getDisplayName())
                    .replace("$time", LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond()));
            new GuiBingo(plugin, target).show(player);
        }
    }
}
