public enum Archetype {
    THREE_AND_D {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("3pt") +
                   player.getAttributeValue("Contested 3pt") +
                   player.getAttributeValue("Perimeter D");
        }
    },
    POINT_GOD {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Passing") +
                   player.getAttributeValue("Dribbling") +
                   player.getAttributeValue("Offensive Discipline");
        }
    },
    THE_SYSTEM {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Passing") +
                   player.getAttributeValue("Dribbling") +
                   player.getAttributeValue("3pt");
        }
    },
    BRUISER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Defensive Rebounding") +
                   player.getAttributeValue("Hard Fouls") +
                   player.getAttributeValue("Paint D");
        }
    },
    SKULL_BASHER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Offensive Rebounding") +
                   player.getAttributeValue("Rim Finishing") +
                   player.getAttributeValue("Contested Rim Finishing");
        }
    },
    DEFENDER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Perimeter D") +
                   player.getAttributeValue("Paint D") +
                   player.getAttributeValue("Defensive Discipline");
        }
    },
    UNSHAKEABLE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Contested Rim Finishing") +
                   player.getAttributeValue("Contested Midrange") +
                   player.getAttributeValue("Contested 3pt");
        }
    },
    SNIPER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Free Throw") +
                   player.getAttributeValue("3pt") +
                   player.getAttributeValue("Contested 3pt");
        }
    },
    MR_FUNDAMENTAL {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Free Throw") +
                   player.getAttributeValue("Offensive Discipline") +
                   player.getAttributeValue("Defensive Discipline");
        }
    },
    PACEMAKER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Pace") +
                   player.getAttributeValue("Passing") +
                   player.getAttributeValue("Stamina");
        }
    },
    CANNONBALL {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Rim Finishing") +
                   player.getAttributeValue("Dribbling") +
                   player.getAttributeValue("Pace");
        }
    },
    MENACE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Steals") +
                   player.getAttributeValue("Defensive Rebounding") +
                   player.getAttributeValue("Hard Fouls");
        }
    },
    PURE_ATHLETE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Stamina") +
                   player.getAttributeValue("Pace") +
                   player.getAttributeValue("Rim Finishing");
        }
    },
    PAINT_DEMON {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Rim Finishing") +
                   player.getAttributeValue("Contested Rim Finishing") +
                   player.getAttributeValue("Offensive Discipline");
        }
    },
    MR_TRIPLE_DOUBLE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Passing") +
                   player.getAttributeValue("Defensive Rebounding") +
                   player.getAttributeValue("Stamina");
        }
    },
    CLUTCH_SHOOTER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Contested 3pt") +
                   player.getAttributeValue("Offensive Discipline") +
                   player.getAttributeValue("Free Throw");
        }
    },
    WIZARD {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Dribbling") +
                   player.getAttributeValue("Offensive Discipline") +
                   player.getAttributeValue("Rim Finishing");
        }
    },
    THE_GLOVE {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("Steals") +
                   player.getAttributeValue("Defensive Discipline") +
                   player.getAttributeValue("Perimeter D");
        }
    },
    THREE_LEVEL_SCORER {
        @Override
        public double score(Player player) {
            return player.getAttributeValue("3pt") +
                   player.getAttributeValue("Midrange") +
                   player.getAttributeValue("Rim Finishing");
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


