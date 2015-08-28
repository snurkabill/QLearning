package domain.qvalues;

import domain.Action;
import domain.ActionQValuePair;
import domain.State;

public abstract class QValuesContainer {

    protected final Action[] actions;

    public QValuesContainer(Action[] actions) {
        this.actions = actions;
    }

    public ActionQValuePair getActionWithHighestQValue(State state) {
        int bestActionIndex = -1;
        double max = -Double.MAX_VALUE;
        for (int i = 0; i < actions.length; i++) {
            double actionQ = getQ(state, actions[i]);
            if(actionQ > max) {
                max = actionQ;
                bestActionIndex = i;
            }
        }
        if(bestActionIndex == -1) {
            throw new IllegalStateException("No Action was picked");
        }
        return new ActionQValuePair(bestActionIndex, max);
    }

    public abstract double getQ(State state, Action action);

    public abstract void setQ(State oldState, Action action, double learningRate, double oldValue, double learnedValue);

}
