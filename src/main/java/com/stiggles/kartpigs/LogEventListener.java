package com.stiggles.kartpigs;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LogEventListener implements Listener {
    public GameManager gameManager;

    public LogEventListener () {
        gameManager = KartPigs.getGameManager();
    }
    @EventHandler
    public void onPlayerJoin (PlayerJoinEvent e) {
        Player player = e.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.setInvulnerable(true);
        player.setInvisible(false);
        player.setAllowFlight(false);
        player.setCollidable(true);
        player.getInventory().clear ();
        player.setExp(0);
        player.setFoodLevel(20);
        player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 999999, 255, false));
    }

    @EventHandler
    public void onPlayerLeave (PlayerQuitEvent e) {
        Player player = e.getPlayer();

        if (gameManager.containsPlayer(player))
            gameManager.removePlayer(player);
    }
}
