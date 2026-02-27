package crystal.champions.data;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.loot.v2.LootTableEvents;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.EntityPropertiesLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.NbtPredicate;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.util.Identifier;

public class ChampionsLootTableRegister implements ModInitializer {

    @Override
    public void onInitialize() {
        LootTableEvents.MODIFY.register((resourceManager, lootManager, id, tableBuilder, source) -> {
            if (source.isBuiltin() && id.getPath().startsWith("entities/")) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1));

                for (int tier = 1; tier <= 5; tier++) {
                    poolBuilder.with(LootTableEntry.builder(new Identifier("champions", "champions/tier_" + tier))
                            .conditionally(EntityPropertiesLootCondition.builder(
                                    LootContext.EntityTarget.THIS,
                                    EntityPredicate.Builder.create()
                                            .nbt(new NbtPredicate(createTierNbt(tier)))
                            ))
                    );
                }

                tableBuilder.pool(poolBuilder);
            }
        });
    }
    private static NbtCompound createTierNbt(int tier) {
        NbtCompound nbt = new NbtCompound();
        nbt.putInt("tier", tier);
        return nbt;
    }
}
