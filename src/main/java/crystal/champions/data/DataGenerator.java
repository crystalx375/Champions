package crystal.champions.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.loot.context.LootContextTypes;

public class DataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider((FabricDataGenerator.Pack.Factory<ChampionsLootTable>) (output)
                -> new ChampionsLootTable(output, LootContextTypes.ENTITY));
    }
}