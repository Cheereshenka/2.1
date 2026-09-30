package edu.labs;
import org.junit.jupiter.api.Test;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;
class GameTest {
    private Game game(double roll) { return new Game(new Player("Тест", 3), new ActionChain(new Random() { @Override public double nextDouble() { return roll; } })); }
    @Test void winningsCanBeReinvestedThenCashedOut() {
        Game g = game(0); g.startRound(); g.choose(ActionChain.Strategy.RETAIN);
        assertEquals(2, g.stake()); g.risk(); g.choose(ActionChain.Strategy.RETAIN); g.cashOut();
        assertEquals(6, g.player().coins()); assertEquals(1, g.player().paidRounds()); assertEquals(Game.State.FINISHED, g.state());
        assertThrows(IllegalStateException.class, g::startRound);
    }
    @Test void lossConsumesOnlyStake() {
        Game g = game(0.99); g.startRound(); g.choose(ActionChain.Strategy.TAKE);
        assertEquals(0, g.stake()); assertEquals(2, g.player().coins()); assertEquals(Game.State.READY, g.state());
    }
    @Test void chanceDoesNotChargeAgain() {
        Game g = game(0.60); g.startRound(); g.choose(ActionChain.Strategy.RETAIN);
        assertEquals(Game.State.CHOOSE, g.state()); assertEquals(1, g.stake()); assertEquals(2, g.player().coins());
    }
    @Test void invalidRequestsAndDoublePaymentAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new ActionChain().process(0));
        Game g = game(0); g.startRound(); assertThrows(IllegalStateException.class, g::startRound);
        assertFalse(new Player("Тест", 0).pay(1));
    }
}
