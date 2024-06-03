package com.stiggles.kartpigs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public class Kart {
    double speed = 1.f;
    double scale = 1.f;

    Pig pig;
    Ability ability;

    public Kart () {
        pig = (Pig) Bukkit.getWorld ("world").spawnEntity (new Location(Bukkit.getWorld ("world"), 0, 0, 0), EntityType.PIG);
        pig.setSaddle(true);
    }
    public void setSpeed (double newSpeed) {
        speed = newSpeed;
        pig.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
    }
    public void setScale (double newScale) {
        scale = newScale;
    }
    public double getSpeed () {
        return speed;
    }
    public double getScale () {
        return scale;
    }
    public void setRider (PigKartPlayer p) {
        pig.addPassenger(p.getPlayer());
    }
}
