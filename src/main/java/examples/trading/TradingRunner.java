package examples.trading;

import domain.Action;
import domain.qvalues.features.BiasFeature;
import domain.qvalues.features.Feature;
import examples.trading.data.cyclic.ParameterizedSinus;
import examples.trading.domain.CommodityFeature;
import examples.trading.domain.DeltaPriceFeature;
import examples.trading.domain.MoneyFeature;
import examples.trading.domain.TradingAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TradingRunner {

    public static void main(String [] args) {

        List<Action> actions = new ArrayList<>();
        actions.add(new TradingAction(0));
        actions.add(new TradingAction(1));
        actions.add(new TradingAction(2));

        double learningRate = 0.001;
        double discountFactor = 0.5;
        double randomFactor = 0.8;
        int sizeOfSeenFector = 5;
        List<Feature> features = new ArrayList<>();
        features.add(new BiasFeature());
        features.add(new MoneyFeature());
        features.add(new CommodityFeature());
        for (int i = 0; i < sizeOfSeenFector; i++) {
            features.add(new DeltaPriceFeature(i));
        }
        Trading trading = new Trading(actions, new Random(0), learningRate, discountFactor, randomFactor, features,
                10, 0, sizeOfSeenFector, new ParameterizedSinus(0, 1, 1, 1), 0);
        trading.run(100000, 100, 0);
        trading.setRandomFactor(0.5);
        trading.run(100000, 100, 0);
        trading.setRandomFactor(0.2);
        trading.run(100000, 100, 0);
        trading.reseLearningAtributes();
        trading.initialize(10, 0, 0);
        trading.run(1, 10000);


    }
}
