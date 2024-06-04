package com.stiggles.kartpigs;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

public class JoinCommand implements CommandExecutor {
    private static KartPigs pigKart;
    private static Items items;

    public JoinCommand(Items items) { JoinCommand.items = items; }
    public JoinCommand(KartPigs pigKart) { JoinCommand.pigKart = pigKart; }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (pigKart.playersJoined >= pigKart.maxPlayers) { //If player is trying to join full lobby
                player.sendMessage(ChatColor.RED + "The lobby you are trying to join is full! (16/16)");
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1, 1);

            } else {
                pigKart.pkp.put(player.getUniqueId(), new PigKartPlayer(player));
                PigKartPlayer p = KartPigs.getPigKartPlayer(player.getUniqueId());
                p.pigSelection = "0";
                player.getInventory().clear();
                player.getInventory().addItem(items.backNextStar());
                findSpot(player);
                player.playSound(player, Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1, 1);
                //pigPickingSequence(player);
            }
        }
        return false;
    }

    private void findSpot(Player player) {
        switch (pigKart.playersJoined) {
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

    private void pigPickingSequence(Player p){
        Location nextStandLoc = p.getLocation().add(1, 0, -1);
        Location backStandLoc = p.getLocation().add(-1, 0, -1);
        spawnNext(nextStandLoc);
        spawnBack(backStandLoc);
    }

    private void spawnNext(Location nextStandLoc) {
        ArmorStand nextStand = nextStandLoc.getWorld().spawn(nextStandLoc, ArmorStand.class);
        nextStand.setSmall(true);
        nextStand.setInvisible(true);
        nextStand.setInvulnerable(true);
        nextStand.setCustomNameVisible(true);
        nextStand.setCustomName(ChatColor.GREEN.toString() + ChatColor.BOLD + "<NEXT>");

    }

    private void spawnBack(Location backStandLoc) {
        ArmorStand backStand = backStandLoc.getWorld().spawn(backStandLoc, ArmorStand.class);
        backStand.setSmall(true);
        backStand.setInvisible(true);
        backStand.setInvulnerable(true);
        backStand.setCustomNameVisible(true);
        backStand.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD + "<BACK>");
    }



}
