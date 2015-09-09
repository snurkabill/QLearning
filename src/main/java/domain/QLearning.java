package domain;

import domain.qvalues.QValuesContainer;
import domain.qvalues.statebased.StateBasedQValuesContainer;
import domain.qvalues.statefree.StateFreeQValuesContainer;
import domain.qvalues.statefree.generalization.FeatureBasedStateEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public abstract class QLearning {

    public static final Logger LOGGER = LoggerFactory.getLogger(QLearning.class.getName());

    public enum Mode {
        TRAINING,
        TESTING
    }

    private Mode mode;

    private double learningRate;
    private double discountFactor;
    private double randomFactor;

    private final Action[] actions;
    private final Random random;
    private final QValuesContainer qValuesContainer;
    private int totalStepsCounter;

    public QLearning(List<Action> actions, Random random, double learningRate, double discountFactor,
                      double randomFactor, FeatureBasedStateEvaluator featureBasedStateEvaluator,
                     FeatureBasedStateEvaluator shiftedFeatureBasedStateEvaluator) {
        this.random = random;
        this.actions = convertListToArray(actions);
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.randomFactor = randomFactor;
        this.qValuesContainer = featureBasedStateEvaluator == null ? new StateBasedQValuesContainer(this.actions) :
                new StateFreeQValuesContainer(this.actions, featureBasedStateEvaluator, shiftedFeatureBasedStateEvaluator);
    }

    public Mode getMode() {
        return mode;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    private Action[] convertListToArray(List<Action> actions) {
        Action[] convertedArray = new Action[actions.size()];
        for (int i = 0; i < convertedArray.length; i++) {
            convertedArray[i] = actions.get(i);
        }
        return convertedArray;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getDiscountFactor() {
        return discountFactor;
    }

    public void setDiscountFactor(double discountFactor) {
        this.discountFactor = discountFactor;
    }

    public double getRandomFactor() {
        return randomFactor;
    }

    public void setRandomFactor(double randomFactor) {
        this.randomFactor = randomFactor;
    }

    public abstract State createState();

    protected abstract boolean isFinished(State state);

    protected abstract double calcReward(State oldState, State newState, Action action);

    protected abstract State createNextState(State oldState, Action action);

    protected void beforeEpisode() {
        LOGGER.debug("Starting episode");
        random.setSeed(random.nextLong());
    }

    protected abstract void duringEpisode(State oldState, Action action);

    protected void afterEpisode(State oldState, int iterationsCount) {
        LOGGER.debug("Ending episode after {} steps", iterationsCount);
        totalStepsCounter += iterationsCount;
    }

    private Action createRandomAction(State state) {
        return actions[random.nextInt(actions.length)];
    }

    private Action determineNextAction(State state) {
        return actions[qValuesContainer.getActionWithHighestQValue(state, QValuesContainer.Approximator.NEW).getActionIndex()];
    }

    private Action createAction(State state) {
        if(random.nextDouble() < randomFactor) {
            LOGGER.trace("Random action picked!");
            return createRandomAction(state);
        } else {
            return determineNextAction(state);
        }
    }

    public void run(int episodesCount, int maxIterationsPerEpisode, long seed) {
        this.random.setSeed(seed);
        run(episodesCount, maxIterationsPerEpisode);
    }

    public void run(int episodesCount, int maxIterationsPerEpisode) {
        LOGGER.info("Starting {} episodes each with {} iterations", episodesCount, maxIterationsPerEpisode);
        totalStepsCounter = 0;
        for (int i = 0; i < episodesCount; i++) {
            run(maxIterationsPerEpisode);
        }
        LOGGER.info("Average steps: {}", (double) totalStepsCounter / episodesCount );
    }

    public void run(int iterationsCount) {
        beforeEpisode();
        State newState = createState();
        Action action;
        int iterations = 0;
        for (; !isFinished(newState) && iterations < iterationsCount; iterations++) {
            State oldState = newState;
            action = createAction(oldState);
            duringEpisode(oldState, action);
            newState = createNextState(oldState, action);
            if(mode == Mode.TRAINING) {
                calculateQ(oldState, newState, action);
            }
        }
        afterEpisode(newState, iterations);
    }

    protected void calculateQ(State oldState, State newState, Action action) {
        double reward = calcReward(oldState, newState, action);
        double oldQValue = qValuesContainer.getQ(oldState, action, QValuesContainer.Approximator.NEW);
        double learnedValue = reward +
                discountFactor * qValuesContainer.getActionWithHighestQValue(newState, QValuesContainer.Approximator.OLD).getQValue();
        qValuesContainer.setQ(oldState, action, learningRate, oldQValue, learnedValue);
    }

}
