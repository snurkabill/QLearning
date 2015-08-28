package domain.qvalues.statefree.features;

import domain.Action;
import domain.State;

public interface Feature {

    double calculateFeature(State state, Action action);
}
