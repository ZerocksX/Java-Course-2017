package hr.fer.zemris.java.encoder;

import java.security.MessageDigest;
import java.util.Formatter;

/**
 * Encoder using 'SHA-1' algorithm
 *
 * @author Pavao Jerebić
 */
public class Encoder {
    /**
     * Encodes given value using 'SHA-1'
     *
     * @param value value
     * @return hashed value
     * @throws IllegalArgumentException if anything fails
     */
    public static String encode(String value) {
        String encoded;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            messageDigest.reset();
            messageDigest.update(value.getBytes("UTF-8"));
            encoded = byteToHex(messageDigest.digest());
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
        return encoded;
    }

    /**
     * Helping method that transfers byte to hex format
     *
     * @param hash hash
     * @return hash as hex string
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
