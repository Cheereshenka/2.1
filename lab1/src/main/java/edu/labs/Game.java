package edu.labs;

public final class Game {
    public enum State { READY, CHOOSE, DECIDE, FINISHED }
    private final Player player;
    private final ActionChain chain;
    private State state = State.READY;
    private int stake, rounds;
    public Game(Player player, ActionChain chain) { this.player = player; this.chain = chain; }
    public void startRound() {
        if (state != State.READY) throw new IllegalStateException("Сначала завершите текущий раунд");
        if (!player.pay(1)) throw new IllegalStateException("Вставьте монетку для начала игры");
        stake = 1; state = State.CHOOSE;
    }
    public Outcome choose(ActionChain.Strategy strategy) {
        if (state != State.CHOOSE) throw new IllegalStateException("Сначала начните раунд");
        Outcome result = chain.draw(strategy, ++rounds);
        if (result.type() == ActionChain.WIN) {
            stake = Math.multiplyExact(stake, result.multiplier()); state = State.DECIDE;
        } else if (result.type() == ActionChain.LOSS) {
            stake = 0; state = State.READY;
        }
        return result;
    }
    public void risk() {
        if (state != State.DECIDE) throw new IllegalStateException("Пока нет выигрыша");
        if (stake > Integer.MAX_VALUE / 2) throw new IllegalStateException("Достигнут предел ставки — заберите выигрыш");
        state = State.CHOOSE;
    }
    public void cashOut() {
        if (state != State.DECIDE) throw new IllegalStateException("Пока нет выигрыша");
        player.addCoins(stake); stake = 0; state = State.FINISHED;
    }
    public State state() { return state; }
    public int stake() { return stake; }
    public int rounds() { return rounds; }
    public Player player() { return player; }
}
