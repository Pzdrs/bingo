package me.pzdrs.bingo.commands;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CommandTop implements TabExecutor {
    private Bingo plugin;

    public CommandTop(Bingo plugin) {
        this.plugin = plugin;
        plugin.getCommand("top").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.getLogger().severe("Only players may use this command!");
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission("bingo.top")) {
            player.sendMessage(Message.noPerms("bingo.top"));
            return true;
        }
        if (!plugin.getConfig().getBoolean("allowTop")) {
            player.sendMessage(Message.error("chat.topDisabled"));
            return true;
        }
        Location location = player.getLocation();
        location.setY(player.getWorld().getHighestBlockAt(player.getLocation().getBlockX(), player.getLocation().getBlockZ()).getLocation().getY() + 1);
        player.teleport(location);
        player.sendMessage(Message.success("chat.top"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return new ArrayList<>();
    }
}
