package examples.gridworld;

import domain.Action;
import domain.State;

public class GridWorldAction extends Action {

    public GridWorldAction(int index) {
        super(index);
    }

    @Override
    public void apply(State state) {
        GridWorldState newState = (GridWorldState) state;
        if(index == 0) {
            newState.setX(newState.getX() + 1);
        } else if(index == 1) {
            newState.setX(newState.getX() - 1);
        } else if(index == 2) {
            newState.setY(newState.getY() + 1);
        } else if(index == 3) {
            newState.setY(newState.getY() - 1);
        } else {
            throw new IllegalStateException("There is no such action");
        }
    }

    @Override
    public State createNextState(State state) {
        GridWorldState newState = (GridWorldState) state.copy();
        apply(newState);
        return newState;
    }
}
