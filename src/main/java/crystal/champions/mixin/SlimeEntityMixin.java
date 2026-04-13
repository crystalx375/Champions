package crystal.champions.mixin;

import crystal.champions.IChampions;
import crystal.champions.util.ChampionRank;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static crystal.champions.util.PrepareChampions.prepareAffixes;
import static crystal.champions.util.PrepareChampions.prepareAttributes;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin extends Entity {

    protected SlimeEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Инжектимся в метод remove, где создаются слаймы.
     */
    @Inject(
            method = "remove",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILSOFT
    )
    private void applyChampions(RemovalReason reason, CallbackInfo ci, int i, Text text, boolean bl, float f, float g, int j, int k, int l, float h, float m, SlimeEntity slimeEntity) {
        IChampions child = (IChampions) slimeEntity;
        ChampionRank rank = ChampionRank.getRandomRank(slimeEntity.getRandom());
        if (rank.tier() > 0) {
            child.champions$setChampionTier(rank.tier());
            child.champions$setAffixesString(prepareAffixes(rank));
            prepareAttributes(slimeEntity, rank);
        }
    }
}