package crystal.champions.client.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import crystal.champions.config.ChampionsConfigClient;
import crystal.champions.config.ChampionsConfigServer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ChampionsModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return this::createConfigScreen;
    }

    private Screen createConfigScreen(Screen parent) {
        ChampionsConfigClient cfg = ChampionsConfigClient.get();
        ChampionsConfigServer config = ChampionsConfigServer.get();

        Map<String, Object> changes = new HashMap<>();
        Map<String, Object> changesServer = new HashMap<>();


        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("champions.title"));

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        ConfigCategory hud = builder.getOrCreateCategory(Text.translatable("champions.category.client"));


        var colors = entryBuilder.startSubCategory(Text.translatable("champions.hud.colors"))
                .setExpanded(true);

        final String hex_1 = "hex_tier_1";
        final String hex_2 = "hex_tier_2";
        final String hex_3 = "hex_tier_3";
        final String hex_4 = "hex_tier_4";
        final String hex_5 = "hex_tier_5";

        final String hexFormat = "#%06X";

        final String colorHex = "champions.tooltip.color_hex";

        colors.add(entryBuilder.startColorField(Text.translatable("champions.hex_tier_1"), parseColor(cfg.hexTier1))
                .setDefaultValue(parseColor("#FFFF55"))
                .setSaveConsumer(colorInt -> changes.put(hex_1, String.format(hexFormat, (0xFFFFFF & colorInt))))
                .setTooltip(Text.translatable(colorHex))
                .build());

        colors.add(entryBuilder.startColorField(Text.translatable("champions.hex_tier_2"), parseColor(cfg.hexTier2))
                .setDefaultValue(parseColor("#F57C2C"))
                .setSaveConsumer(colorInt -> changes.put(hex_2, String.format(hexFormat, (0xFFFFFF & colorInt))))
                .setTooltip(Text.translatable(colorHex))
                .build());

        colors.add(entryBuilder.startColorField(Text.translatable("champions.hex_tier_3"), parseColor(cfg.hexTier3))
                .setDefaultValue(parseColor("#46DFFA"))
                .setSaveConsumer(colorInt -> changes.put(hex_3, String.format(hexFormat, (0xFFFFFF & colorInt))))
                .setTooltip(Text.translatable(colorHex))
                .build());

        colors.add(entryBuilder.startColorField(Text.translatable("champions.hex_tier_4"), parseColor(cfg.hexTier4))
                .setDefaultValue(parseColor("#8823DB"))
                .setSaveConsumer(colorInt -> changes.put(hex_4, String.format(hexFormat, (0xFFFFFF & colorInt))))
                .setTooltip(Text.translatable(colorHex))
                .build());

        colors.add(entryBuilder.startColorField(Text.translatable("champions.hex_tier_5"), parseColor(cfg.hexTier5))
                .setDefaultValue(parseColor("#F98AFF"))
                .setSaveConsumer(colorInt -> changes.put(hex_5, String.format(hexFormat, (0xFFFFFF & colorInt))))
                .setTooltip(Text.translatable(colorHex))
                .build());

        hud.addEntry(colors.build());


        var yOffsets = entryBuilder.startSubCategory(Text.translatable("champions.hud.y_offset"))
                .setExpanded(true);

        final String yOf1 = "y_offset_stars";
        final String yOf2 = "y_offset_text";
        final String yOf3 = "y_offset_bar";
        final String yOf4 = "y_offset_affixes";

        yOffsets.add(entryBuilder.startIntField(Text.translatable("champions.y_offset_stars"), cfg.yOffsetStars)
                .setDefaultValue(-5)
                .setMin(-100).setMax(5000)
                .setSaveConsumer(val -> changes.put(yOf1, val))
                .setTooltip(Text.translatable("champions.tooltip.y_offset_stars"))
                .build());

        yOffsets.add(entryBuilder.startIntField(Text.translatable("champions.y_offset_text"), cfg.yOffsetText)
                .setDefaultValue(7).setMin(-100).setMax(5000)
                .setSaveConsumer(val -> changes.put(yOf2, val))
                .setTooltip(Text.translatable("champions.tooltip.y_offset_text"))
                .build());

        yOffsets.add(entryBuilder.startIntField(Text.translatable("champions.y_offset_bar"), cfg.yOffsetBar)
                .setDefaultValue(19).setMin(-100).setMax(5000)
                .setSaveConsumer(val -> changes.put(yOf3, val))
                .setTooltip(Text.translatable("champions.tooltip.y_offset_bar"))
                .build());

        yOffsets.add(entryBuilder.startIntField(Text.translatable("champions.y_offset_affixes"), cfg.yOffsetAffixes)
                .setDefaultValue(29).setMin(-100).setMax(5000)
                .setSaveConsumer(val -> changes.put(yOf4, val))
                .setTooltip(Text.translatable("champions.tooltip.y_offset_affixes"))
                .build());

        hud.addEntry(yOffsets.build());


        var xOffsets = entryBuilder.startSubCategory(Text.translatable("champions.hud.x_offset"))
                .setExpanded(true);

        final String xOf1 = "x_offset_stars";
        final String xOf2 = "x_offset_text";
        final String xOf3 = "x_offset_bar";
        final String xOf4 = "x_offset_affixes";

        xOffsets.add(entryBuilder.startIntField(Text.translatable("champions.x_offset_stars"), cfg.xOffsetStars)
                .setDefaultValue(0).setMin(-5000).setMax(5000)
                .setSaveConsumer(val -> changes.put(xOf1, val))
                .setTooltip(Text.translatable("champions.tooltip.x_offset_stars"))
                .build());

        xOffsets.add(entryBuilder.startIntField(Text.translatable("champions.x_offset_text"), cfg.xOffsetText)
                .setDefaultValue(0).setMin(-5000).setMax(5000)
                .setSaveConsumer(val -> changes.put(xOf2, val))
                .setTooltip(Text.translatable("champions.tooltip.x_offset_text"))
                .build());

        xOffsets.add(entryBuilder.startIntField(Text.translatable("champions.x_offset_bar"), cfg.xOffsetBar)
                .setDefaultValue(0).setMin(-5000).setMax(5000)
                .setSaveConsumer(val -> changes.put(xOf3, val))
                .setTooltip(Text.translatable("champions.tooltip.x_offset_bar"))
                .build());

        xOffsets.add(entryBuilder.startIntField(Text.translatable("champions.x_offset_affixes"), cfg.xOffsetAffixes)
                .setDefaultValue(0).setMin(-5000).setMax(5000)
                .setSaveConsumer(val -> changes.put(xOf4, val))
                .setTooltip(Text.translatable("champions.tooltip.x_offset_affixes"))
                .build());

        hud.addEntry(xOffsets.build());


        var barRender = entryBuilder.startSubCategory(Text.translatable("champions.hud.bar_render"))
                .setExpanded(true);

        final String alwaysRender = "always_render";
        final String cacheC = "cache_client";
        final String cacheS = "cache_server";

        barRender.add(entryBuilder.startBooleanToggle(Text.translatable("champions.always_render"), cfg.alwaysRenderBox)
                .setDefaultValue(false)
                .setSaveConsumer(val -> changes.put(alwaysRender, val))
                .setTooltip(Text.translatable("champions.tooltip.always_render"))
                .build());

        barRender.add(entryBuilder.startIntField(Text.translatable("champions.cache_client"), cfg.cacheClient)
                .setDefaultValue(1000).setMin(0).setMax(10000)
                .setSaveConsumer(val -> changes.put(cacheC, val))
                .setTooltip(Text.translatable("champions.tooltip.cache_client"))
                .build());

        barRender.add(entryBuilder.startIntField(Text.translatable("champions.cache_server"), cfg.cacheServer)
                .setDefaultValue(5000).setMin(0).setMax(10000)
                .setSaveConsumer(val -> changes.put(cacheS, val))
                .setTooltip(Text.translatable("champions.tooltip.cache_server"))
                .build());

        hud.addEntry(barRender.build());



        ConfigCategory server = builder.getOrCreateCategory(Text.translatable("champions.category.server"));

        var tiersWeight = entryBuilder.startSubCategory(Text.translatable("champions.hud.tiers_w"))
                .setExpanded(true);

        final String w0 = "tier0_weight";
        final String w1 = "tier1_weight";
        final String w2 = "tier2_weight";
        final String w3 = "tier3_weight";
        final String w4 = "tier4_weight";
        final String w5 = "tier5_weight";


        tiersWeight.add(entryBuilder.startIntField(Text.translatable("champions.tier0_weight"), config.w0)
                .setDefaultValue(9320).setMin(0)
                .setSaveConsumer(val -> changesServer.put(w0, val))
                .setTooltip(Text.translatable("champions.tooltip.tier0_weight"))
                .build());

        tiersWeight.add(entryBuilder.startIntField(Text.translatable("champions.tier1_weight"), config.w1)
                .setDefaultValue(490).setMin(0)
                .setSaveConsumer(val -> changesServer.put(w1, val))
                .setTooltip(Text.translatable("champions.tooltip.tier1_weight"))
                .build());

        tiersWeight.add(entryBuilder.startIntField(Text.translatable("champions.tier2_weight"), config.w2)
                .setDefaultValue(150).setMin(0)
                .setSaveConsumer(val -> changesServer.put(w2, val))
                .setTooltip(Text.translatable("champions.tooltip.tier2_weight"))
                .build());

        tiersWeight.add(entryBuilder.startIntField(Text.translatable("champions.tier3_weight"), config.w3)
                .setDefaultValue(32).setMin(0)
                .setSaveConsumer(val -> changesServer.put(w3, val))
                .setTooltip(Text.translatable("champions.tooltip.tier3_weight"))
                .build());

        tiersWeight.add(entryBuilder.startIntField(Text.translatable("champions.tier4_weight"), config.w4)
                .setDefaultValue(6).setMin(0)
                .setSaveConsumer(val -> changesServer.put(w4, val))
                .setTooltip(Text.translatable("champions.tooltip.tier4_weight"))
                .build());

        tiersWeight.add(entryBuilder.startIntField(Text.translatable("champions.tier5_weight"), config.w5)
                .setDefaultValue(2).setMin(0)
                .setSaveConsumer(val -> changesServer.put(w5, val))
                .setTooltip(Text.translatable("champions.tooltip.tier5_weight"))
                .build());

        server.addEntry(tiersWeight.build());

        var tiersGh = entryBuilder.startSubCategory(Text.translatable("champions.hud.tiers_gh"))
                .setExpanded(true);

        final String gh1 = "tier1_growth_health";
        final String gh2 = "tier2_growth_health";
        final String gh3 = "tier3_growth_health";
        final String gh4 = "tier4_growth_health";
        final String gh5 = "tier5_growth_health";

        tiersGh.add(entryBuilder.startFloatField(Text.translatable("tier1_growth_health"), config.gh1)
                .setDefaultValue(1.5F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gh1, val))
                .setTooltip(Text.translatable("champions.tooltip.tier1_growth_health"))
                .build());

        tiersGh.add(entryBuilder.startFloatField(Text.translatable("tier2_growth_health"), config.gh2)
                .setDefaultValue(2.5F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gh2, val))
                .setTooltip(Text.translatable("champions.tooltip.tier2_growth_health"))
                .build());

        tiersGh.add(entryBuilder.startFloatField(Text.translatable("tier3_growth_health"), config.gh3)
                .setDefaultValue(4F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gh3, val))
                .setTooltip(Text.translatable("champions.tooltip.tier3_growth_health"))
                .build());

        tiersGh.add(entryBuilder.startFloatField(Text.translatable("tier4_growth_health"), config.gh4)
                .setDefaultValue(7F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gh4, val))
                .setTooltip(Text.translatable("champions.tooltip.tier4_growth_health"))
                .build());

        tiersGh.add(entryBuilder.startFloatField(Text.translatable("tier5_growth_health"), config.gh5)
                .setDefaultValue(12F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gh5, val))
                .setTooltip(Text.translatable("champions.tooltip.tier5_growth_health"))
                .build());

        server.addEntry(tiersGh.build());

        var tiersGs = entryBuilder.startSubCategory(Text.translatable("champions.hud.tiers_gs"))
                .setExpanded(true);

        final String gs1 = "tier1_growth_strength";
        final String gs2 = "tier2_growth_strength";
        final String gs3 = "tier3_growth_strength";
        final String gs4 = "tier4_growth_strength";
        final String gs5 = "tier5_growth_strength";

        tiersGs.add(entryBuilder.startFloatField(Text.translatable("tier1_growth_strength"), config.gs1)
                .setDefaultValue(1.5F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gs1, val))
                .setTooltip(Text.translatable("champions.tooltip.tier1_growth_strength"))
                .build());

        tiersGs.add(entryBuilder.startFloatField(Text.translatable("tier2_growth_strength"), config.gs2)
                .setDefaultValue(1.8F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gs2, val))
                .setTooltip(Text.translatable("champions.tooltip.tier2_growth_strength"))
                .build());

        tiersGs.add(entryBuilder.startFloatField(Text.translatable("tier3_growth_strength"), config.gs3)
                .setDefaultValue(2.2F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gs3, val))
                .setTooltip(Text.translatable("champions.tooltip.tier3_growth_strength"))
                .build());

        tiersGs.add(entryBuilder.startFloatField(Text.translatable("tier4_growth_strength"), config.gs4)
                .setDefaultValue(3.5F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gs4, val))
                .setTooltip(Text.translatable("champions.tooltip.tier4_growth_strength"))
                .build());

        tiersGs.add(entryBuilder.startFloatField(Text.translatable("tier5_growth_strength"), config.gs5)
                .setDefaultValue(5F).setMin(0)
                .setSaveConsumer(val -> changesServer.put(gs5, val))
                .setTooltip(Text.translatable("champions.tooltip.tier5_growth_strength"))
                .build());

        server.addEntry(tiersGs.build());

        var tiersA = entryBuilder.startSubCategory(Text.translatable("champions.hud.tiers_a"))
                .setExpanded(true);

        final String a1 = "tier1_affixes_count";
        final String a2 = "tier2_affixes_count";
        final String a3 = "tier3_affixes_count";
        final String a4 = "tier4_affixes_count";
        final String a5 = "tier5_affixes_count";


        tiersA.add(entryBuilder.startIntField(Text.translatable("champions.tier1_affixes_count"), config.a1)
                .setDefaultValue(1).setMin(0).setMax(30)
                .setSaveConsumer(val -> changesServer.put(a1, val))
                .setTooltip(Text.translatable("champions.tooltip.tier1_affixes_count"))
                .build());

        tiersA.add(entryBuilder.startIntField(Text.translatable("champions.tier2_affixes_count"), config.a2)
                .setDefaultValue(2).setMin(0).setMax(30)
                .setSaveConsumer(val -> changesServer.put(a2, val))
                .setTooltip(Text.translatable("champions.tooltip.tier2_affixes_count"))
                .build());

        tiersA.add(entryBuilder.startIntField(Text.translatable("champions.tier3_affixes_count"), config.a3)
                .setDefaultValue(3).setMin(0).setMax(30)
                .setSaveConsumer(val -> changesServer.put(a3, val))
                .setTooltip(Text.translatable("champions.tooltip.tier3_affixes_count"))
                .build());

        tiersA.add(entryBuilder.startIntField(Text.translatable("champions.tier4_affixes_count"), config.a4)
                .setDefaultValue(4).setMin(0).setMax(30)
                .setSaveConsumer(val -> changesServer.put(a4, val))
                .setTooltip(Text.translatable("champions.tooltip.tier4_affixes_count"))
                .build());

        tiersA.add(entryBuilder.startIntField(Text.translatable("champions.tier5_affixes_count"), config.a5)
                .setDefaultValue(8).setMin(0).setMax(30)
                .setSaveConsumer(val -> changesServer.put(a5, val))
                .setTooltip(Text.translatable("champions.tooltip.tier5_affixes_count"))
                .build());

        server.addEntry(tiersA.build());


        builder.setSavingRunnable(() -> {
            ChampionsConfigClient.save(changes);
            ChampionsConfigClient.reload();
            ChampionsConfigServer.save(changesServer);
            ChampionsConfigServer.reload();
        });
        return builder.build();
    }

    private int parseColor(String hex) {
        try {
            if (hex.startsWith("#")) hex = hex.substring(1);
            return Integer.parseInt(hex, 16);
        } catch (Exception e) {
            return 0xFFFFFF;
        }
    }
}
