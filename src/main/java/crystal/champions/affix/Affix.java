package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;

public abstract class Affix {
    private final String name;
    public Affix(String name) {this.name = name;}

    public String getName() { return name; }

    public void onTick(LivingEntity entity) {}
    public void onAttack(LivingEntity champion, MobEntity target) {}

    public void onHurt(LivingEntity champion, LivingEntity target) {}
}