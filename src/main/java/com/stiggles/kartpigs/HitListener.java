package com.stiggles.kartpigs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class HitListener implements Listener {

    @EventHandler
    public void onHitEvent (EntityDamageEvent e) {
        if (e.getEntity() instanceof Player)
            e.setCancelled(true);
    }
}
