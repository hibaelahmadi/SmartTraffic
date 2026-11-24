package model.light;

import common.Direction;

/** Un axe de l'intersection : Nord/Sud ou Est/Ouest. */
public enum Axis {
    NORD_SUD, EST_OUEST;

    /** Renvoie l'axe auquel appartient une direction. */
    public static Axis of(Direction d) {
        return (d == Direction.NORD || d == Direction.SUD) ? NORD_SUD : EST_OUEST;
    }

    /** Renvoie l'autre axe. */
    public Axis opposite() {
        return this == NORD_SUD ? EST_OUEST : NORD_SUD;
    }
}
