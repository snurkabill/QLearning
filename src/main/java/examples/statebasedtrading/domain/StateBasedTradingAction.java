package examples.statebasedtrading.domain;

import domain.Action;
import domain.State;

import java.util.Arrays;

public class StateBasedTradingAction extends Action {

    public enum Type {
        BUY,
        SELL,
        NOTHING
    }

    private final int volume;
    private final Type type;

    public StateBasedTradingAction(int index, int volume, Type type) {
        super(index);
        this.volume = volume;
        this.type = type;
    }

    public int getVolume() {
        return volume;
    }

    public Type getType() {
        return type;
    }

    @Override
    public State apply(State state) {
        StateBasedTradingState tradingState = (StateBasedTradingState) state;
        if(type == Type.NOTHING) {
            return tradingState.copy();
        }
        double money = tradingState.getMoney();
        int commodity = tradingState.getCommodity();
        double price = tradingState.getPrice();
        boolean[] prediction = tradingState.getNetworkResults();
        if(type == Type.BUY) {
            return new StateBasedTradingState(Arrays.copyOf(prediction, prediction.length), commodity + volume,
                    money - price * volume, price);
        } else if(type == Type.SELL) {
            return new StateBasedTradingState(Arrays.copyOf(prediction, prediction.length), commodity - volume,
                    money + price * volume, price);
        } else {
            throw new IllegalStateException("Don't mess with domain.");
        }
    }

}
