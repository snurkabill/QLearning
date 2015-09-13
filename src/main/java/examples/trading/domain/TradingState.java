package examples.trading.domain;

import domain.State;

import java.util.Arrays;

public class TradingState extends State {

    private static final double RELEVANT_DECIMAL = 10;
    private static final double TOLERANCE = Math.pow(10, -RELEVANT_DECIMAL);
    private static final long MULTIPLYING_CAFF = (long) Math.pow(10, RELEVANT_DECIMAL);

    private double money;
    private int commodity;
    private double[] deltaPrice;
    private double lastPrice;

    public TradingState(double money, int commodity, double[] deltaPrice, double lastPrice) {
        this.money = money;
        this.commodity = commodity;
        this.deltaPrice = deltaPrice;
        this.lastPrice = lastPrice;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCommodity() {
        return commodity;
    }

    public void setCommodity(int commodity) {
        this.commodity = commodity;
    }

    public double[] getDeltaPrice() {
        return deltaPrice;
    }

    public void setDeltaPrice(double[] deltaPrice) {
        this.deltaPrice = deltaPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    @Override
    public boolean areEqual(State other) {
        if(this == other) {
            return true;
        }
        if(other == null || getClass() != other.getClass()) {
            return false;
        }
        TradingState that = (TradingState) other;

        if(Math.abs(money - that.money) > TOLERANCE) {
            return false;
        }
        if(commodity != that.commodity) {
            return false;
        }
        if(deltaPrice.length != that.deltaPrice.length) {
            return false;
        }
        for (int i = 0; i < deltaPrice.length; i++) {
            if(Math.abs(deltaPrice[i] - that.deltaPrice[i]) > TOLERANCE) {
                return false;
            }
        }
        if(Math.abs(lastPrice - that.lastPrice) > TOLERANCE) {
            return false;
        }
        return true;
    }

    @Override
    public Long createHashCode() {
        Long value = (long) money * MULTIPLYING_CAFF;
        value = value * 31 + commodity;
        value = value * 47 + (long) lastPrice * MULTIPLYING_CAFF;
        value = value * 53 + Arrays.hashCode(deltaPrice);
        return value;
    }

    @Override
    public State copy() {
        return new TradingState(money, commodity, Arrays.copyOf(deltaPrice, deltaPrice.length), lastPrice);
    }
}
