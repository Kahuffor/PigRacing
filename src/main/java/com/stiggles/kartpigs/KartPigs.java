package com.stiggles.kartpigs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
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
public final class KartPigs extends JavaPlugin implements Listener {

    ArrayList<String> pigList = new ArrayList<String>();
    //ArrayList<UUID> pickingPig = new ArrayList<UUID>();
    int maxPlayers = 16;
    int playersJoined;
   // HashMap<UUID, Integer> playerLapMap = new HashMap<UUID, Integer>();
    //HashMap<UUID, String> playerPigSelection = new HashMap<UUID, String>();
    // ONLY IF NEEDED --> HashMap<UUID, Integer> playerPlaceMap = new HashMap<UUID, Integer>();
   public static HashMap <UUID, PigKartPlayer> pkp = new HashMap<>();

    public static PigKartPlayer getPigKartPlayer (UUID uuid) {
        // Gives you a pigkartplayer corresponding to uuid
        return pkp.get (uuid);
    }


    @Override
    public void onEnable() {
        prepPigList();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginCommand("join").setExecutor(new JoinCommand(this));
        playersJoined = 0;
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    //Adding the pigs to the list

    public void prepPigList(){
        pigList.add("1 pig");
        pigList.add("2 pig");
        pigList.add("3 pig");
        pigList.add("4 pig");

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.AQUA + "To join the game, please do /join");
        p.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onPlayerIntStand(PlayerInteractEntityEvent e){
        if (e.getRightClicked().getType() == EntityType.ARMOR_STAND){
            ArmorStand armorStand = (ArmorStand) e.getRightClicked();
            Player p = e.getPlayer();
            PigKartPlayer pkp = getPigKartPlayer(p.getUniqueId());
            if (armorStand.getName().equals(ChatColor.GREEN.toString() + ChatColor.BOLD + "<NEXT>")) {
                int index = pkp.pigIndex += 1;
                p.sendMessage(pigList.get(index));


            } else if (armorStand.getName().equals(ChatColor.RED.toString() + ChatColor.BOLD + "<BACK>")) {
                int index = pkp.pigIndex -= 1;
                p.sendMessage(pigList.get(index));

            }

        }
    }

}