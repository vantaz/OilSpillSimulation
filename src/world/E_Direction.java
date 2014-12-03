package world;

/**
 * Created by VanitaZ on 2014-11-20.
 */
public enum E_Direction {
    N,
    NE,
    E,
    SE,
    S,
    SW,
    W,
    NW;

    public E_Direction getOppositeDirection() {
        return values()[(ordinal() + 4) % 8];
    }

    public E_Direction getRightDirection() {
        return values()[(ordinal() + 1) % 8];
    }

    public E_Direction getLeftDirection() {
        return values()[(ordinal() + 7) % 8];
    }
}
