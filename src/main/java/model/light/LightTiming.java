package model.light;

/**
 * Durées d'un cycle de feux, en secondes.
 * Le rouge d'un axe dure autant que (vert + orange) de l'autre axe.
 */
public final class LightTiming {
    private final double green;
    private final double orange;

    public LightTiming(double green, double orange) {
        this.green = check(green, "vert");
        this.orange = check(orange, "orange");
    }

    /** Valeurs par défaut : vert 30 s, orange 5 s. */
    public static LightTiming standard() {
        return new LightTiming(30, 5);
    }

    private static double check(double value, String name) {
        if (Double.isNaN(value) || Double.isInfinite(value) || value <= 0) {
            throw new InvalidTimingException("Durée invalide pour " + name + " : " + value);
        }
        return value;
    }

    public double getGreen() { return green; }
    public double getOrange() { return orange; }

    /** Durée totale d'un cycle complet (deux axes). */
    public double getCycleDuration() { return 2 * (green + orange); }
}
