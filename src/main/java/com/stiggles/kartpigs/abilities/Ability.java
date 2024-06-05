package com.stiggles.kartpigs.abilities;

import com.stiggles.kartpigs.Kart;

public abstract class Ability {
    int duration;
    int cooldown;

    int canUseIn;
    int durationLeft;
    Kart user;

    public Ability (Kart user, int duration, int cooldown) {
        this.duration = duration;
        this.cooldown = cooldown;
        canUseIn = 0;
        durationLeft = 0;
        this.user = user;
    }
    public abstract void use ();

    public float getCooldown () {
        return cooldown;
    }
    public float getDuration () {
        return duration;
    }

    public void tick () {
        if (cooldown > 0)
            --cooldown;
        if (durationLeft > 0)
            --durationLeft;
    }

}
