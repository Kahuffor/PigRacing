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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Pig;

import java.util.ArrayList;

public class GameManager {

    public final int LOBBY_COUNTDOWN_TIME = 120;
    public final int CHARACTER_SELECT_COUNTDOWN = 30;
    public final int RACE_COUNTDOWN_TIME = 5;

    private int countdown = -1;
    private boolean full = false;

    private GameStates currentState = GameStates.LOBBY;
    private ArrayList<PigKartPlayer> inGamePlayers = new ArrayList<>();

    boolean cancelled = false;
    Cuboid startingLine;

    int placement = 1;

    boolean bypassStartRestrictions = false;

    public void setStartingLine (Cuboid cuboid) {
        startingLine = cuboid;
    }
    public void construct () {
        initializeLobbyPhase();
        if (startingLine == null)
            startingLine = new Cuboid(new Location(Bukkit.getWorld("world"), -5, 0, -1), new Location(Bukkit.getWorld("world"), 5, 5, 1));
    }
    public void destruct () {

    }
    public void everyTick () {
        for (PigKartPlayer p : inGamePlayers) {
            p.everyTick();
            if (startingLine.contains(p.getLocation ())) {
                if (p.canLap()) {
                    p.addLap();
                    sendMessageToAll(ChatColor.GOLD + p.getName() + " has completed lap " + p.currentLap);
                    if (p.currentLap == 3) {
                        p.placement = placement;
                        ++placement;
                    }
                }
            }
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
    }
    public void runPreRacePhase () {
        if (countdown == 0) {
            for (PigKartPlayer p : inGamePlayers) {
                p.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "GO!!!");
                p.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 2);
                p.playSound(Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 2);
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

            }
        }
    }
    public void initializeRacePhase () {
        currentState = GameStates.RACE;
    }
    public void runRacePhase () {

    }
    public void initializeEndPhase () {
        currentState = GameStates.END;
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
}
