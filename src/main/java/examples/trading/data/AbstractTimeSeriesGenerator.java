package examples.trading.data;

public abstract  class AbstractTimeSeriesGenerator {

    public abstract double getValue(long time);

    public double[] createVector(long time, int size) {
        double[] vector = new double[size];
        for (int i = 0; i < vector.length; i++) {
            vector[i] = getValue(time + i);
        }
        return vector;
    }
}
