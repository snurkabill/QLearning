package examples.trading;

import domain.Action;
import domain.QLearning;
import domain.State;
import domain.qvalues.statefree.generalization.FeatureBasedStateEvaluator;
import examples.trading.data.AbstractTimeSeriesGenerator;
import examples.trading.domain.TradingAction;
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
    private TradingState lastState;
    private int countBuyCrash = 0;
    private int countSellCrash = 0;
    private int[] actionCounts = new int[TradingAction.Type.values().length];

    public Trading(List<Action> actions, Random random, double learningRate, double discountFactor, double randomFactor,
                   FeatureBasedStateEvaluator featureBasedStateEvaluator,
                   FeatureBasedStateEvaluator shiftedFeatureBasedStateEvaluator, double startingMoney,
                   int startingCommodity, int sizeOfSeenVector, AbstractTimeSeriesGenerator generator,
                   int startingTime) {
        super(actions, random, learningRate, discountFactor, randomFactor, featureBasedStateEvaluator, shiftedFeatureBasedStateEvaluator);
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

    public TradingState getLastState() {
        return lastState;
    }

    public void setLastState(TradingState lastState) {
        this.lastState = lastState;
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
        money = 10;
        commodity = 0;
        for (int i = 0; i < actionCounts.length; i++) {
            actionCounts[i] = 0;
        }
    }

    @Override
    protected void duringEpisode(State state, Action action) {
        actionCounts[action.getIndex()]++;
    }

    @Override
    protected void afterEpisode(State oldState, int iterationsCount) {
        super.afterEpisode(oldState, iterationsCount);
        lastState = (TradingState) oldState;
        if(super.getMode() == Mode.TESTING) {
            LOGGER.info("Actions: {}", actionCounts);
        }
        LOGGER.debug("Total money diff: {}", lastState.getMoney() - startingMoney);
        LOGGER.debug("Total commodity diff: {}", lastState.getCommodity() - startingCommodity);
        LOGGER.debug("Total wealth : {}", lastState.getMoney() + lastState.getCommodity() * lastState.getLastPrice());
    }

    @Override
    public State createState() {
        return new TradingState(money, commodity, Arrays.copyOf(deltaPrices, deltaPrices.length), lastPrice);
    }

    @Override
    protected boolean isFinished(State state) {
        TradingState tradingState = (TradingState) state;
        if(tradingState.getMoney() < 0.0 || tradingState.getCommodity() < 0) {
            if(tradingState.getMoney() < 0.0) {
                countSellCrash++;
            } else {
                countBuyCrash++;
            }
            return true;
        }
        /*if(tradingState.getMoney() *//*+ tradingState.getCommodity() * tradingState.getLastPrice()*//* > 1000) {
            return true;
        }*/
        return false;
    }

    @Override
    protected double calcReward(State oldState, State newState, Action action) {
        TradingState tradingOldState = (TradingState) oldState;
        TradingState tradingNewState = (TradingState) newState;
        if(tradingNewState.getCommodity() < 0) {
            return -100;
        }
        if(tradingNewState.getMoney() < 0.0) {
            return -100;
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
