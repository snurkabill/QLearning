package examples.gridworld;

import domain.Action;
import domain.QLearning;
import domain.State;

import java.util.List;
import java.util.Random;

public class GridWorld extends QLearning {

    private List<List<GridCell>> cells;
    private int x;
    private int y;

    public GridWorld(List<Action> actions, Random random, double learningRate, double discountFactor,
                     double randomFactor, List<List<GridCell>> cells, int x, int y) {
        super(actions, random, learningRate, discountFactor, randomFactor, null, null);
        this.cells = cells;
        this.x = x;
        this.y = y;
    }

    @Override
    public State createState() {
        return new GridWorldState(x, y, cells.size());
    }

    @Override
    protected boolean isFinished(State state) {
        GridWorldState gridWorldState = (GridWorldState) state;
        if(gridWorldState.getX() < 0 || gridWorldState.getX() > cells.size()) {
            return true;
        }
        if(gridWorldState.getY() < 0 || gridWorldState.getY() > cells.get(0).size()) {
            return true;
        }
        if(cells.get(gridWorldState.getX()).get(gridWorldState.getY()).isWall()) {
            return true;
        }
        return false;
    }

    @Override
    protected double calcReward(State oldState, State newState, Action action) {
        GridWorldState state = (GridWorldState) newState;
        return cells.get((state).getX()).get(state.getY()).getReward();
    }

    @Override
    protected State createNextState(State oldState, Action action) {
        return action.createNextState(oldState);
    }

    @Override
    protected void duringEpisode(State oldState, Action action) {

    }
}
