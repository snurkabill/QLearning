package examples.statebasedtrading;

import domain.Action;
import domain.QLearning;
import domain.State;
import domain.qvalues.statefree.generalization.FeatureBasedStateEvaluator;
import net.snurkabill.neuralnetworks.neuralnetwork.feedforward.backpropagative.impl.online.OnlineFeedForwardNetwork;

import java.util.List;
import java.util.Random;

public class StateBasedTrading extends QLearning {

    private final OnlineFeedForwardNetwork neuralNetwrok;

    public StateBasedTrading(List<Action> actions, long randomSeed, double learningRate, double discountFactor,
                             double randomFactor, FeatureBasedStateEvaluator featureBasedStateEvaluator,
                             FeatureBasedStateEvaluator shiftedFeatureBasedStateEvaluator,
                             OnlineFeedForwardNetwork neuralNetwrok) {
        super(actions, randomSeed, learningRate, discountFactor, randomFactor, featureBasedStateEvaluator, shiftedFeatureBasedStateEvaluator);
        this.neuralNetwrok = neuralNetwrok;
    }

    @Override
    public State createState() {
        return ;
    }

    @Override
    protected boolean isFinished(State state) {
        return false;
    }

    @Override
    protected double calcReward(State oldState, State newState, Action action) {
        return 0;
    }

    @Override
    protected void duringEpisode(State oldState, Action action) {

    }
}
