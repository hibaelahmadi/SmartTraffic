package model.light;

/** Lancée quand une durée de feu est invalide (négative, nulle ou non finie). */
public class InvalidTimingException extends IllegalArgumentException {
    public InvalidTimingException(String message) {
        super(message);
    }
}
