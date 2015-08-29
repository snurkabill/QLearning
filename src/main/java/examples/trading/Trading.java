package examples.trading;

import domain.Action;
import domain.QLearning;
import domain.State;
import domain.qvalues.statefree.generalization.FeatureBasedStateEvaluator;
import examples.trading.data.AbstractTimeSeriesGenerator;
import examples.trading.domain.TradingState;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Trading extends QLearning {

    private double startingMoney;
    private int startingCommodity;
    private double money;
    private int commodity;
    private final AbstractTimeSeriesGenerator generator;
    private double lastPrice;
    private final double[] prices;
    private final double[] deltaPrices;
    private int time;

    public Trading(List<Action> actions, Random random, double learningRate, double discountFactor, double randomFactor,
                   FeatureBasedStateEvaluator featureBasedStateEvaluator, double startingMoney, int startingCommodity,
                   int sizeOfSeenVector, AbstractTimeSeriesGenerator generator, int startingTime) {
        super(actions, random, learningRate, discountFactor, randomFactor, featureBasedStateEvaluator);
        this.deltaPrices = new double[sizeOfSeenVector];
        this.prices = new double[sizeOfSeenVector];
        this.generator = generator;
        initialize(startingMoney, startingCommodity, startingTime);
    }

    public void initialize(double startingMoney, int startingCommodity, int startingTime) {
        this.money = startingMoney;
        this.startingMoney = startingMoney;
        this.commodity = startingCommodity;
        this.startingCommodity = startingCommodity;
        this.time = startingTime;
    }

    public double getStartingMoney() {
        return startingMoney;
    }

    public int getStartingCommodity() {
        return startingCommodity;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getCommodity() {
        return commodity;
    }

    public void setCommodity(int commodity) {
        this.commodity = commodity;
    }

    private void calcNewPrices(int i) {
        double[] newPrices = generator.createVector(i, deltaPrices.length);
        for (int j = 0; j < deltaPrices.length; j++) {
            deltaPrices[j] = newPrices[j] - prices[j];
            prices[j] = newPrices[j];
        }
        lastPrice = prices[prices.length - 1];
    }

    @Override
    protected void beforeEpisode() {
        super.beforeEpisode();
        calcNewPrices(time++);
        calcNewPrices(time++);
    }

    @Override
    protected void afterEpiose(State oldState, int iterationsCount) {
        super.afterEpiose(oldState, iterationsCount);
        TradingState state = (TradingState) oldState;
        LOGGER.info("Total money diff: {}", state.getMoney() - startingMoney);
        LOGGER.info("Total commodity diff: {}", state.getCommodity() - startingCommodity);
        LOGGER.info("Total wealth : {}", state.getMoney() + state.getCommodity() * state.getLastPrice());
    }

    @Override
    public State createState() {
        return new TradingState(money, commodity, Arrays.copyOf(deltaPrices, deltaPrices.length), lastPrice);
    }

    @Override
    protected boolean isFinished(State state) {
        TradingState tradingState = (TradingState) state;
        if(tradingState.getMoney() < 0.0 || tradingState.getCommodity() < 0) {
            return true;
        }
        if(tradingState.getMoney() + tradingState.getCommodity() * tradingState.getLastPrice() > 100) {
            return true;
        }
        return false;
    }

    @Override
    protected double calcReward(State oldState, State newState, Action action) {
        TradingState tradingOldState = (TradingState) oldState;
        TradingState tradingNewState = (TradingState) newState;
        if(tradingNewState.getCommodity() < 0) {
            return -10;
        }
        if(tradingNewState.getMoney() < 0.0) {
            return -10;
        }
        return tradingNewState.getMoney() - tradingOldState.getMoney();
    }

    @Override
    protected State createNextState(State oldState, Action action) {
        TradingState tradingState = (TradingState) action.createNextState(oldState);
        calcNewPrices(time++);
        tradingState.setDeltaPrice(Arrays.copyOf(deltaPrices, deltaPrices.length));
        tradingState.setLastPrice(lastPrice);
        return tradingState;
    }
}
