package domain.qvalues.statefree.generalization;

import domain.Action;
import domain.State;
import domain.qvalues.statefree.features.Feature;

import java.util.List;

public class LinearStateEvaluator extends FeatureBasedStateEvaluator {

    private final double [] weights;

    public LinearStateEvaluator(List<Feature> features) {
        super(features);
        this.weights = new double[features.size()];
    }

    @Override
    public double[] getWeights() {
        return this.weights;
    }

    @Override
    public void pasteWeights(Object weights) {
        if(weights instanceof double[]) {
            double[] weights_ = (double[]) weights;
            for (int i = 0; i < this.weights.length; i++) {
                this.weights[i] = weights_[i];
            }
        } else {
            throw new IllegalStateException("... ");
        }
    }


    @Override
    public double getQ(State state, Action action) {
        double value = 0.0;
        for (int i = 0; i < weights.length; i++) {
            value += weights[i] * features.get(i).calculateFeature(state, action);
        }
        return value;
    }

    @Override
    public void applyTemporalDifference(State oldState, Action action, double difference, double learningRate) {
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * difference * features.get(i).calculateFeature(oldState, action);
        }
    }
}
