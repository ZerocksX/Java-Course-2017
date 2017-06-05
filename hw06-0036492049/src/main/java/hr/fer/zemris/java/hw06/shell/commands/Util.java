package hr.fer.zemris.java.hw06.shell.commands;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities class
 *
 * @author Pavao Jerebić
 */
public class Util {

    /**
     * Splits given string into string tokens representing arguments
     *
     * @param string starting string
     * @return arguments from string in an array(process described in {@link hr.fer.zemris.java.hw06.shell.MyShell}
     * @throws IllegalArgumentException if string is not properly formated
     */
    public static String[] split(String string) throws IllegalArgumentException {
        char[] data = string.toCharArray();
        int length = data.length;
        List<String> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int offset = processOneString(string.substring(i), sb);
            list.add(sb.toString());
            sb = new StringBuilder();
            i += offset;
        }
        String[] result = new String[list.size()];
        return list.toArray(result);
    }

    /**
     * Produces one string token from string by appending it to given string builder
     *
     * @param string given string
     * @param sb     string builder
     * @return offset in starting string
     * @throws IllegalArgumentException if string is not properly formatted
     */
    private static int processOneString(String string, StringBuilder sb) throws IllegalArgumentException {
        int length = string.length();
        if (string.equals("")) {
            throw new IllegalArgumentException("Given string is blank");
        }
        if (string.startsWith("\"")) {
            char[] data = string.toCharArray();
//            if (string.endsWith("\\\"") || !string.endsWith("\"")) {
//                throw new IllegalArgumentException("String does not end qoutation marks");
//            }
            int i;
            for (i = 1; i < length - 1; ++i) {
                if (data[i] == '\\') {
                    if (i + 1 < length &&
                            (data[i + 1] == '\"' || data[i + 1] == '\\')) {
                        sb.append(new char[]{data[i + 1]});
                        i++;
                        if (i + 1 == length) {
                            throw new IllegalArgumentException("String does not end qoutation marks");
                        }
                    } else if (i + 1 == length) {
                        throw new IllegalArgumentException("String does not end qoutation marks");
                    } else {
                        sb.append("\\\\");
                    }
                } else if (data[i] == '"') {
                    if ((i + 1 < length && !Character.isWhitespace(data[i + 1]))) {
                        throw new IllegalArgumentException("String must either end on quotation mark or have a whitespace after");
                    }
                    return i;
                } else {
                    sb.append(data[i]);
                }
            }
            if (data[i] != '"') {
                throw new IllegalArgumentException("String must either end on quotation mark or have a whitespace after");
            }
            return i;
        } else {
            String result = string.trim().split(" ")[0];
            sb.append(result);
            return result.length();
        }
    }

    /**
     * Transforms byte to hex to format output used in hexdump
     *
     * @param bytearray array of bytes
     * @return hex string representing bytearray
     */
    public static String bytetohex(byte[] bytearray) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytearray) {
            sb.append(String.format("%02x ", b));
        }
        String result = sb.toString();
        if (result.length() > 0) result = result.substring(0, result.length() - 1);
        return result;
    }


    /**
     * Transforms long to hex to format output used in hexdump
     *
     * @param l long
     * @return hex string of given long
     */
    public static String longtohex(long l) {
        return String.format("%010X", l);
    }

}
