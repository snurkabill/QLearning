package examples.trading.domain;

import domain.Action;
import domain.State;

public class TradingAction extends Action {

    public TradingAction(int index) {
        super(index);
    }

    @Override
    public void apply(State state) {
        TradingState tradingState = (TradingState) state;
        if(index == 0) {
            return;
        }
        double money = tradingState.getMoney();
        int commodity = tradingState.getCommodity();
        double lastPrice = tradingState.getLastPrice();
        if(index == 1) {
            tradingState.setMoney(money - lastPrice);
            tradingState.setCommodity(++commodity);
        } else if(index == 2) {
            tradingState.setMoney(money + lastPrice);
            tradingState.setCommodity(--commodity);
        }
    }

    @Override
    public State createNextState(State state) {
        State newState = state.copy();
        this.apply(newState);
        return newState;
    }
}
