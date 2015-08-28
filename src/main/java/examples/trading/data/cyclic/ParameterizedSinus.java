package examples.trading.data.cyclic;

import examples.trading.data.AbstractTimeSeriesGenerator;

public class ParameterizedSinus extends AbstractTimeSeriesGenerator {

    private final double shiftX;
    private final double shiftY;
    private final double frequencyCoeff;
    private final double coeff;

    public ParameterizedSinus(double shiftX, double shiftY, double frequencyCoeff, double coeff) {
        this.shiftX = shiftX;
        this.shiftY = shiftY;
        this.frequencyCoeff = frequencyCoeff;
        this.coeff = coeff;
    }

    @Override
    public double getValue(long time) {
        return coeff * Math.sin(time * frequencyCoeff + shiftX) + shiftY;
    }
}
