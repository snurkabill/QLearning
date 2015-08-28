package domain.qvalues.statefree.features;

import domain.Action;
import domain.State;

public class BiasFeature implements Feature {

    @Override
    public double calculateFeature(State state, Action action) {
        return 1;
    }
}
