package me.pzdrs.bingo.tasks;

import me.pzdrs.bingo.Bingo;
import me.pzdrs.bingo.listeners.events.GameEndEvent;
import me.pzdrs.bingo.listeners.events.GameStartEvent;
import me.pzdrs.bingo.utils.Message;
import me.pzdrs.bingo.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;

public class LobbyLoop extends BukkitRunnable {
    private Bingo plugin;
    private int timer;

    public LobbyLoop() {
        this.plugin = Bingo.getInstance();
        this.timer = plugin.getConfig().getInt("countdown.lobby");
    }

    @Override
    public void run() {
        if (timer == 0) {
            Bukkit.getServer().getPluginManager().callEvent(new GameStartEvent());
            cancel();
            return;
        }
        Bukkit.getOnlinePlayers().forEach(online -> {
            online.sendMessage(Message.info("chat.lobbyCountdown").replace("$timer", String.valueOf(timer)));
            online.playSound(online.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1f, 1f);
        });
        plugin.getPlayers().forEach(((uuid, bingoPlayer) -> {
            if (Utils.isEnoughPlayers()) {
                bingoPlayer.getScoreboard().updateLine(1, Utils.color(" &9>>&a " + timer + "s"));
            } else {
                bingoPlayer.getScoreboard().updateLine(1, Utils.color(" &9>>&7 Waiting for players..."));
            }
        }));
        timer--;
    }

    @Override
    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        plugin.getPlayers().forEach((uuid, bingoPlayer) -> {
            if (!Utils.isEnoughPlayers())
                bingoPlayer.getScoreboard().updateLine(1, Utils.color(" &9>>&7 Waiting for players..."));
        });
    }
}
