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

import org.bukkit.ChatColor;
import org.bukkit.Sound;

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
    public void construct () {
        initializeLobbyPhase();
    }
    public void destruct () {

    }

    public void everySecond () {

        if (countdown > -1)
            --countdown;

        if (currentState == GameStates.LOBBY) {
            if (inGamePlayers.isEmpty()) {
                if (!cancelled)
                    cancelCountdown();
                return;
            }
            if (inGamePlayers.size () > 1 && cancelled)
                initializeLobbyPhase();

            if (countdown % 30 == 0 || countdown <= 10) {
                for (PigKartPlayer p : inGamePlayers) {
                    p.sendMessage(ChatColor.GREEN + "Game begins in " + countdown + " seconds");
                    p.playSound(Sound.BLOCK_NOTE_BLOCK_CHIME, 1, 1);
                }
            }
            if (countdown == 0) {

            }

        }
        if (currentState == GameStates.PIG_SELECT) {

        }
        if (currentState == GameStates.PRERACE) {

        }
        if (currentState == GameStates.BEGIN_RACE) {

        }
        if (currentState == GameStates.RACE) {

        }
        if (currentState == GameStates.END) {

        }
        if (currentState == GameStates.RESULTS) {

        }
    }
    public void initializeLobbyPhase () {
        currentState = GameStates.LOBBY;
        setCountdown(120);
        cancelled = false;
    }
    public void runLobbyPhase () {

    }
    public void initializePigSelectPhase () {
        currentState = GameStates.PIG_SELECT;
    }
    public void runPigSelectPhase () {

    }
    public void initializePreRacePhase () {
        currentState = GameStates.PRERACE;
    }
    public void runPreRacePhase () {

    }
    public void initializeBeginRacePhase () {
        currentState = GameStates.BEGIN_RACE;
    }
    public void runBeginRacePhase () {

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
}
