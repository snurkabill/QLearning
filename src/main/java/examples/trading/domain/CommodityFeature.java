package examples.trading.domain;

import domain.Action;
import domain.State;
import domain.qvalues.features.Feature;

public class CommodityFeature implements Feature {

    @Override
    public double calculateFeature(State state, Action action) {
        return ((TradingState)action.createNextState(state)).getCommodity();
    }
}
