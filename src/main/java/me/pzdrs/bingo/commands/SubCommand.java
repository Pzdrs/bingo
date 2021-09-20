package me.pzdrs.bingo.commands;

import org.bukkit.entity.Player;

public abstract class SubCommand {

    public abstract String getName();

    public abstract String getDescription();

    public abstract String getUsage();

    public abstract String getPermission();

    public abstract String[] getAliases();

    public abstract void handle(Player player, String args[]);
}
