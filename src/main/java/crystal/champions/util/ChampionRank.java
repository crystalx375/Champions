package crystal.champions.util;

import crystal.champions.config.ChampionsConfigServer;
import net.minecraft.util.math.random.Random;

import java.util.ArrayList;
import java.util.List;

public record ChampionRank(int tier, int affixes, int weight, float growth_h, float growth_s) {

    public static List<ChampionRank> RANKS = new ArrayList<>();
    private static int TOTAL_WEIGHT = 0;

    public static void reload() {
        var cfg = ChampionsConfigServer.get(); // Твой класс конфига
        TOTAL_WEIGHT = 0;
        RANKS = List.of(
                new ChampionRank(0, 0, cfg.w0, 1.0f, 1.0f),
                new ChampionRank(1, cfg.a1, cfg.w1, cfg.gh1, cfg.gs1),
                new ChampionRank(2, cfg.a2, cfg.w2, cfg.gh2, cfg.gs2),
                new ChampionRank(3, cfg.a3, cfg.w3, cfg.gh3, cfg.gs3),
                new ChampionRank(4, cfg.a4, cfg.w4, cfg.gh4, cfg.gs4),
                new ChampionRank(5, cfg.a5, cfg.w5, cfg.gh5, cfg.gs5)
        );

        for (ChampionRank r : RANKS) {
            if (r.weight() > 0) TOTAL_WEIGHT += r.weight();
        }
    }

    public static ChampionRank getRandomRank(Random random) {
        if (TOTAL_WEIGHT <= 0) return RANKS.getFirst();

        int roll = random.nextInt(TOTAL_WEIGHT);
        for (ChampionRank rank : RANKS) {
            if (roll < rank.weight()) return rank;
            roll -= rank.weight();
        }
        return RANKS.getFirst();
    }
}