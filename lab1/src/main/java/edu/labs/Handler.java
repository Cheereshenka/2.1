package edu.labs;

/** Звено цепочки. Неизвестный запрос передаётся следующему обработчику. */
public abstract class Handler {
    private final Handler processor;
    protected Handler(Handler processor) { this.processor = processor; }
    public Outcome process(Integer request) {
        if (processor != null) return processor.process(request);
        throw new IllegalArgumentException("Неизвестный тип розыгрыша: " + request);
    }
}
