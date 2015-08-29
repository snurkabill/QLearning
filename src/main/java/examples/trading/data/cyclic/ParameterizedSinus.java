package examples.trading.data.cyclic;

import examples.trading.data.AbstractTimeSeriesGenerator;

public class ParameterizedSinus extends AbstractTimeSeriesGenerator {

    private final double shiftX;
    private final double shiftY;
    private final double frequencyCoeff;
    private final double sizeCoeff;

    public ParameterizedSinus(double shiftX, double shiftY, double frequencyCoeff, double sizeCoeff) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.frequencyCoeff = frequencyCoeff;
        this.sizeCoeff = sizeCoeff;
    }

    @Override
    public double getValue(long time) {
        return sizeCoeff * Math.sin(time * frequencyCoeff + shiftX) + shiftY;
    }
}
