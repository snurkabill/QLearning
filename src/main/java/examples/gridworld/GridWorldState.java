package examples.gridworld;

import domain.State;

public class GridWorldState extends State {

    private int x;
    private int y;
    private final int sizeX;

    public GridWorldState(int x, int y, int sizeX) {
        this.x = x;
        this.y = y;
        this.sizeX = sizeX;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean areEqual(State other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        GridWorldState that = (GridWorldState) other;
        return x == that.x && y == that.y && sizeX == that.sizeX;
    }

    @Override
    public Long createHashCode() {
        return (long) (y * sizeX + x);
    }

    @Override
    public State copy() {
        return new GridWorldState(x, y, sizeX);
    }
}
