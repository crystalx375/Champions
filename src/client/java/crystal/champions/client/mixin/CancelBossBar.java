package crystal.champions.client.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.entity.boss.BossBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BossBarHud.class)
public class CancelBossBar {

    @Unique
    private boolean isChampion(BossBar bar) {
        String name = bar.getName().getString();
        return name.contains("★") || name.contains("Skilled") || name.contains("*") || name.contains("affix.");
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;)V", at = @At("HEAD"))
    private void onRenderStart(DrawContext context, CallbackInfo ci) {
    }

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;)V", at = @At("HEAD"), cancellable = true)
    private void cancelChampionBar(DrawContext context, int x, int y, BossBar bar, CallbackInfo ci) {
        if (isChampion(bar)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderBossBar(Lnet/minecraft/client/gui/DrawContext;IILnet/minecraft/entity/boss/BossBar;II)V", at = @At("HEAD"), cancellable = true)
    private void cancelChampionBarText(DrawContext context, int x, int y, BossBar bar, int width, int height, CallbackInfo ci) {
        if (isChampion(bar)) {
            ci.cancel();
        }
    }
}