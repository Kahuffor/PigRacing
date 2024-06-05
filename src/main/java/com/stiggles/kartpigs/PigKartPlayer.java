package com.stiggles.kartpigs;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PigKartPlayer {

    private Player player;
    private Kart kart;
    boolean pickingKart = false;
    int currentLap = 0;
    String pigSelection;

    public int pigIndex = 0;

    int canLapIn = 50;
    int CAN_LAP_DEFAULT = 50;

    int placement = -1;

    boolean entered = false;
    boolean backwards = false;
    boolean lapCounted = false;

    boolean finished = false;

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
        if (kart != null)
            kart.everyTick();
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
    public boolean hasEntered () {
        return entered;
    }
    public boolean isBackwards () {
        return backwards;
    }
    public void setEntered (boolean newVal) {
        entered = newVal;
    }
    public void setBackwards (boolean newVal) {
        backwards = newVal;
    }
    public boolean hasLapCounted () {
        return lapCounted;
    }
    public void setLapCounted (boolean newVal) {
        lapCounted = newVal;
    }
    public boolean isFinished () {
        return finished;
    }

    public void onComplete () {
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvisible(true);
        player.setInvulnerable(true);
        player.setAllowFlight(true);
        player.setCollidable(false);
        finished = true;
        if (kart != null) {
            kart.getPig().remove();
            kart = null;
        }
    }
    public void resetPlayer () {
        player.setGameMode(GameMode.ADVENTURE);
        player.setInvisible(false);
        player.setInvulnerable(true);
        player.setAllowFlight(false);
        player.setCollidable(true);
    }

    public void spawnKart () {
        kart = new Kart(player);
        kart.setSpeed(0);
    }
    public Kart getKart () {
        return kart;
    }


}
