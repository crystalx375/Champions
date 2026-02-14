package crystal.champions.rank;

import net.minecraft.util.math.random.Random;

import java.util.List;

public record ChampionRank(int tier, int affixes, int weight, float growth) {
    public static final List<ChampionRank> RANKS = List.of(
            new ChampionRank(0, 0, 0, 1.0f),
            new ChampionRank(1, 1, 3000, 1.5f),
            new ChampionRank(2, 2, 150, 3.0f),
            new ChampionRank(3, 3, 45, 6.0f),
            new ChampionRank(4, 4, 4, 12.0f),
            new ChampionRank(5, 8, 1, 30.0f)
    );

    public static ChampionRank getRandomRank(Random random) {
        int totalWeight = 0;
        for (ChampionRank rank : RANKS) {totalWeight += rank.weight();}
        int roll = random.nextInt(totalWeight);
        for (ChampionRank rank : RANKS) {
            if (roll < rank.weight()) return rank;
            roll -= rank.weight();
        }
        System.out.println(RANKS);
        return RANKS.get(0);
    }
}