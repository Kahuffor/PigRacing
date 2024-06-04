package com.stiggles.kartpigs;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CountdownOverride implements CommandExecutor {

    GameManager gameManager;
    public CountdownOverride (GameManager mng) {
        gameManager = mng;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        gameManager.setCountdown(5);
        return true;
    }
}
