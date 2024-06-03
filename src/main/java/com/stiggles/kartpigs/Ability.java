package com.stiggles.kartpigs;

public abstract class Ability {
    float duration;
    float cooldown;
    public Ability (float duration, float cooldown) {
        this.duration = duration;
        this.cooldown = cooldown;
    }
    public abstract void run ();

}
