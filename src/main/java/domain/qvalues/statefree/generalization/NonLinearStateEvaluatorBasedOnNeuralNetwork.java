package domain.qvalues.statefree.generalization;

import domain.Action;
import domain.State;
import domain.qvalues.statefree.features.Feature;
import net.snurkabill.neuralnetworks.neuralnetwork.feedforward.backpropagative.impl.online.OnlineFeedForwardNetwork;

import java.util.List;

public class NonLinearStateEvaluatorBasedOnNeuralNetwork extends FeatureBasedStateEvaluator {

    private final OnlineFeedForwardNetwork neuralNetwork;

    public NonLinearStateEvaluatorBasedOnNeuralNetwork(List<Feature> features,
                                                          OnlineFeedForwardNetwork neuralNetwork) {
        super(features);
        this.neuralNetwork = neuralNetwork;
    }

    public OnlineFeedForwardNetwork getNeuralNetwork() {
        return neuralNetwork;
    }

    @Override
    public double getQ(State state, Action action) {
        double[] inputVector = new double[features.size()];
        for (int i = 0; i < inputVector.length; i++) {
            inputVector[i] = features.get(i).calculateFeature(state, action);
        }
        neuralNetwork.calculateNetwork(inputVector);
        return neuralNetwork.getOutputValues()[0];
    }

    @Override
    public void applyTemporalDifference(State oldState, Action action, double difference, double learningRate) {
        neuralNetwork.trainNetwork(new double[] {difference});
    }
}
