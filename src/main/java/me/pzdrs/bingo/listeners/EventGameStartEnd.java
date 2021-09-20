package me.pzdrs.bingo.listeners;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.listeners.events.GameEndEvent;
import me.pzdrs.bingo.listeners.events.GameStartEvent;
import me.pzdrs.bingo.managers.GameManager;
import me.pzdrs.bingo.utils.ItemBuilder;
import me.pzdrs.bingo.utils.Message;
import me.pzdrs.bingo.utils.Utils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class EventGameStartEnd implements Listener {
    private Bingo plugin;
    private GameManager gameManager;

    public EventGameStartEnd(Bingo plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        // Initial world border
        WorldBorder border = Bukkit.getWorld("Bingo").getWorldBorder();
        border.setCenter(Bukkit.getWorld("Bingo").getSpawnLocation());
        border.setSize(5);
        gameManager.assignCards();
        gameManager.setGameInProgress(true);
        gameManager.startGameLoop();
        // Setup
        plugin.getPlayers().forEach((uuid, bingoPlayer) -> {
            // Teleport everyone in the newly generated world + cosmetics
            Player player = Bukkit.getPlayer(uuid);
            player.setInvulnerable(false);
            player.teleport(Bukkit.getWorld("Bingo").getSpawnLocation());
            // Setup scoreboards
            bingoPlayer.setScoreboard(Utils.getGameScoreboard(Bukkit.getPlayer(uuid)));
            bingoPlayer.getPlayer().getInventory().clear();
            // Give away cards as items
            if (plugin.getConfig().getBoolean("giveBingoCardItem"))
                bingoPlayer.getPlayer().getInventory().addItem(new ItemBuilder(Material.PAPER)
                        .setDisplayName("&aYour Bingo card")
                        .addLoreLine("&7Right-click to open your Bingo card")
                        .setPersistentData(plugin, "bingoCardOpener", PersistentDataType.BYTE, (byte) 1)
                        .build());
        });
        // Countdown and more world border stuff
        new BukkitRunnable() {
            int timer = 5;

            @Override
            public void run() {
                if (timer == 0) {
                    Bukkit.getWorld("Bingo").playSound(Bukkit.getWorld("Bingo").getSpawnLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1f, 1f);
                    border.setSize(plugin.getConfig().getInt("worldBorderSize"), 30);
                    border.setWarningDistance(0);
                    plugin.getPlayers().keySet().forEach(uuid -> {
                        Player player = Bukkit.getPlayer(uuid);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 60 * 20, 0));
                        player.sendMessage(Message.info("chat.invisibility"));
                    });
                    cancel();
                }
                for (UUID uuid : plugin.getPlayers().keySet()) {
                    Player player = Bukkit.getPlayer(uuid);
                    player.sendTitle(Utils.color("&9&lStarting in"), Utils.color("&l&a" + timer), 0, 20, 0);
                    player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
                }
                timer--;
            }
        }.runTaskTimer(plugin, 0, 20);
    }

    @EventHandler
    public void onGameEnd(GameEndEvent event) {
        gameManager.setGameInProgress(false);
        gameManager.getGameLoop().cancel();
        Player winner = event.getWinner();
        switch (event.getOutcome()) {
            case DEFAULT:
                plugin.getPlayers().forEach((uuid, bingoPlayer) -> {
                    // Handle everyone
                    if (winner.getUniqueId().equals(uuid)) {
                        // Handle winner
                        winner.playSound(winner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                        winner.spawnParticle(Particle.FIREWORKS_SPARK, winner.getLocation(), 150);
                        winner.sendTitle(Utils.color(plugin.getLang().getString("other.victoryTitle")), Utils.color(plugin.getLang().getString("other.victorySubtitle")), 20, 100, 20);
                    } else {
                        // Handle loser
                        Player loser = Bukkit.getPlayer(uuid);
                        loser.sendTitle(Utils.color(plugin.getLang().getString("other.gameOverTitle")), Utils.color(plugin.getLang().getString("other.gameOverSubtitle").replace("$winner", winner.getDisplayName())), 20, 100, 20);
                        loser.playSound(loser.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);

                    }
                });
                break;
            case TIMEOUT:
                plugin.getPlayers().forEach((uuid, bingoPlayer) -> {
                    Player player = Bukkit.getPlayer(uuid);
                    player.sendTitle(Utils.color(plugin.getLang().getString("other.gameOverTitle")), Utils.color(plugin.getLang().getString("other.gameOverSubtitle2")), 20, 100, 20);
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1f, 1f);
                });
                break;
            case NO_PLAYERS_LEFT:
                winner.playSound(winner.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                winner.spawnParticle(Particle.FIREWORKS_SPARK, winner.getLocation(), 150);
                winner.sendTitle(Utils.color(plugin.getLang().getString("other.victoryTitle")), Utils.color(plugin.getLang().getString("other.victorySubtitle2")), 20, 100, 20);
                break;
            case EVERYONE_LEFT:
                serverRestart(0);
                return;
        }
        // Handle all players - UI
        plugin.getPlayers().forEach((uuid, bingoPlayer) -> {
            Player player = Bukkit.getPlayer(uuid);
            player.closeInventory();
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20d);
            player.setFoodLevel(20);
            if (!bingoPlayer.getScoreboard().isDeleted())
                bingoPlayer.getScoreboard().delete();
        });
        serverRestart(200);
    }

    private void serverRestart(int delay) {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            plugin.getServer().getOnlinePlayers().forEach(o -> o.kickPlayer("This server is restarting!"));
            plugin.getServer().reload();
        }, delay);
    }
}
