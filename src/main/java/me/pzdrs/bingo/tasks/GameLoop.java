package me.pzdrs.bingo.tasks;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.listeners.events.GameEndEvent;
import me.pzdrs.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameLoop extends BukkitRunnable {
    private Bingo plugin;
    private int timer;

    public GameLoop() {
        this.plugin = Bingo.getInstance();
        this.timer = (int) ((plugin.getConfig().getString("mode").equals("normal") ? plugin.getConfig().getDouble("countdown.normal") : plugin.getConfig().getDouble("countdown.full-house")) * 60);
    }

    @Override
    public void run() {
        if (timer == 0) cancel();
        plugin.getPlayers().forEach((uuid, bingoPlayer) -> {
            if (timer <= 60) {
                bingoPlayer.getScoreboard().updateLine(1, Utils.color(" &9>>&a " + timer + "s"));
            } else {
                bingoPlayer.getScoreboard().updateLine(1, Utils.color(" &9>>&a " + timer / 60 + "m"));
            }
        });
        timer--;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Bukkit.getServer().getPluginManager().callEvent(new GameEndEvent(GameEndEvent.Outcome.TIMEOUT, null));
    }
}
