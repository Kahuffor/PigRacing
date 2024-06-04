package com.stiggles.kartpigs;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class HitListener implements Listener {

    @EventHandler
    public void onHitEvent (EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player))
            return;

        e.setCancelled(true);
    }
}
