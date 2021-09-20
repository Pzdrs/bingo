package me.pzdrs.bingo.utils;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.Mode;
import me.pzdrs.bingo.fastBoard.FastBoard;
import me.pzdrs.bingo.managers.GameManager;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.Arrays;
import java.util.Random;

public class Utils {
    private static Bingo plugin = Bingo.getInstance();
    private static GameManager gameManager = plugin.getGameManager();

    public static void delete(File file) {
        if (file.isDirectory()) { // If its a folder, delete its contents
            for (File f : file.listFiles())
                delete(f);
        }
        file.delete(); // Delete the actual file
    }

    public static Material[] generateCardItems(int numberOfItems) {
        Material[] items = new Material[numberOfItems];
        switch (plugin.getConfig().getString("itemMode")) {
            case "whitelist":
                for (int i = 0; i < items.length; i++) {
                    Material material;
                    do {
                        material = Material.values()[new Random().nextInt(Material.values().length)];
                    } while (!plugin.getConfig().getStringList("whitelist").contains(material.toString()) ||
                            Arrays.asList(items).contains(material) || material.equals(Material.LIME_STAINED_GLASS_PANE) || !material.isItem());
                    items[i] = material;
                }
                break;
            case "blacklist":
                for (int i = 0; i < items.length; i++) {
                    Material material;
                    do {
                        material = Material.values()[new Random().nextInt(Material.values().length)];
                    } while (plugin.getConfig().getStringList("blacklist").contains(material.toString()) ||
                            Arrays.asList(items).contains(material) || material.equals(Material.LIME_STAINED_GLASS_PANE) || !material.isItem());
                    items[i] = material;
                }
                break;
        }
        return items;
    }

    public static boolean isEnoughPlayers() {
        return plugin.getPlayers().size() >= plugin.getConfig().getDouble("playersNeeded") * Bukkit.getMaxPlayers();
    }

    public static String typeToFriendlyName(Material material) {
        return WordUtils.capitalize(material.toString().toLowerCase().replace("_", " "));
    }

    public static FastBoard getGameScoreboard(Player player) {
        FastBoard scoreboard = new FastBoard(player);
        scoreboard.updateTitle(Utils.color(plugin.getLang().getString("scoreboard.header")));
        scoreboard.updateLines(
                Utils.color("&7Time left"),
                Utils.color(" &9>>&a "),
                "",
                Utils.color("&7Players"),
                Utils.color(" &9>>&a " + plugin.getServer().getOnlinePlayers().size() + "/" + plugin.getServer().getMaxPlayers()),
                "",
                Utils.color("&7Mode"),
                Utils.color(" &9>>&a " + Mode.toString(gameManager.getMode())),
                "",
                Utils.color("&7Your score"),
                Utils.color(" &9>>&a 0"),
                "",
                getFooter()
        );
        return scoreboard;
    }

    public static FastBoard getLobbyScoreboard(Player player) {
        FastBoard scoreboard = new FastBoard(player);
        scoreboard.updateTitle(Utils.color(plugin.getLang().getString("scoreboard.header")));
        scoreboard.updateLines(
                Utils.color("&7Starting in"),
                Utils.color(" &9>>&a "),
                "",
                Utils.color("&7Players"),
                Utils.color(" &9>>&a " + plugin.getServer().getOnlinePlayers().size() + "/" + plugin.getServer().getMaxPlayers()),
                "",
                Utils.color("&7Mode"),
                Utils.color(" &9>>&a " + Mode.toString(gameManager.getMode())),
                "",
                getFooter()
        );
        return scoreboard;
    }

    private static String getFooter() {
        if (plugin.getLang().getString("scoreboard.footer") != null)
            return color(plugin.getLang().getString("scoreboard.footer"));
        return color("&eby " + plugin.getDescription().getAuthors().get(0) + " &7v" + plugin.getDescription().getVersion());
    }

    public static String color(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
