package domain;

public abstract class Action {

    protected final int index;

    public Action(int index) {
        this.index = index;
    }

    public abstract void apply(State state);

    public abstract State createNextState(State state);

    public int getIndex() {
        return index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Action that = (Action) o;
        return index == that.index;
    }

    @Override
    public int hashCode() {
        return 7 * index;
    }

}
