package examples.trading.data.cyclic;

import examples.trading.data.AbstractTimeSeriesGenerator;

public class ArtificialFunction extends AbstractTimeSeriesGenerator {

    @Override
    public double getValue(long time) {
        return time % 2 == 0 ? 1 : 2;
    }
}
