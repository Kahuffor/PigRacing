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

    int canLapIn = 300;
    int CAN_LAP_DEFAULT = 300;

    int placement = -1;

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
    public void playSoundAt (Sound sound, float volume, float pitch, Location location) {
        player.playSound(location, sound, volume, pitch);
    }
    public Location getLocation () {
        return player.getLocation();
    }
    public void everyTick () {
        if (canLapIn > 0)
            --canLapIn;
    }
    public boolean canLap () {
        return canLapIn == 0;
    }
    public void addLap () {
        ++currentLap;
        canLapIn = CAN_LAP_DEFAULT;
    }
    public String getName () {
        return player.getName();
    }

}
