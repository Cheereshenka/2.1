package edu.labs;

public final class Player {
    private final String name;
    private int coins;
    private int paidRounds;
    public Player(String name, int coins) {
        if (name == null || name.isBlank() || coins < 0) throw new IllegalArgumentException("Укажите имя и неотрицательный баланс");
        this.name = name.trim(); this.coins = coins;
    }
    public boolean pay(int amount) {
        if (amount <= 0) throw new IllegalArgumentException("Ставка должна быть положительной");
        if (coins < amount) return false;
        coins -= amount; paidRounds++; return true;
    }
    public void addCoins(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Нельзя добавить отрицательное число монет");
        coins = Math.addExact(coins, amount);
    }
    public String name() { return name; }
    public int coins() { return coins; }
    public int paidRounds() { return paidRounds; }
}
