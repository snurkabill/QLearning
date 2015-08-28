package domain.qvalues;

import domain.Action;
import domain.State;
import domain.qvalues.features.Feature;

import java.util.List;

public class StateFreeQValuesContainer extends QValuesContainer {

    private final List<Feature> features;
    private final double[] weights;

    public StateFreeQValuesContainer(Action[] actions, List<Feature> features) {
        super(actions);
        this.features = features;
        this.weights = new double[features.size()];
    }

    @Override
    public double getQ(State state, Action action) {
        double sum = 0.0;
        for (int i = 0; i < features.size(); i++) {
            sum += weights[i] * features.get(i).calculateFeature(state, action);
        }
        return sum;
    }

    @Override
    public void setQ(State oldState, Action action, double learningRate, double oldValue, double learnedValue) {
        double difference = learnedValue - oldValue;
        for (int i = 0; i < weights.length; i++) {
            weights[i] += learningRate * difference * features.get(i).calculateFeature(oldState, action);
        }
    }
}
