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
    public double[][][] getWeights() {
        return neuralNetwork.getWeights();
    }

    @Override
    public void pasteWeights(Object weights) {
        if(weights instanceof double[][][]) {
            double[][][] weights_ = (double[][][]) weights;
            double[][][] original = neuralNetwork.getWeights();
            for (int i = 0; i < original.length; i++) {
                for (int j = 0; j < original[i].length; j++) {
                    for (int k = 0; k < original[i][j].length; k++) {
                        original[i][j][k] = weights_[i][j][k];
                    }
                }
            }
        } else {
            throw new IllegalStateException("....");
        }
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
//        QLearning.LOGGER.info("{}, {}", neuralNetwork.getOutputValues()[0], difference);
//        QLearning.LOGGER.info("difference: {}", difference);
        neuralNetwork.trainNetwork(new double[] {difference});
    }
}
