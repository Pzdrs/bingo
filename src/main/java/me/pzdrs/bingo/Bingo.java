package me.pzdrs.bingo;


import me.pzdrs.bingo.commands.CommandBingo;
import me.pzdrs.bingo.commands.CommandTop;
import me.pzdrs.bingo.listeners.*;
import me.pzdrs.bingo.listeners.events.EventBlockPlaceBreak;
import me.pzdrs.bingo.managers.BingoPlayer;
import me.pzdrs.bingo.managers.GameManager;
import me.pzdrs.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class Bingo extends JavaPlugin {
    private static Bingo instance;
    private CustomConfigurationFile lang;
    private GameManager gameManager;
    private HashMap<UUID, BingoPlayer> players;
    private List<UUID> spectators;

    @Override
    public void onEnable() {
        instance = this;

        // Init configs
        saveDefaultConfig();
        this.lang = new CustomConfigurationFile(this, "lang.yml", false);

        this.players = new HashMap<>();
        this.spectators = new ArrayList<>();
        this.gameManager = new GameManager(this);

        init();

        // Verify configuration for items
        if (getConfig().getString("itemMode") == null ||
                (!getConfig().getString("itemMode").equalsIgnoreCase("whitelist") &&
                        !getConfig().getString("itemMode").equalsIgnoreCase("blacklist"))) {
            disable();
            throw new NullPointerException("Item mode is null");
        }

        if (getConfig().getString("itemMode").equalsIgnoreCase("whitelist") && getConfig().getStringList("whitelist").size() < 25) {
            disable();
            throw new IllegalArgumentException("Item mode is set to Whitelist, but there are less than 25 items specified.");
        }

        // Generate new world to play in
        getServer().createWorld(new WorldCreator("Bingo"));
        // Set gamerules
        for (World world : getServer().getWorlds()) {
            if (getConfig().getBoolean("keepInventory")) {
                world.setGameRule(GameRule.KEEP_INVENTORY, true);
            } else {
                world.setGameRule(GameRule.KEEP_INVENTORY, false);
            }
        }
    }

    @Override
    public void onDisable() {
        // Delete the world that the game took place in so it can be generated again
        Bukkit.unloadWorld("Bingo", false);
        Utils.delete(new File("Bingo"));
        for (World world : getServer().getWorlds()) {
            Utils.delete(new File(world.getWorldFolder().toString() + "\\playerdata"));
        }
    }

    private void disable() {
        getPluginLoader().disablePlugin(this);
    }

    public HashMap<UUID, BingoPlayer> getPlayers() {
        return players;
    }

    public List<UUID> getSpectators() {
        return spectators;
    }

    public FileConfiguration getLang() {
        return lang.getConfig();
    }

    public CustomConfigurationFile getLangConfig() {
        return lang;
    }

    public BingoPlayer getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public static Bingo getInstance() {
        return instance;
    }

    private void init() {
        // Init commands
        new CommandBingo(this);
        new CommandTop(this);

        // Init listeners
        new EventPlayerJoinQuit(this);
        new EventPlayerCommandPreprocess(this);
        new EventPlayerInteractEntity(this);
        new EventFoodLevelChange(this);
        new EventPlayerDeathRespawn(this);
        new EventAsyncPlayerChat(this);
        new EventInventoryClick(this);
        new EventsItemAcquire(this);
        new EventAsyncPreLogin(this);
        new EventBlockPlaceBreak(this);
        new EventEntityDamageByEntity(this);
        new EventPlayerDropItem(this);
        new EventGameStartEnd(this);
        new EventPlayerInteract(this);
        new EventItemFound(this);
    }
}
