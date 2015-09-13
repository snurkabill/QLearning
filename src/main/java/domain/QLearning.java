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

    public QLearning(List<Action> actions, long randomSeed, double learningRate, double discountFactor,
                      double randomFactor, FeatureBasedStateEvaluator featureBasedStateEvaluator,
                     FeatureBasedStateEvaluator shiftedFeatureBasedStateEvaluator) {
        this.random = new Random(randomSeed);
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

    protected abstract State createInitialState();

    protected abstract boolean isFinished(State state);

    protected abstract double calcReward(State oldState, State newState, Action action);

    protected void beforeEpisode() {
        LOGGER.debug("Starting episode");
    }

    protected abstract void duringEpisode(State state, Action action);

    protected void afterEpisode(State state, int stepsCount) {
        LOGGER.debug("Ending episode after {} steps", stepsCount);
        totalStepsCounter += stepsCount;
    }

    private Action createRandomAction() {
        return actions[random.nextInt(actions.length)];
    }

    private Action determineNextAction(State state) {
        return actions[qValuesContainer.getActionWithHighestQValue(state, QValuesContainer.Approximator.NEW).getActionIndex()];
    }

    private Action createAction(State state) {
        if(mode == Mode.TRAINING && random.nextDouble() < randomFactor) {
            LOGGER.trace("Picking random action");
            return createRandomAction();
        } else {
            return determineNextAction(state);
        }
    }

    public void run(int episodesCount, int maxStepsPerEpisode, long seed) {
        this.random.setSeed(seed);
        run(episodesCount, maxStepsPerEpisode);
    }

    public void run(int episodesCount, int maxIterationsPerEpisode) {
        LOGGER.info("Starting {} episodes each with {} iterations", episodesCount, maxIterationsPerEpisode);
        totalStepsCounter = 0;
        for (int i = 0; i < episodesCount; i++) {
            run(maxIterationsPerEpisode);
        }
        LOGGER.info("Average steps per episode: {}", (double) totalStepsCounter / episodesCount);
    }

    public void run(int stepsCount) {
        beforeEpisode();
        State newState = createInitialState();
        Action action;
        int numOfSteps = 0;
        for (; !isFinished(newState) && numOfSteps < stepsCount; numOfSteps++) {
            State oldState = newState;
            action = createAction(oldState);
            duringEpisode(oldState, action);
            newState = action.apply(oldState);
            if(mode == Mode.TRAINING) {
                calculateQ(oldState, newState, action);
            }
        }
        afterEpisode(newState, numOfSteps);
    }

    protected void calculateQ(State oldState, State newState, Action action) {
        double reward = calcReward(oldState, newState, action);
        double oldQValue = qValuesContainer.getQ(oldState, action, QValuesContainer.Approximator.NEW);
        double learnedValue = reward +
                discountFactor * qValuesContainer.getActionWithHighestQValue(newState, QValuesContainer.Approximator.OLD).getQValue();
        qValuesContainer.setQ(oldState, action, learningRate, oldQValue, learnedValue);
    }

}
