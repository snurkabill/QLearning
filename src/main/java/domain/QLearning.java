package domain;

import domain.qvalues.QValuesContainer;
import domain.qvalues.StateBasedQValuesContainer;
import domain.qvalues.StateFreeQValuesContainer;
import domain.qvalues.features.Feature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public abstract class QLearning {

    public static final Logger LOGGER = LoggerFactory.getLogger(QLearning.class.getName());

    private double learningRate;
    private double discountFactor;
    private double randomFactor;

    private final Action[] actions;
    private final Random random;
    private final QValuesContainer qValuesContainer;

    public QLearning(List<Action> actions, Random random, double learningRate, double discountFactor,
                      double randomFactor, List<Feature> features) {
        this.random = random;
        this.actions = convertListToArray(actions);
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.randomFactor = randomFactor;
        this.qValuesContainer = features == null ? new StateBasedQValuesContainer(this.actions) :
                new StateFreeQValuesContainer(this.actions, features);
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

    public void reseLearningAtributes() {
        this.learningRate = 0.0;
        this.discountFactor = 0.0;
        this.randomFactor = 0.0;
    }

    protected void beforeEpisode() {
        LOGGER.info("Starting episode");
    }

    protected void afterEpiose(State oldState, int iterationsCount) {
        LOGGER.info("Ending episode after {} steps", iterationsCount);
    }

    private Action createRandomAction(State state) {
        return actions[random.nextInt(actions.length)];
    }

    private Action determineNextAction(State state) {
        return actions[qValuesContainer.getActionWithHighestQValue(state).getActionIndex()];
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
        for (int i = 0; i < episodesCount; i++) {
            run(maxIterationsPerEpisode);
        }
    }

    public void run(int numberOfIterations, long seed) {
        this.random.setSeed(seed);
        run(numberOfIterations);
    }

    public void run(int iterationsCount) {
        beforeEpisode();
        State newState = createState();
        int iterations = 0;
        for (; !isFinished(newState) && iterations < iterationsCount; iterations++) {
            State oldState = newState;
            Action action = createAction(oldState);
            LOGGER.info("Action index: {}", action.getIndex());
            newState = createNextState(oldState, action);
            calculateQ(oldState, newState, action);
        }
        afterEpiose(newState, iterations);
    }

    protected void calculateQ(State oldState, State newState, Action action) {
        double reward = calcReward(oldState, newState, action);
        double oldQValue = qValuesContainer.getQ(oldState, action);
        double learnedValue = reward +
                discountFactor * qValuesContainer.getActionWithHighestQValue(newState).getQValue();
        qValuesContainer.setQ(oldState, action, learningRate, oldQValue, learnedValue);
    }

}
