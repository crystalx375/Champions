package crystal.champions.affix;

import net.minecraft.entity.LivingEntity;

public class LivelyAffix extends Affix {

    public LivelyAffix() {
        super("lively");
    }

    @Override
    public void onTick(LivingEntity entity) {
        if (entity.age % 20 != 0) return;

        assert entity.getAttacker() != null;
        if (entity.getAttacker().age - 100 >= 0) entity.heal(5);
        else entity.heal(1);
    }
}
