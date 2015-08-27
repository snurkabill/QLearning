package domain;

public abstract class State {

    public abstract boolean areEqual(State other);

    public abstract Long createHashCode();

    public abstract State copy();

}
