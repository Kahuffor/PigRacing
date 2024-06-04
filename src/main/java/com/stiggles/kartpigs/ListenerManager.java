package com.stiggles.kartpigs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class ListenerManager implements Listener {

    private static KartPigs pigKart;
    private static Items items;

    public ListenerManager(KartPigs pigKart) { ListenerManager.pigKart = pigKart; }




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

        GameManager gm = KartPigs.getGameManager();


        if (gm.containsPlayer(p)) { // Checking to see if the player is in the game
            PigKartPlayer pkPlayer = gm.getPlayerFromPigKart(p); //Make the pig player object
            pigIndex = pkPlayer.pigIndex; // Set the event var equal to the players index amount
            if (invItemStack.equals(items.backNextStar())) {

                if (e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    if (e.getHand().equals(EquipmentSlot.HAND)) {
                        // Right Click is the Next Option
                        if (pigIndex == (pigKart.maxPigs - 1)) { // Check if the index equals the max amount of pigs
                            // If so, then start at 0 to avoid errors.
                            pigIndex = 1;
                            p.sendMessage("You are currently viewing the " + pigKart.pigList.get(pigIndex));

                        } else {
                            // Else, then continue.
                            pigIndex += 1;
                            p.sendMessage("You are currently viewing the " + pigKart.pigList.get(pigIndex));
                        }
                    }

                } else if (e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
                    if (e.getHand().equals(EquipmentSlot.HAND)) {
                        // Left Click is the Back Option
                        if (pigIndex == 1) { // Check if the index equals the lowest array value of which a pig is held at.
                            // If so, then start at 0 to avoid errors.
                            pigIndex = pigKart.maxPigs - 1;
                            p.sendMessage("You are currently viewing the " + pigKart.pigList.get(pigIndex));

                        } else {
                            // Else, then continue.
                            pigIndex -= 1;
                            p.sendMessage("You are currently viewing the " + pigKart.pigList.get(pigIndex));
                        }
                    }
                }
            }
        }
    }


}
