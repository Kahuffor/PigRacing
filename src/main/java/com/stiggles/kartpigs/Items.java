package com.stiggles.kartpigs;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class Items {

    public ItemStack backNextStar(){
        ItemStack item = new ItemStack(Material.NETHER_STAR);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "Pig Browser 3000");
        meta.setLore(Arrays.asList(
                String.valueOf(ChatColor.GRAY),
                        ChatColor.GRAY + "To use this correctly look at the following:",
                ChatColor.GRAY + "(RIGHT CLICK) --> View the next pig",
                ChatColor.GRAY + " ",
                ChatColor.GRAY + "(LEFT CLICK)  --> View the previous pig",
                ChatColor.GRAY + " ",
                ChatColor.GRAY + " "
        ));

        item.setItemMeta(meta);
        return item;
    }

    public ItemStack carrotOnAStick () {
        ItemStack is = new ItemStack(Material.CARROT_ON_A_STICK);
        ItemMeta im = is.getItemMeta();
        im.setUnbreakable(true);
        is.setItemMeta(im);
        return is;
    }
}
