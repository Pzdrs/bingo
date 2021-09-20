package me.pzdrs.bingo.managers;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.Mode;
import me.pzdrs.bingo.tasks.GameLoop;
import me.pzdrs.bingo.tasks.LobbyLoop;
import me.pzdrs.bingo.utils.Utils;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {
    private static GameManager instance;
    private Bingo plugin;
    private boolean gameInProgress;
    private Material[] cardItems;
    private Mode mode;
    private BukkitTask gameLoop, lobbyLoop;

    public GameManager(Bingo plugin) {
        instance = this;
        this.plugin = plugin;
        this.gameInProgress = false;
        this.cardItems = new Material[25];
        this.mode = plugin.getConfig().getString("mode").equalsIgnoreCase("normal") ? Mode.NORMAL : Mode.FULL_HOUSE;
    }

    public void setGameInProgress(boolean b) {
        gameInProgress = b;
    }

    public static GameManager getInstance() {
        return instance;
    }

    public boolean isGameInProgress() {
        return gameInProgress;
    }

    public Mode getMode() {
        return mode;
    }

    public BukkitTask getGameLoop() {
        return gameLoop;
    }

    public BukkitTask getLobbyLoop() {
        return lobbyLoop;
    }

    public void assignCards() {
        this.cardItems = Utils.generateCardItems(cardItems.length);
        // Assign the cards to players
        plugin.getPlayers().forEach((uuid, bingoPlayer) -> bingoPlayer.setCard(new BingoCard(cardItems)));
    }

    public void startGameLoop() {
        this.gameLoop = new GameLoop().runTaskTimer(plugin, 0, 20);
    }

    public void startLobbyCountdown() {
        this.lobbyLoop = new LobbyLoop().runTaskTimer(plugin, 0, 20);
    }

    public int getTimer() {
        return 0;
    }
}
