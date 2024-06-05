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
    private GameManager gameManager;
    private static Items items = new Items();

    public JoinCommand(Items items) { JoinCommand.items = items; }

    public JoinCommand(KartPigs pigKart) {
        JoinCommand.pigKart = pigKart;
        gameManager = KartPigs.getGameManager();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            if (pigKart.playersJoined >= pigKart.maxPlayers) { //If player is trying to join full lobby
                player.sendMessage(ChatColor.RED + "The lobby you are trying to join is full! (16/16)");
                player.playSound(player, Sound.ENTITY_VILLAGER_NO, SoundCategory.MASTER, 1, 1);

            } else {
                // instead of accessing KartPig's PKP array, we are going to access GameManager's PKP array
                PigKartPlayer p = new PigKartPlayer(player);
                gameManager.addPlayer(player);
                //pigKart.pkp.put(player.getUniqueId(), new PigKartPlayer(player));
                //PigKartPlayer p = KartPigs.getPigKartPlayer(player.getUniqueId());

                //pigPickingSequence(player);
            }
        }
        return false;
    }


}