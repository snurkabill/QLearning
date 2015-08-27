package examples.gridworld;

public class GridCell {

    private final int x;
    private final int y;
    private final double reward;
    private final boolean isWall;

    public GridCell(int x, int y, double reward, boolean isWall) {
        this.x = x;
        this.y = y;
        this.reward = reward;
        this.isWall = isWall;
    }

    public boolean isWall() {
        return isWall;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getReward() {
        return reward;
    }
}
