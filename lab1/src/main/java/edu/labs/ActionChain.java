package edu.labs;

import java.util.random.RandomGenerator;

public final class ActionChain {
    public static final int WIN = 1, LOSS = 2, CHANCE = 3;
    public enum Strategy {
        RETAIN("Удержать"), TAKE("Обобрать"), HOPE("Дать надежду");
        private final String title;
        Strategy(String title) { this.title = title; }
        @Override public String toString() { return title; }
    }
    private final Handler chain = new LossHandler(new WinHandler(new ChanceHandler(null)));
    private final RandomGenerator random;
    public ActionChain() { this(RandomGenerator.getDefault()); }
    public ActionChain(RandomGenerator random) { this.random = random; }
    public Outcome process(Integer type) { return chain.process(type); }
    public Outcome draw(Strategy strategy, int round) {
        double winChance = switch (strategy) {
            case RETAIN -> round < 4 ? 0.55 : 0.35;
            case TAKE -> 0.12;
            case HOPE -> round % 3 == 1 ? 0.70 : 0.15;
        };
        double value = random.nextDouble();
        return process(value < winChance ? WIN : value < winChance + 0.15 ? CHANCE : LOSS);
    }
}
