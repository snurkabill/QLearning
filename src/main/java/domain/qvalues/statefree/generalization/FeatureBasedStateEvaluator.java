package domain.qvalues.statefree.generalization;

import domain.Action;
import domain.State;
import domain.qvalues.statefree.features.Feature;

import java.util.List;

public abstract class FeatureBasedStateEvaluator {

    protected final List<Feature> features;

    protected FeatureBasedStateEvaluator(List<Feature> features) {
        this.features = features;
    }

    public abstract Object getWeights();

    public abstract void pasteWeights(Object weights);

    public abstract double getQ(State state, Action action);

    public abstract void applyTemporalDifference(State oldState, Action action, double difference, double learningRate);
}
