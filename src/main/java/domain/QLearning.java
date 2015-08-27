package domain;

import domain.qvalues.QValuesContainer;
import domain.qvalues.StateBasedQValuesContainer;
import domain.qvalues.StateFreeQValuesContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class QLearning {

    public static final Logger LOGGER = LoggerFactory.getLogger(QLearning.class.getName());

    public enum Type {
        STATE_FREE,
        STATE_BASED
    }

    private double learningRate;
    private double discountFactor;
    private double randomFactor;

    private final List<Action> actions;
    private final Random random;
    private final QValuesContainer qValuesContainer;

    public QLearning(List<Action> actions, Random random, Type type, double learningRate,
                     double discountFactor, double randomFactor) {
        if(type == Type.STATE_FREE) {
            throw new UnsupportedOperationException("Not supported yet");
        }
        ArrayList<Action> convertedList = new ArrayList<>(actions);
        qValuesContainer = type == Type.STATE_BASED ?
                new StateBasedQValuesContainer(convertedList) :
                new StateFreeQValuesContainer(convertedList);
        this.random = random;
        this.actions = convertedList;
        this.learningRate = learningRate;
        this.discountFactor = discountFactor;
        this.randomFactor = randomFactor;
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

    protected void beforeStart() {
        LOGGER.info("Starting episode");
    }

    private Action createRandomAction(State state) {
        return actions.get(random.nextInt(actions.size()));
    }

    private Action determineNextAction(State state) {
        return actions.get(qValuesContainer.getActionWithHighestQValue(state).getActionIndex());
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
        for (int i = 0; i < episodesCount; i++) {
            run(maxIterationsPerEpisode);
        }
    }

    public void run(int numberOfIterations, long seed) {
        this.random.setSeed(seed);
        run(numberOfIterations);
    }

    public void run(int numberOfIterations) {
        beforeStart();
        State newState = createState();
        for (int i = 0; i < numberOfIterations; i++) {
            State oldState = newState;
            if(isFinished(oldState)) {
                LOGGER.info("Ending episode after {} steps", i);
                break;
            }
            Action action = createAction(oldState);
            LOGGER.info("Action index: {}", action.getIndex());
            newState = action.createNextState(oldState);
            calculateQ(oldState, newState, action);
        }
    }

    protected void calculateQ(State oldState, State newState, Action action) {
        double reward = calcReward(oldState, newState, action);
        double oldQValue = qValuesContainer.getQ(oldState, action);
        double learnedValue = reward +
                discountFactor * qValuesContainer.getActionWithHighestQValue(newState).getQValue();
        qValuesContainer.setQ(oldState, action, learningRate, oldQValue, learnedValue);
    }

}
