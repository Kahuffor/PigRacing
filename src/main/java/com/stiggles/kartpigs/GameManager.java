package com.stiggles.kartpigs;


/*
public enum GameStates {
    LOBBY,
    CHARACTER_SELECT,
    PRERACE,
    BEGIN_RACE,
    RACE,
    END,
    RESULTS
}*/
/*
PIGKART

Cancel Barrel and ArmorStand events by default

Round = LOBBY
- Countdown from 2 min begins when 2 players join
- Countdown cancels if at 1 person
- Countdown decreases to 10 sec when MAX_PLAYERS join
- At zero, teleport players to character selection screen and switch round to CHARACTER_SELECT

Round = CHARACTER_SELECT
- 30 second countdown for players to choose their pig.
- Players should not be able to move in this phase, or see each other
- Prepare map
- When timer reaches zero, teleport players and according pigs to the start of the track.
- Switch to PRERACE

Round = PRERACE
- 5 second countdown to begin
- Do not allow player movement
- At zero switch to RACE

Round = RACE
- Ensure players are properly reaching checkpoints, count each lap a player has reached.
-
Round = END
 */
import com.stiggles.kartpigs.Cuboid.Cuboid;
import org.bukkit.*;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;

public class GameManager {
    public static final int MAX_PLAYERS = 16;
    public final int LOBBY_COUNTDOWN_TIME = 120;
    public final int CHARACTER_SELECT_COUNTDOWN = 30;
    public final int RACE_COUNTDOWN_TIME = 5;

    private int countdown = -1;
    private boolean full = false;

    private GameStates currentState = GameStates.LOBBY;
    private ArrayList<PigKartPlayer> inGamePlayers = new ArrayList<>();

    private static Items items;

    private boolean beginEndCountdown = false;

    boolean cancelled = false;
    Cuboid startingLine;
    Cuboid reverseCheck;

    int placement = 1;

    ArrayList<PigKartPlayer> winners = new ArrayList<>();
    ArrayList<Location> raceSpawnpoints = new ArrayList<>();
    int ticksPassed = 0;

    boolean active = true;

    boolean bypassStartRestrictions = false;

    Location countdownBlock3a = new Location(Bukkit.getWorld("world"), -423, 103, -11);
    Location countdownBlock3b = new Location(Bukkit.getWorld("world"), -423, 102, -11);
    Location countdownBlock2a = new Location(Bukkit.getWorld("world"), -421, 103, -11);
    Location countdownBlock2b = new Location(Bukkit.getWorld("world"), -421, 102, -11);
    Location countdownBlock1a = new Location(Bukkit.getWorld("world"), -419, 103, -11);
    Location countdownBlock1b = new Location(Bukkit.getWorld("world"), -419, 102, -11);
    Location countdownBlock0a = new Location(Bukkit.getWorld("world"), -417, 103, -11);
    Location countdownBlock0b = new Location(Bukkit.getWorld("world"), -417, 102, -11);

    public GameManager () {
        construct ();
        items = new Items ();
        Bukkit.getScheduler().runTaskTimer(KartPigs.getPlugin(), this::everyTick, 1, 1);
        prepRaceSpawn();
    }
    public void setStartingLine (Cuboid cuboid) {
        startingLine = cuboid;
    }
    public void setReverseCheck (Cuboid cuboid) {
        reverseCheck = cuboid;
    }
    public void construct () {
        initializeLobbyPhase();
        if (startingLine == null)
            startingLine = new Cuboid(new Location(Bukkit.getWorld("world"), -5, 0, -1), new Location(Bukkit.getWorld("world"), 5, 5, 1));
        if (reverseCheck == null)
            reverseCheck = new Cuboid (new Location(Bukkit.getWorld("world"), -5, 0, -1), new Location(Bukkit.getWorld("world"), 5, 5, 1));

    }
    public void destruct () {

    }
    public void everyTick () {
        if (active)
            ++ticksPassed;
        if (ticksPassed % 20 == 0)
            everySecond();

        if (currentState != GameStates.RACE)
            return;

        // During race logic
        for (PigKartPlayer p : inGamePlayers) {
            if (startingLine.contains(p.getLocation ())) {
                if (!p.hasEntered()) {
                    p.setEntered(true);
                    //if (!p.isBackwards()) {
                    p.addLap();

                    if (p.currentLap == 3) {
                        if (p.placement == -1) {
                            winners.add(p);
                            p.placement = winners.size();
                            p.onComplete();
                            sendMessageToAll(ChatColor.GOLD + "#" + winners.size() + " - " + p.getName());
                            if (!beginEndCountdown && winners.size() >= inGamePlayers.size() * 0.35) {
                                sendMessageToAll(ChatColor.RED + ChatColor.BOLD.toString() + "You have 15 seconds left to complete the race");
                                beginEndCountdown = true;
                                countdown = 15;
                            }
                        }
                    }
                    else {
                        sendMessageToAll(ChatColor.GOLD + p.getName() + " has completed lap " + p.currentLap);
                    }
                    //}
                }
            }
            else {
                if (p.hasEntered()) {
                    p.setEntered(false);
                }
            }

           // if (reverseCheck.contains (p.getLocation()) && !p.hasEntered())
            //    p.setBackwards(true);


           // if (p.isBackwards()) {
                /*if (p.hasEntered() && !startingLine.contains(p.getLocation())) {
                    p.setEntered(false);
                    p.setBackwards(false);
                    p.setLapCounted (false);
                }*/
            //}
            if (!p.isFinished ())
                p.everyTick();


        }
    }
    public void everySecond () {
        if (countdown > -1)
            --countdown;

        if (currentState == GameStates.LOBBY) {
            runLobbyPhase();
        }
        if (currentState == GameStates.PIG_SELECT) {
           runPigSelectPhase();
        }
        if (currentState == GameStates.PRERACE) {
            runPreRacePhase();
        }
        if (currentState == GameStates.RACE) {
            runRacePhase();
        }
        if (currentState == GameStates.END) {
            runEndPhase();
        }
        if (currentState == GameStates.RESULTS) {
            runResultsPhase();
        }
    }
    public void initializeLobbyPhase () {
        currentState = GameStates.LOBBY;
        setCountdown(120);
        cancelled = false;
    }

    public void runLobbyPhase () {
        if (inGamePlayers.isEmpty()) {
            if (!cancelled)
                cancelCountdown();
            return;
        }

        if (inGamePlayers.size () > 1 && cancelled)
            initializeLobbyPhase();
        if (countdown == 0) {
            initializePigSelectPhase();
            return;
        }
        if (countdown % 30 == 0 || countdown <= 10) {
            for (PigKartPlayer p : inGamePlayers) {
                p.sendMessage(ChatColor.GREEN + "Game begins in " + countdown + " seconds");

            }
        }

    }
    public void initializePigSelectPhase () {
        countdown = 30;
        sendMessageToAll(ChatColor.GRAY + "You have 30 seconds to select a pig!");
        currentState = GameStates.PIG_SELECT;

    }
    public void runPigSelectPhase () {
        if (countdown == 0) {
            initializePreRacePhase();
            return;
        }
        if ((countdown < 30 && countdown % 10 == 0) || countdown <= 5) {
            sendMessageToAll(ChatColor.GRAY + "Starting in " + countdown + " seconds!");
        }
    }
    public void initializePreRacePhase () {
        currentState = GameStates.PRERACE;
        countdown = 10;
        sendMessageToAll(ChatColor.GREEN + "Get ready to race!");
        // TP Players here
        teleportToRaceStart();

    }
    public void runPreRacePhase () {
        if (countdown == 0) {
            for (PigKartPlayer p : inGamePlayers) {
                Bukkit.getWorld("world").getBlockAt(countdownBlock3a).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock3b).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock2a).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock2b).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock1a).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock1b).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock0a).setType(Material.VERDANT_FROGLIGHT);
                Bukkit.getWorld("world").getBlockAt(countdownBlock0b).setType(Material.VERDANT_FROGLIGHT);

                p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "GO!!!");
                p.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 2);
                p.playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 2);
                ticksPassed = 0;
                // What if we play a piggy sound here
            }
            initializeRacePhase();
            return;
        }
        if (countdown <= 3) {
            for (PigKartPlayer p : inGamePlayers) {
                p.sendMessage(ChatColor.GREEN + String.valueOf(countdown) + "...");
                p.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
                p.playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 1);
                if (countdown == 3) {

                    Bukkit.getWorld("world").getBlockAt(countdownBlock3a).setType(Material.PEARLESCENT_FROGLIGHT);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock3b).setType(Material.PEARLESCENT_FROGLIGHT);
                } else if (countdown == 2) {
                    Bukkit.getWorld("world").getBlockAt(countdownBlock3a).setType(Material.WHITE_CONCRETE);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock3b).setType(Material.WHITE_CONCRETE);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock2a).setType(Material.PEARLESCENT_FROGLIGHT);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock2b).setType(Material.PEARLESCENT_FROGLIGHT);
                } else if (countdown == 1){
                    Bukkit.getWorld("world").getBlockAt(countdownBlock2a).setType(Material.WHITE_CONCRETE);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock2b).setType(Material.WHITE_CONCRETE);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock1a).setType(Material.PEARLESCENT_FROGLIGHT);
                    Bukkit.getWorld("world").getBlockAt(countdownBlock1b).setType(Material.PEARLESCENT_FROGLIGHT);
                }


            }
        }
    }
    public void initializeRacePhase () {
        countdown = -1;
        currentState = GameStates.RACE;

        for (int i = 0; i < inGamePlayers.size(); ++i) {
            inGamePlayers.get (i).getPlayer().getInventory().addItem(items.carrotOnAStick());
            Kart kart = inGamePlayers.get(i).getKart();
            if (i > 8) {
                kart.setSpeed(kart.MAX_SPEED + 0.05f);
            }
            else {
                kart.setSpeed(kart.MAX_SPEED);
            }
        }
        // Will reduce speed after 10 seconds of racing
        Bukkit.getScheduler().runTaskLater(KartPigs.getPlugin(), () -> {
            for (int i = 8; i < inGamePlayers.size(); ++i) {
                Kart kart = inGamePlayers.get(i).getKart();
                kart.setSpeed(kart.MAX_SPEED);
            }
        }, 200);

    }
    public void runRacePhase () {
        if (countdown == 0) {
            initializeEndPhase();
        }
    }
    public void initializeEndPhase () {
        currentState = GameStates.END;
        Bukkit.getWorld("world").getBlockAt(countdownBlock3a).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock3b).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock2a).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock2b).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock1a).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock1b).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock0a).setType(Material.WHITE_CONCRETE);
        Bukkit.getWorld("world").getBlockAt(countdownBlock0b).setType(Material.WHITE_CONCRETE);

        for (PigKartPlayer p : inGamePlayers) {
            p.onComplete();
        }
        // mAKE ALL PLAYERS INVIS AND STUFF HERE
        sendMessageToAll(ChatColor.GREEN +    "=============================");
        sendMessageToAll(ChatColor.WHITE +    "          Pig Kart         \n");
        if (winners.isEmpty()) {
            sendMessageToAll(ChatColor.GRAY + "             Tie             ");
        }
        else {
            sendMessageToAll(ChatColor.GOLD + "1st - " + winners.get (0).getName());
            if (winners.size() >= 2) {
                sendMessageToAll(ChatColor.GOLD + "2nd - " + winners.get (1).getName());
            }
            if (winners.size() >= 3) {
                sendMessageToAll(ChatColor.GREEN + "3rd - " + winners.get (2).getName());
            }
        }
        sendMessageToAll(ChatColor.GREEN +    "=============================");
    }
    public void runEndPhase () {

    }
    public void initializeResultsPhase () {
        currentState = GameStates.RESULTS;
    }
    public void runResultsPhase () {

    }
    public void setCountdown (int time) {
        countdown = time;
    }
    public int getCountdown () {
        return countdown;
    }
    public void startCountdown () {
        countdown = 120;
    }
    public void cancelCountdown () {
        countdown = -1;
        cancelled = true;
    }
    public void sendMessageToAll (String message) {
        for (PigKartPlayer p : inGamePlayers)
            p.sendMessage(message);
    }

    public ArrayList<PigKartPlayer> getInGamePlayers() {
        return inGamePlayers;
    }

    public boolean containsPlayer (Player player) {
        for (PigKartPlayer p : inGamePlayers) {
            if (p.getUUID() == player.getUniqueId())
                return true;
        }
        return false;
    }
    public void beginCountdown () {
        countdown = 15;
    }

    public PigKartPlayer getPlayerFromPigKart (Player player) {
        for (PigKartPlayer p : inGamePlayers) {
            if (p.getUUID() == player.getUniqueId())
                return p;
        }
        return null;
    }
    public void addPlayer (Player player) {
        if (containsPlayer(player))
            return;
        PigKartPlayer p = new PigKartPlayer (player);
        inGamePlayers.add (p);
        sendMessageToAll(ChatColor.GREEN + "Pig Kart: " + player.getName() + " has joined (" + inGamePlayers.size () + "/" + MAX_PLAYERS + ")");

        p.pigSelection = "0";
        player.getInventory().clear();
        player.getInventory().addItem(items.backNextStar());
        findSpot(player);
        player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1, 1);
    }
    public void addPlayer (PigKartPlayer player) {
        if (!inGamePlayers.contains(player))
            inGamePlayers.add (player);
        sendMessageToAll(ChatColor.GREEN + "Pig Kart: " + player.getName() + " has joined (" + inGamePlayers.size () + "/" + MAX_PLAYERS + ")");

    }
    public void removePlayer (Player player) {
        for (PigKartPlayer p : inGamePlayers) {
            if (p.getUUID() == player.getUniqueId()) {
                inGamePlayers.remove (p);
                sendMessageToAll(ChatColor.GREEN + "Pig Kart: " + player.getName() + " has quit!");
                return;
            }
        }
    }
    public void removePlayer (PigKartPlayer player) {
        inGamePlayers.remove (player);
        sendMessageToAll(ChatColor.GREEN + "Pig Kart: " + player.getName() + " has quit!");
    }

    private void findSpot(Player player) {
        switch (inGamePlayers.size() - 1) {
            case 0:
                player.teleport(new Location(player.getWorld(), -252.5, 115, -15.5, 180, -4));
                break;
            case 1:
                player.teleport(new Location(player.getWorld(), -247.5, 115, -15.5, 180, -4));
                break;
            case 2:
                player.teleport(new Location(player.getWorld(), -242.5, 115, -15.5, 180, -4));
                break;
            case 3:
                player.teleport(new Location(player.getWorld(), -237.5, 115, -15.5, 180, -4));
                break;
            case 4:
                player.teleport(new Location(player.getWorld(), -232.5, 115, -15.5, 180, -4));
                break;
            case 5:
                player.teleport(new Location(player.getWorld(), -227.5, 115, -15.5, 180, -4));
                break;
            case 6:
                player.teleport(new Location(player.getWorld(), -222.5, 115, -15.5, 180, -4));
                break;
            case 7:
                player.teleport(new Location(player.getWorld(), -217.5, 115, -15.5, 180, -4));
                break;
            case 8:
                player.teleport(new Location(player.getWorld(), -252.5, 120, -15.5, 180, -4));
                break;
            case 9:
                player.teleport(new Location(player.getWorld(), -247.5, 120, -15.5, 180, -4));
                break;
            case 10:
                player.teleport(new Location(player.getWorld(), -242.5, 120, -15.5, 180, -4));
                break;
            case 11:
                player.teleport(new Location(player.getWorld(), -237.5, 120, -15.5, 180, -4));
                break;
            case 12:
                player.teleport(new Location(player.getWorld(), -232.5, 120, -15.5, 180, -4));
                break;
            case 13:
                player.teleport(new Location(player.getWorld(), -227.5, 120, -15.5, 180, -4));
                break;
            case 14:
                player.teleport(new Location(player.getWorld(), -222.5, 120, -15.5, 180, -4));
                break;
            case 15:
                player.teleport(new Location(player.getWorld(), -217.5, 120, -15.5, 180, -4));
                break;
        }
    }
    public void teleportToRaceStart () {
        for (int i = 0; i < inGamePlayers.size (); ++i) {
            inGamePlayers.get(i).teleport(raceSpawnpoints.get(i));
            inGamePlayers.get(i).spawnKart();
        }
    }

    public void prepRaceSpawn () {
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -415.5, 96, -6.5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -417.5, 96, -6.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -419.5, 96, -6.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -421.5, 96, -6.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -423.5, 96, -6.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -422.5, 96, -4.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -420.5, 96, -4.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -418.5, 96, -4.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -416.5, 96, -4.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -415.5, 96, -2.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -417.5, 96, -2.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -419.5, 96, -2.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -421.5, 96, -2.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -423.5, 96, -2.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -422.5, 96, -0.5, 180, -5));
        raceSpawnpoints.add (new Location(Bukkit.getWorld("world"), -420.5, 96, -0.5, 180, -5));
    }

    public void getRaceSpot(int i){

    }

}
