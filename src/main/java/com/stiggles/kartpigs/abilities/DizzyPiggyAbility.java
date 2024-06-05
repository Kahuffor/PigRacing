package com.stiggles.kartpigs.abilities;

import com.stiggles.kartpigs.Kart;
import org.bukkit.entity.Pig;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DizzyPiggyAbility extends Ability {

    public DizzyPiggyAbility (Kart user)
    {
        super (user, 200, 600);
    }
    @Override
    public void use() {
        if (canUseIn != 0)
            return;
        user.getOwner().addPotionEffect(new PotionEffect (PotionEffectType.NAUSEA, 200, 255, true, false));
        user.getOwner().addPotionEffect(new PotionEffect (PotionEffectType.DARKNESS, 60, 1, true, false));
        canUseIn = cooldown;
    }
}
