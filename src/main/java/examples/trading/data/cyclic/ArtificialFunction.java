package examples.trading.data.cyclic;

import examples.trading.data.AbstractTimeSeriesGenerator;

public class ArtificialFunction extends AbstractTimeSeriesGenerator {

    @Override
    public double getValue(long time) {
        return Math.pow(10, time % 3);
    }
}
