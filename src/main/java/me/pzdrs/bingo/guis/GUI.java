package me.pzdrs.bingo.guis;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import me.pzdrs.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class GUI implements InventoryHolder {
    protected Bingo plugin;
    protected Inventory inventory;
    protected Player player;

    public GUI(Bingo plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public abstract String getTitle();

    public abstract int getSize();

    public abstract void setContents();

    public abstract void handle(InventoryClickEvent event);

    public void reload() {
        inventory.clear();
        setContents();
    }

    /**
     * Used when you want to display the card to the owner
     */
    public void show() {
        if (!plugin.getPlayers().containsKey(player.getUniqueId())) {
            player.sendMessage(Message.error("chat.noCard"));
            return;
        }
        if (!plugin.getGameManager().isGameInProgress()) {
            player.sendMessage(Message.info("chat.gameNotStarted"));
            return;
        }
        inventory = Bukkit.createInventory(this, getSize(), getTitle());
        setContents();
        player.openInventory(inventory);
    }

    /**
     * Used to display the card to a different player
     *
     * @param showTo The player to display the card to
     */
    public void show(Player showTo) {
        if (!plugin.getPlayers().containsKey(player.getUniqueId())) {
            showTo.sendMessage(Message.error("chat.noCard"));
            return;
        }
        if (!plugin.getGameManager().isGameInProgress()) {
            showTo.sendMessage(Message.info("chat.gameNotStarted"));
            return;
        }
        inventory = Bukkit.createInventory(this, getSize(), Utils.color(plugin.getLang().getString("other.playerCard")
                .replace("$player", player.getDisplayName())
                .replace("$time", LocalTime.now().getHour() + ":" + LocalTime.now().getMinute() + ":" + LocalTime.now().getSecond())))
        ;
        setContents();
        showTo.openInventory(inventory);
    }
}
