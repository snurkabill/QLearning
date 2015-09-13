package examples.statebasedtrading.domain;

import domain.State;

import java.util.Arrays;

public class StateBasedTradingState extends State {

    private static final double RELEVANT_DECIMAL = 10;
    private static final double TOLERANCE = Math.pow(10, -RELEVANT_DECIMAL);
    private static final long MULTIPLYING_CAFF = (long) Math.pow(10, RELEVANT_DECIMAL);

    private static int resolveMoneyLevel(double money) {
        if(money < 0.0) {
            throw new IllegalStateException("There can't be negative money");
        }
        for (int i = 0; ; i++) {
            if(money < Math.pow(2, i)) {
                return i;
            }
        }
    }

    private static int resolveCommodityLevel(int commodity) {
        if(commodity < 0) {
            throw new IllegalStateException("There can't be negative commodity");
        }
        for (int i = 0; ; i++) {
            if(commodity < Math.pow(2, i)) {
                return i;
            }
        }
    }

    private static int resolveBuyableLevel(double money, double price) {
        if(money < 0.0) {
            throw new IllegalStateException("There can't be negative money");
        }
        if(price <= 0.0) {
            throw new IllegalStateException("Price must be higher than 0.0");
        }
        double possibleVolume = money / price;
        for (int i = 0;  ; i++) {
            if(possibleVolume < Math.pow(2, i)) {
                return i;
            }
        }
    }

    private final boolean[] networkResults;
    private final int commodity;
    private final double money;
    private final double price;
    private final int moneyLevel;
    private final int commodityLevel;
    private final int buyableLevel;

    public StateBasedTradingState(boolean[] networkResults, int commodity, double money, double price) {
        this.networkResults = networkResults;
        this.commodity = commodity;
        this.money = money;
        this.price = price;
        this.moneyLevel = resolveMoneyLevel(money);
        this.commodityLevel = resolveCommodityLevel(commodity);
        this.buyableLevel = resolveBuyableLevel(money, price);
    }

    public boolean[] getNetworkResults() {
        return networkResults;
    }

    public int getCommodity() {
        return commodity;
    }

    public double getMoney() {
        return money;
    }

    public double getPrice() {
        return price;
    }

    public int getMoneyLevel() {
        return moneyLevel;
    }

    public int getCommodityLevel() {
        return commodityLevel;
    }

    public int getBuyableLevel() {
        return buyableLevel;
    }

    @Override
    public boolean areEqual(State other) {
        if(this == other) {
            return true;
        }
        if(other == null || getClass() != other.getClass()) {
            return false;
        }
        StateBasedTradingState that = (StateBasedTradingState) other;
        if(!Arrays.equals(networkResults, that.networkResults)) {
            return false;
        }
        if(moneyLevel != that.moneyLevel) {
            return false;
        }
        if(commodityLevel != that.commodityLevel) {
            return false;
        }
        if(buyableLevel != that.buyableLevel) {
            return false;
        }
        return true;
    }

    @Override
    public Long createHashCode() {
        Long value = (long) moneyLevel;
        value = value * 461 + commodityLevel;
        value = value * 647 + buyableLevel;
        value = value * 997 + Arrays.hashCode(networkResults);
        return value;
    }

    @Override
    public State copy() {
        return new StateBasedTradingState(
                Arrays.copyOf(networkResults, networkResults.length), commodity, money, price);
    }
}
