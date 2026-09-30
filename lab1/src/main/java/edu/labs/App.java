package edu.labs;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class App extends Application {
    private Game game;
    private final Label balance = Ui.label("", "metric"), stake = Ui.label("", "metric"), rounds = Ui.label("", "metric");
    private final Label status = new Label("Создайте игрока и начните игру");
    private final Label message = Ui.label("Три мешочка. Один выбор.", "section");
    private final ListView<String> history = new ListView<>();
    private final ComboBox<ActionChain.Strategy> strategy = new ComboBox<>();
    private final Button[] bags = new Button[3];
    private Button start, risk, cash;
        @Override public void start(Stage stage) {
            history.setPlaceholder(Ui.label("Здесь появится история розыгрышей", "muted"));
        TextField name = new TextField("Игрок"); name.setPromptText("Имя игрока");
        strategy.getItems().setAll(ActionChain.Strategy.values()); strategy.setValue(ActionChain.Strategy.RETAIN);
        Button create = Ui.button("Новая игра", () -> {
            if (game != null && game.stake() > 0) throw new IllegalStateException("Завершите текущую ставку перед новой игрой");
            game = new Game(new Player(name.getText(), 3), new ActionChain()); history.getItems().clear();
            message.setText("Внесите ставку и выберите мешочек"); update();
        });
        Button coin = Ui.button("+ Вставить монетку", () -> { requireGame(); game.player().addCoins(1); update(); });
        start = Ui.primary("Играть · 1 монета", () -> { requireGame(); game.startRound(); message.setText("Выберите любой мешочек"); update(); });
        risk = Ui.primary("Играть на выигрыш", () -> { game.risk(); message.setText("На кону весь выигрыш. Выберите мешочек"); update(); });
        cash = Ui.button("Забрать и закончить", () -> { int value = game.stake(); game.cashOut(); message.setText("Вы забрали " + value + " монет. Игра завершена!"); update(); });
        HBox bagRow = new HBox(20); bagRow.setAlignment(Pos.CENTER);
        for (int i = 0; i < 3; i++) {
            final int index = i + 1;
            bags[i] = Ui.button("Мешочек " + index, () -> {
                Outcome result = game.choose(strategy.getValue()); message.setText(result.message());
                history.getItems().add(0, "Раунд " + game.rounds() + " · Мешочек " + index + " · " + result.message()); update();
            });
            bags[i].getStyleClass().add("bag"); bagRow.getChildren().add(bags[i]);
        }
        HBox metrics = new HBox(16, Ui.card(new Label("Кошелёк"), balance), Ui.card(new Label("На кону"), stake), Ui.card(new Label("Раундов"), rounds));
        metrics.getChildren().forEach(n -> HBox.setHgrow(n, Priority.ALWAYS));
        VBox play = Ui.card(message, new Label("Все три мешочка обрабатываются одной цепочкой событий."), bagRow, new HBox(12, start, risk, cash));
        VBox left = new VBox(18, metrics, play, history); VBox.setVgrow(history, Priority.ALWAYS); HBox.setHgrow(left, Priority.ALWAYS);
        VBox right = Ui.card(Ui.label("Игровая сессия", "section"), Ui.field("Имя", name), create, coin, new Separator(), Ui.field("Стратегия автомата", strategy), Ui.label("Учебная симуляция: исход зависит от стратегии и номера раунда, а не от выбранного мешочка. Монеты виртуальные.", "muted"));
        right.setPrefWidth(270); right.setMinWidth(250);
        Ui.show(stage, "Три мешочка", "Лабораторная 01 / Цепочка обязанностей", new HBox(20, left, right), status);
        create.fire();
    }
    private void requireGame() { if (game == null) throw new IllegalStateException("Сначала создайте игрока"); }
    private void update() {
        balance.setText(game.player().coins() + " мон."); stake.setText(game.stake() + " мон."); rounds.setText("" + game.rounds());
        for (Button bag : bags) bag.setDisable(game.state() != Game.State.CHOOSE);
        start.setDisable(game.state() != Game.State.READY); risk.setDisable(game.state() != Game.State.DECIDE); cash.setDisable(game.state() != Game.State.DECIDE);
        strategy.setDisable(game.state() == Game.State.CHOOSE || game.state() == Game.State.DECIDE);
        status.setText(game.player().name() + " · Оплаченных раундов: " + game.player().paidRounds());
    }
    public static void main(String[] args) { launch(args); }
}
