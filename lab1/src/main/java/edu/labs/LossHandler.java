package edu.labs;

public final class LossHandler extends Handler {
    public LossHandler(Handler next) { super(next); }
    @Override public Outcome process(Integer request) {
        if (request == null || request != ActionChain.LOSS) return super.process(request);
        return new Outcome(ActionChain.LOSS, 0, "Мешочек пуст. Ставка потеряна.");
    }
}
