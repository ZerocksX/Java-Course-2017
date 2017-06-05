package hr.fer.zemris.java.web.random;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Class that has one method {@link #randomColorHex()} that generates a random color in hex format<br/>
 * Uses {@link SecureRandom}<br/>
 * Should be used as a bean
 *
 * @author Pavao Jerebić
 */
public class RandomColor {
    /**
     * Static final Random object used for generation of hex value
     */
    private static final Random random = new SecureRandom();

    /**
     * Method that creates a random color in hex format
     *
     * @return color in hex format
     */
    public String randomColorHex() {
        return "#" + Integer.toString(random.nextInt(16777215), 16);
    }

}
