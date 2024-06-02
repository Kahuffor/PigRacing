package com.stiggles.kartpigs;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PigKartPlayer {

    private Player player;

    boolean pickingKart = false;
    int currentLap = 0;
    String pigSelection;

    public int pigIndex = 0;


    public PigKartPlayer (Player p) {
        player = p;
    }

    public Player getPlayer () {
        return player;
    }

    public UUID getUUID () {
        return player.getUniqueId();
    }

    public void teleport (Location location) {
        player.teleport(location);
    }

    public void sendMessage (String message) {
        player.sendMessage(message);
    }
    public void playSound (Sound sound, float volume, float pitch) {
        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}
