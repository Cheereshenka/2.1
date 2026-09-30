package edu.labs;

public final class WinHandler extends Handler {
    public WinHandler(Handler next) { super(next); }
    @Override public Outcome process(Integer request) {
        if (request == null || request != ActionChain.WIN) return super.process(request);
        return new Outcome(ActionChain.WIN, 2, "В мешочке монеты! Выигрыш удвоен.");
    }
}
