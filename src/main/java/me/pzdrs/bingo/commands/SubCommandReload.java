package me.pzdrs.bingo.commands;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.utils.Message;
import org.bukkit.entity.Player;

public class SubCommandReload extends SubCommand {
    private Bingo plugin;

    SubCommandReload(Bingo plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reload all configurations";
    }

    @Override
    public String getUsage() {
        return "/bingo reload";
    }

    @Override
    public String getPermission() {
        return "bingo.reload";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"rl"};
    }

    @Override
    public void handle(Player player, String[] args) {
       plugin.reloadConfig();
       plugin.getLangConfig().reload();
       player.sendMessage(Message.success("chat.reload"));
    }
}
