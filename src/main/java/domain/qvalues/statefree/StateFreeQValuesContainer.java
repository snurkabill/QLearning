package domain.qvalues.statefree;

import domain.Action;
import domain.State;
import domain.qvalues.QValuesContainer;
import domain.qvalues.statefree.generalization.FeatureBasedStateEvaluator;

public class StateFreeQValuesContainer extends QValuesContainer {

    private final FeatureBasedStateEvaluator featureBasedStateEvaluator;
    private final FeatureBasedStateEvaluator shiftedFeatureBasedStateEvaluator;

    public StateFreeQValuesContainer(Action[] actions, FeatureBasedStateEvaluator featureBasedStateEvaluator,
                                     FeatureBasedStateEvaluator shiftedFeatureBasedStateEvaluator) {
        super(actions);
        this.featureBasedStateEvaluator = featureBasedStateEvaluator;
        this.shiftedFeatureBasedStateEvaluator = shiftedFeatureBasedStateEvaluator;
    }

    @Override
    public double getQ(State state, Action action, Approximator approximator) {
        if(approximator == Approximator.NEW) {
            return featureBasedStateEvaluator.getQ(state, action);
        } else {
            return shiftedFeatureBasedStateEvaluator.getQ(state, action);
        }
    }

    @Override
    public void setQ(State oldState, Action action, double learningRate, double oldValue, double learnedValue) {
        double difference = learnedValue - oldValue;
//        double difference = learnedValue;
        shiftedFeatureBasedStateEvaluator.pasteWeights(featureBasedStateEvaluator.getWeights());
        featureBasedStateEvaluator.applyTemporalDifference(oldState, action, difference, learningRate);
    }
}
