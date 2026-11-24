package model.light;

/** Les quatre phases du cycle d'une intersection. */
public enum Phase {
    NS_VERT(Axis.NORD_SUD, true),
    NS_ORANGE(Axis.NORD_SUD, false),
    EO_VERT(Axis.EST_OUEST, true),
    EO_ORANGE(Axis.EST_OUEST, false);

    private final Axis axis;
    private final boolean green;

    Phase(Axis axis, boolean green) {
        this.axis = axis;
        this.green = green;
    }

    /** L'axe qui a le feu (vert ou orange) pendant cette phase. */
    public Axis getAxis() { return axis; }

    public boolean isGreen() { return green; }

    /** Durée de cette phase selon la configuration. */
    public double duration(LightTiming timing) {
        return green ? timing.getGreen() : timing.getOrange();
    }

    /** Phase suivante du cycle. */
    public Phase next() {
        switch (this) {
            case NS_VERT:   return NS_ORANGE;
            case NS_ORANGE: return EO_VERT;
            case EO_VERT:   return EO_ORANGE;
            default:        return NS_VERT;
        }
    }

    public static Phase green(Axis axis) {
        return axis == Axis.NORD_SUD ? NS_VERT : EO_VERT;
    }

    public static Phase orange(Axis axis) {
        return axis == Axis.NORD_SUD ? NS_ORANGE : EO_ORANGE;
    }
}
