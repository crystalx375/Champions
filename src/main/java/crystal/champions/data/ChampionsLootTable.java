package crystal.champions.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextType;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import net.minecraft.loot.function.EnchantWithLevelsLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.util.Identifier;

import java.util.function.BiConsumer;

public class ChampionsLootTable extends SimpleFabricLootTableProvider {

    public ChampionsLootTable(FabricDataOutput output, LootContextType lootContextType) {
        super(output, lootContextType);
    }

    @Override
    public void accept(BiConsumer<Identifier, LootTable.Builder> exporter) {
        generateTier(exporter, 1, ItemEntry.builder(Items.BOOK)
                .apply(EnchantRandomlyLootFunction.builder()));

        generateTier(exporter, 2, ItemEntry.builder(Items.BOOK)
                .apply(EnchantRandomlyLootFunction.builder()));

        generateTier(exporter, 3, ItemEntry.builder(Items.BOOK)
                .apply(EnchantRandomlyLootFunction.builder()));

        generateTier(exporter, 4, ItemEntry.builder(Items.BOOK)
                .apply(EnchantRandomlyLootFunction.builder()));

        exporter.accept(new Identifier("champions", "champions/tier_5"),
                LootTable.builder()
                        .pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1))
                                .with(ItemEntry.builder(Items.NETHERITE_HELMET))
                                .with(ItemEntry.builder(Items.NETHERITE_BOOTS))
                                .with(ItemEntry.builder(Items.NETHERITE_CHESTPLATE))
                                .with(ItemEntry.builder(Items.NETHERITE_LEGGINGS))
                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(30, 60)).allowTreasureEnchantments())
                        )
                        .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(0.2f))
                                .with(ItemEntry.builder(Items.NETHERITE_INGOT))
                        )
                        .pool(LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(3))
                                .with(ItemEntry.builder(Items.BOOK)
                                        .apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(80)).allowTreasureEnchantments()))
                        )
        );
    }
    private void generateTier(BiConsumer<Identifier, LootTable.Builder> exporter, int tier, LootPoolEntry.Builder<?> entry) {
        if (tier >= 5) return;
        exporter.accept(new Identifier("champions", "champions/tier_" + tier),
                LootTable.builder().pool(LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(tier))
                        .with(entry)
                )
        );
    }
}
