package com.stiggles.kartpigs;

import com.stiggles.kartpigs.Cuboid.Cuboid;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public final class KartPigs extends JavaPlugin implements Listener {

    private static Items items;


    static GameManager gameManager = new GameManager();
    ArrayList<String> pigList = new ArrayList<String>();
    //ArrayList<UUID> pickingPig = new ArrayList<UUID>();
    int maxPlayers = 16;
    int playersJoined;
    int maxPigs;

    // HashMap<UUID, Integer> playerLapMap = new HashMap<UUID, Integer>();

    static KartPigs instance;
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
        instance = this;
        prepPigList();
        Bukkit.getPluginManager().registerEvents(this, this);
        Bukkit.getPluginManager().registerEvents(new ListenerManager(this), this);
        Bukkit.getPluginCommand("join").setExecutor(new JoinCommand(this));
        playersJoined = 0;
        gameManager.setStartingLine(new Cuboid(Bukkit.getWorld("world"), -425, 96, -10, -415, 102, -13));
        gameManager.setStartingLine(new Cuboid(Bukkit.getWorld("world"), -425, 96, -14, -415, 102, -16));
        gameManager.setStartingLine(new Cuboid(Bukkit.getWorld("world"), -425, 96, -10, -415, 102, -14));

        System.out.println("THE MAX AMOUNT OF PIGS THERE ARE IN THE ARRAY ARE " + maxPigs);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    //Adding the pigs to the list

    public void prepPigList(){
        pigList.add("1 Pig");
        pigList.add("2 Pig");
        pigList.add("3 Pig");
        pigList.add("4 Pig");

        maxPigs = pigList.size();
    }
    /*
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendMessage(ChatColor.AQUA + "To join the game, please do /join");
        p.teleport(new Location(Bukkit.getWorld("world"), -264.5 ,116.5, -24.5));
        p.setGameMode(GameMode.ADVENTURE);
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent e) {
        int pigIndex;
        Player p = e.getPlayer();
        ItemStack invItemStack = p.getInventory().getItemInMainHand();
        PigKartPlayer pkPlayer = getPigKartPlayer(p.getUniqueId());

        if (pkPlayer != null) {
            pigIndex = pkPlayer.pigIndex; // Set the event var equal to the players index amount
            if (invItemStack.equals(items.backNextStar())) {

                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (e.getHand().equals(EquipmentSlot.HAND)) {
                        // Right Click is the Next Option
                        if (pigIndex == (maxPigs - 1)) { // Check if the index equals the max amount of pigs
                            // If so, then start at 0 to avoid errors.
                            pigIndex = 1;
                            p.sendMessage("You are currently viewing the " + pigList.get(pigIndex));

                        } else {
                            // Else, then continue.
                            pigIndex += 1;
                            p.sendMessage("You are currently viewing the " + pigList.get(pigIndex));
                        }
                    }

                } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (e.getHand().equals(EquipmentSlot.HAND)) {
                        // Left Click is the Back Option
                        if (pigIndex == 1) { // Check if the index equals the lowest array value of which a pig is held at.
                            // If so, then start at 0 to avoid errors.
                            pigIndex = maxPigs - 1;
                            p.sendMessage("You are currently viewing the " + pigList.get(pigIndex));

                        } else {
                            // Else, then continue.
                            pigIndex -= 1;
                            p.sendMessage("You are currently viewing the " + pigList.get(pigIndex));
                        }
                    }
                }
            }
        }
    }



    public void spawnShowPig(String pigString, Location loc){
    Use pigString and catch method to show the right pig.
    }
     */
    public static GameManager getGameManager () {
        return gameManager;
    }
    public static KartPigs getPlugin () {
        return instance;
    }
}