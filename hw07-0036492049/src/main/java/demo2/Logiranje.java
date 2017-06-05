package demo2;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demo for logging
 */
public class Logiranje {
    /**
     * Logger
     */
    private static final Logger LOG = Logger.getLogger("demo2");

    /**
     * Starting method
     *
     * @param args ignored
     */
    public static void main(String[] args) {
        Level[] levels = new Level[]{
                Level.SEVERE,
                Level.WARNING,
                Level.INFO,
                Level.CONFIG,
                Level.FINE,
                Level.FINER,
                Level.FINEST
        };
        for (Level l : levels) {
            LOG.log(l, "Ovo je poruka " + l + " razine.");
        }
    }
}