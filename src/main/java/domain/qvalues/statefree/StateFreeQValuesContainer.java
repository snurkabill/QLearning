package domain.qvalues.statefree;

import domain.Action;
import domain.State;
import domain.qvalues.QValuesContainer;
import domain.qvalues.statefree.generalization.FeatureBasedStateEvaluator;

public class StateFreeQValuesContainer extends QValuesContainer {

    private final FeatureBasedStateEvaluator featureBasedStateEvaluator;

    public StateFreeQValuesContainer(Action[] actions, FeatureBasedStateEvaluator featureBasedStateEvaluator) {
        super(actions);
        this.featureBasedStateEvaluator = featureBasedStateEvaluator;
    }

    @Override
    public double getQ(State state, Action action) {
        return featureBasedStateEvaluator.getQ(state, action);
    }

    @Override
    public void setQ(State oldState, Action action, double learningRate, double oldValue, double learnedValue) {
        double difference = learnedValue - oldValue;
        if(learningRate != 0.0) {
            featureBasedStateEvaluator.applyTemporalDifference(oldState, action, difference, learningRate);
        }
    }
}
