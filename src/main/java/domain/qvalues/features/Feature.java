package domain.qvalues.features;

import domain.Action;
import domain.State;

public interface Feature {

    double calculateFeature(State state, Action action);
}
