package crystal.champions.util;

import net.minecraft.util.math.random.Random;

import java.util.List;

import static crystal.champions.config.ChampionsConfigServer.*;

public record ChampionRank(int tier, int affixes, int weight, float growth_h, float growth_s) {
    public static final List<ChampionRank> RANKS = List.of(
            new ChampionRank(0, 0, w0, 1.0f, 1.0f),
            new ChampionRank(1, a1, w1, gh1, gs1),
            new ChampionRank(2, a2, w2, gh2, gs2),
            new ChampionRank(3, a3, w3, gh3, gs3),
            new ChampionRank(4, a4, w4, gh4, gs4),
            new ChampionRank(5, a5, w5, gh5, gs5)
    );
    private static final int TOTAL_WEIGHT;
    static {
        int currentSum = 0;
        for (ChampionRank rank : RANKS) {
            if (rank.weight() > 0) {
                currentSum += rank.weight();
            }
        }
        TOTAL_WEIGHT = currentSum;
    }

    public static ChampionRank getRandomRank(Random random) {
        int roll = random.nextInt(TOTAL_WEIGHT);
        int cumulativeSum = 0;

        for (ChampionRank rank : RANKS) {
            cumulativeSum += rank.weight();
            if (roll <= cumulativeSum) {
                return rank;
            }
        }
        return RANKS.get(0);
    }


    public static ChampionRank getBossRank(Random random) {
        List<ChampionRank> ranks = RANKS.stream()
                .filter(r -> r.tier() <= maxBossTier)
                .toList();
        int totalWeight = 0;
        for (ChampionRank rank : ranks) {
            totalWeight += rank.weight();
        }
        int roll = random.nextInt(totalWeight);
        for (ChampionRank rank : ranks) {
            if (roll < rank.weight()) return rank;
            roll -= rank.weight();
        }
        return ranks.get(0);
    }
}