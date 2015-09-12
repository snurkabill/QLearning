package examples.trading;

import domain.Action;
import domain.QLearning;
import domain.qvalues.statefree.features.BiasFeature;
import domain.qvalues.statefree.features.Feature;
import domain.qvalues.statefree.generalization.LinearStateEvaluator;
import domain.qvalues.statefree.generalization.NonLinearStateEvaluatorBasedOnNeuralNetwork;
import examples.trading.data.cyclic.ParameterizedSinus;
import examples.trading.domain.*;
import net.snurkabill.neuralnetworks.heuristic.FeedForwardHeuristic;
import net.snurkabill.neuralnetworks.math.function.transferfunction.LinearFunction;
import net.snurkabill.neuralnetworks.math.function.transferfunction.ParametrizedHyperbolicTangens;
import net.snurkabill.neuralnetworks.neuralnetwork.feedforward.backpropagative.impl.online.OnlineFeedForwardNetwork;
import net.snurkabill.neuralnetworks.weights.weightfactory.GaussianRndWeightsFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TradingRunner {

    public static void main(String [] args) {

        Random random = new Random();


        for (int i = 0; i < 100; i++) {
            random.setSeed(random.nextLong());
        }



        useNeuralNetwork();
    }

    public static void useNeuralNetwork() {
        List<Action> actions = new ArrayList<>();
        actions.add(new TradingAction(0, 0, TradingAction.Type.NOTHING));
        int tradeActionsCount = 1;
        for (int i = 0; i < tradeActionsCount; i++) {
            actions.add(new TradingAction(i + 1, (int) Math.pow(2, i), TradingAction.Type.BUY));
        }
        for (int i = 0; i < tradeActionsCount; i++) {
            actions.add(new TradingAction(i + 1 + tradeActionsCount, (int) Math.pow(2, i), TradingAction.Type.SELL));
        }

        double learningRate = 0.0001;
        double discountFactor = 0.5;
        double randomFactor = 0.8;
        int sizeOfSeenVector = 5;

        List<Feature> features = new ArrayList<>();
        features.add(new MoneyFeature());
        features.add(new CommodityFeature());
        features.add(new IsActionDoable());
        for (int i = 0; i < sizeOfSeenVector; i++) {
            features.add(new DeltaPriceFeature(i));
        }
        FeedForwardHeuristic heuristic = new FeedForwardHeuristic();
        heuristic.learningRate = 0.0000001;
        heuristic.l2RegularizationConstant = 0.0;
        heuristic.dynamicKillingWeights = false;
        heuristic.momentum = 0.000;
        heuristic.BOOSTING_ETA_COEFF = 0.0;
        heuristic.dynamicBoostingEtas = false;
        OnlineFeedForwardNetwork neuralNetwork = new OnlineFeedForwardNetwork("asdf",
                Arrays.asList(/*features.size(), */features.size(), features.size(),  1),
                Arrays.asList(/*new ParametrizedHyperbolicTangens(),*/ new ParametrizedHyperbolicTangens(), new LinearFunction()),
                new GaussianRndWeightsFactory(0.1, 0), heuristic);
        OnlineFeedForwardNetwork neuralNetwork2 = new OnlineFeedForwardNetwork("asdf2",
                Arrays.asList(/*features.size(),*/ features.size(), features.size(),  1),
                Arrays.asList(/*new ParametrizedHyperbolicTangens(),*/ new ParametrizedHyperbolicTangens(), new LinearFunction()),
                new GaussianRndWeightsFactory(0.1, 0), heuristic);
        Trading trading = new Trading(actions, new Random(0), learningRate, discountFactor, randomFactor,
                new NonLinearStateEvaluatorBasedOnNeuralNetwork(features, neuralNetwork),
                new NonLinearStateEvaluatorBasedOnNeuralNetwork(features, neuralNetwork2), 10, 0, sizeOfSeenVector,
                new ParameterizedSinus(0, 1, 1, 1), 0);
        /*trading.run(1000, 1000, 0);
        trading.setRandomFactor(0.5);
        trading.initialize(10, 0, 0);
        trading.run(10000, 1000, 0);*/


        for (int i = 0; i < 1000; i++) {
            QLearning.LOGGER.info("------------------------------------------------------");
            trading.setLearningRate(0.001);
            trading.setDiscountFactor(0.5);
            trading.setMode(QLearning.Mode.TRAINING);
            /*randomFactor = Math.pow(Math.E, (double) -i / 3.0) / 3;
            QLearning.LOGGER.info("Random Factor: {}", randomFactor);*/
            trading.setRandomFactor(0.3/*randomFactor*/);
            trading.initialize(10, 0, 0);
            trading.run(10000, 1000, i);

            int startingMoney = 10;
            int startingCommodity = 0;

            trading.setRandomFactor(0.0);
            trading.setMode(QLearning.Mode.TESTING);
            trading.initialize(startingMoney, startingCommodity, 0);
            trading.run(1, 1000);
            TradingState state = trading.getLastState();
            QLearning.LOGGER.info("Money {}, Commodity: {}", state.getMoney() - startingMoney, state.getCommodity() - startingCommodity);
//            try {
//                System.in.read();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

            /*trading.reseLearningAtributes();
            trading.initialize(startingMoney, startingCommodity, 1);
            trading.run(1, 100100);
            state = trading.getLastState();
            QLearning.LOGGER.info("Money {}, Commodity: {}", state.getMoney() - startingMoney, state.getCommodity() - startingCommodity);

            trading.reseLearningAtributes();
            trading.initialize(startingMoney, startingCommodity, 2);
            trading.run(1, 101000);
            state = trading.getLastState();
            QLearning.LOGGER.info("Money {}, Commodity: {}", state.getMoney() - startingMoney, state.getCommodity() - startingCommodity);*/

            //QLearning.LOGGER.info("Weights {}", neuralNetwork.getWeights());

        }

        QLearning.LOGGER.info("PRODUCTION");

        /*trading.reseLearningAtributes();
        trading.initialize(15, 5, 3);
        trading.run(1, 10000);

        trading.reseLearningAtributes();
        trading.initialize(15, 5, 4);
        trading.run(1, 10000);

        trading.reseLearningAtributes();
        trading.initialize(15, 5, 5);
        trading.run(1, 10000);*/
    }

    public static void linearVersion() {
        List<Action> actions = new ArrayList<>();
        actions.add(new TradingAction(0, 0, TradingAction.Type.NOTHING));
        actions.add(new TradingAction(1, 1, TradingAction.Type.BUY));
        actions.add(new TradingAction(2, 1, TradingAction.Type.SELL));

        double learningRate = 0.001;
        double discountFactor = 0.5;
        double randomFactor = 0.8;
        int sizeOfSeenFector = 5;
        List<Feature> features = new ArrayList<>();
        features.add(new BiasFeature());
        features.add(new MoneyFeature());
        features.add(new CommodityFeature());
        features.add(new IsActionDoable());
        for (int i = 0; i < sizeOfSeenFector; i++) {
            features.add(new DeltaPriceFeature(i));
        }
        int startingMoney = 10;
        int startingCommodity = 0;
        Trading trading = new Trading(actions, new Random(0), learningRate, discountFactor, randomFactor,
                new LinearStateEvaluator(features), new LinearStateEvaluator(features), startingMoney, startingCommodity, sizeOfSeenFector,
                new ParameterizedSinus(0, 1, 1, 1), 0);
        trading.run(100000, 100, 0);

        trading.setRandomFactor(0.5);
        trading.initialize(10, 0, 0);
        trading.run(100000, 100, 0);

        trading.setRandomFactor(0.2);
        trading.initialize(10, 0, 0);
        trading.run(1000000, 100, 0);
    }
}
