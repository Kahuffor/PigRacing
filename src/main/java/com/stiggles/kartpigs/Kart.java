package com.stiggles.kartpigs;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.Queue;

public class Kart {
    double MAX_SPEED = 3.f;
    double speed = 1.f;
    double scale = 1.f;

    Player owner;
    Pig pig;
    Ability ability;

    Queue<Location> previousLocations  = new LinkedList<>();

    public Kart (Player owner) {
        this.owner = owner;
        pig = (Pig) Bukkit.getWorld ("world").spawnEntity (owner.getLocation(), EntityType.PIG);
        pig.setSaddle(true);
        pig.setInvulnerable(true);
        pig.addPassenger(owner);
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
    public Pig getPig () {
        return pig;
    }
    public void addNewLocation (Location location) {
        previousLocations.add (location);
        if (previousLocations.size() >= 30) {
            previousLocations.remove();
        }
    }
    public Location getMostDistantLocation () {
        return previousLocations.peek();
    }
    public void everyTick () {
        if (pig.getLocation().getY () <= 87) {
            pig.teleport(getMostDistantLocation());
            pig.addPassenger(owner.getPlayer());
        }
        Location under = new Location(pig.getWorld(), pig.getLocation().getX(), pig.getLocation().getY() - 1, pig.getLocation().getZ());
        if (!pig.getWorld().getBlockAt(under).getType().equals(Material.AIR)) {
            addNewLocation(pig.getLocation());
        }

    }
}
