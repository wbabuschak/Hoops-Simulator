public enum Archetype {
    UNSHAKEABLE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Contested Rim Finishing") +
                player.getAttributeValue("Contested Midrange") +
                player.getAttributeValue("Contested 3pt") +
                player.getAttributeValue("Offensive Consistency");
        }
    },
    THREE_AND_D {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Perimeter D") +
                player.getAttributeValue("3pt") +
                player.getAttributeValue("Contested 3pt") +
                player.getAttributeValue("Defensive Consistency");
        }
    },
    SNIPER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Midrange") +
            player.getAttributeValue("Free Throw") +
            player.getAttributeValue("3pt") +
            player.getAttributeValue("Contested 3pt");
        }
    },
    DEFENSIVE_MENACE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Defensive Discipline") +
                player.getAttributeValue("Hard Fouls") +
                player.getAttributeValue("Perimeter D") +
                player.getAttributeValue("Paint D");
        }
    },
    POINT_GOD {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Offensive Consistency") +
            player.getAttributeValue("Dribbling") +
            player.getAttributeValue("Passing") +
            player.getAttributeValue("Midrange");
        }
    },
    PLAY_FINISHER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("3pt") +
            player.getAttributeValue("Contested 3pt") +
            player.getAttributeValue("Rim Finishing") +
            player.getAttributeValue("Contested Rim Finishing");
        }
    },
    BLUE_COLLAR_BALLER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Defensive Rebounding") +
            player.getAttributeValue("Offensive Rebounding") +
            player.getAttributeValue("Hard Fouls") +
            player.getAttributeValue("Stamina");
        }
    },
    THE_SYSTEM {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Stamina") +
            player.getAttributeValue("Dribbling") +
            player.getAttributeValue("Passing") +
            player.getAttributeValue("3pt");
        }
    },
    STAT_SHEET_STUFFER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Steals") +
            player.getAttributeValue("Blocks") +
            player.getAttributeValue("Passing") +
            player.getAttributeValue("Defensive Rebounding");
        }
    },
    THREE_LEVEL_SCORER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("3pt") +
                player.getAttributeValue("Midrange") +
                player.getAttributeValue("Rim Finishing") +
                player.getAttributeValue("Free Throw");
        }
    };

    public abstract double score(Player p);

    public static Archetype findArchetype(Player p) {
        Archetype best = null;
        double bestScore = Double.NEGATIVE_INFINITY;

        for (Archetype a : Archetype.values()) {
            double score = a.score(p);
            if (score > bestScore) {
                bestScore = score;
                best = a;
            }
        }

        return best;
    }
}


