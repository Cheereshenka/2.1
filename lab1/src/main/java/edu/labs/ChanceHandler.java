package edu.labs;

public final class ChanceHandler extends Handler {
    public ChanceHandler(Handler next) { super(next); }
    @Override public Outcome process(Integer request) {
        if (request == null || request != ActionChain.CHANCE) return super.process(request);
        return new Outcome(ActionChain.CHANCE, 1, "Второй шанс! Выберите мешочек ещё раз бесплатно.");
    }
}
