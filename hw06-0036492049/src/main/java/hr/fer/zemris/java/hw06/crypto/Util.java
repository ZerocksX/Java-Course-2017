package hr.fer.zemris.java.hw06.crypto;


/**
 * Class with utilities
 *
 * @author Pavao Jerebić
 */
public class Util {

    /**
     * Array of hexadecimal chars
     */
    private static char[] chars = new char[]{
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * Transforms hex string to byte array
     *
     * @param keyText hex string
     * @return byte array
     * @throws IllegalArgumentException if given text is not valid(length not divisible by 2, unkwnows characters)
     */
    public static byte[] hextobyte(String keyText) {
        int keyLength = keyText.length();
        if (keyLength % 2 != 0) {
            throw new IllegalArgumentException("Given text not valid");
        }

        char[] key = keyText.toLowerCase().toCharArray();

        byte[] result = new byte[keyLength / 2];

        for (int i = 0; i < keyLength; i += 2) {
            if (!charInChars(key[i]) || !charInChars(key[i + 1])) {
                throw new IllegalArgumentException("Given text not valid");
            }
            result[i / 2] = (byte) (Character.digit(key[i], 16) * 16 + Character.digit(key[i + 1], 16));
        }
        return result;

    }

    /**
     * Transforms byte array into hex string with leading zeroes
     *
     * @param bytearray byte array
     * @return hex string
     */
    public static String bytetohex(byte[] bytearray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytearray) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * Returns if character is hexadecimal character
     *
     * @param c character
     * @return true if character is hexadecimal character
     */
    private static boolean charInChars(char c) {
        for (char c1 : chars) {
            if (c1 == c) {
                return true;
            }
        }
        return false;
    }
}
