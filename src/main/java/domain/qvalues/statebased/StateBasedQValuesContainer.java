package domain.qvalues.statebased;

import domain.Action;
import domain.ActionQValuePair;
import domain.State;
import domain.qvalues.QValuesContainer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class StateBasedQValuesContainer extends QValuesContainer {

    public class QValues {

        private final double[] actionQValues;

        public QValues(int numOfActions) {
            actionQValues = new double[numOfActions];
        }

        public double getQValueToAction(Action action) {
            return actionQValues[action.getIndex()];
        }

        public void setQValue(Action action, double newValue) {
            actionQValues[action.getIndex()] = newValue;
        }

        public ActionQValuePair getBest() {
            double max = -Double.MAX_VALUE;
            int index = -1;
            for (int i = 0; i < actionQValues.length; i++) {
                if(max < actionQValues[i]) {
                    max = actionQValues[i];
                    index = i;
                }
            }
            if(index == -1) {
                throw new IllegalStateException("No QValue picked. QVals: " + Arrays.toString(actionQValues));
            }
            return new ActionQValuePair(index, max);
        }
    }

    private final Map<Long, QValues> qValues = new HashMap<>();

    public StateBasedQValuesContainer(Action[] actions) {
        super(actions);
    }

    @Override
    public double getQ(State state, Action action) {
        if(!qValues.containsKey(state.createHashCode())) {
            qValues.put(state.createHashCode(), new QValues(actions.length));
        }
        return qValues.get(state.createHashCode()).getQValueToAction(action);
    }

    @Override
    public void setQ(State state, Action action, double learningRate, double oldQValue, double learnedValue) {
        if(!qValues.containsKey(state.createHashCode())) {
            qValues.put(state.createHashCode(), new QValues(actions.length));
        }
        qValues.get(state.createHashCode()).setQValue(action, oldQValue + learningRate * (learnedValue - oldQValue));
    }
}
