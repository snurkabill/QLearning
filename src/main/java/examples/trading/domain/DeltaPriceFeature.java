package examples.trading.domain;

import domain.Action;
import domain.State;
import domain.qvalues.statefree.features.Feature;

public class DeltaPriceFeature implements Feature {

    private final int index;

    public DeltaPriceFeature(int index) {
        this.index = index;
    }

    @Override
    public double calculateFeature(State state, Action action) {
        return ((TradingState)action.createNextState(state)).getDeltaPrice()[index] - 1;
    }
}
