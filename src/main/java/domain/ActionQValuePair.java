package domain;

public class ActionQValuePair {

    private final int actionIndex;
    private final double QValue;

    public ActionQValuePair(int actionIndex, double qValue) {
        this.actionIndex = actionIndex;
        QValue = qValue;
    }

    public int getActionIndex() {
        return actionIndex;
    }

    public double getQValue() {
        return QValue;
    }
}
