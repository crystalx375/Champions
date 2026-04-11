package crystal.champions.util;

import net.minecraft.util.math.random.Random;

import java.util.List;

import static crystal.champions.config.ChampionsConfigServer.get;

public record ChampionRank(int tier, int affixes, int weight, float growth_h, float growth_s) {

    public static final List<ChampionRank> RANKS = List.of(
            new ChampionRank(0, 0, get().w0, 1.0f, 1.0f),
            new ChampionRank(1, get().a1, get().w1, get().gh1, get().gs1),
            new ChampionRank(2, get().a2, get().w2, get().gh2, get().gs2),
            new ChampionRank(3, get().a3, get().w3, get().gh3, get().gs3),
            new ChampionRank(4, get().a4, get().w4, get().gh4, get().gs4),
            new ChampionRank(5, get().a5, get().w5, get().gh5, get().gs5)
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
        return RANKS.getFirst();
    }


    public static ChampionRank getBossRank(Random random) {
        List<ChampionRank> ranks = RANKS.stream()
                .filter(r -> r.tier() <= get().maxBossTier)
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
        return ranks.getFirst();
    }
}