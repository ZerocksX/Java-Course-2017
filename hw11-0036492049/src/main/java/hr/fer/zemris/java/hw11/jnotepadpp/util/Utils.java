package hr.fer.zemris.java.hw11.jnotepadpp.util;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Utilities class
 *
 * @author Pavao Jerebić
 */
public class Utils {
    /**
     * Method that creates an {@link ImageIcon} from given class and icon name<br/>
     * Icon should be in the package as the given class' subpackage 'icons'
     *
     * @param currentClass current class
     * @param iconName     icon name
     * @return image icon
     * @throws IllegalArgumentException if an icon with given name does not exist
     */
    public static ImageIcon createImageIcon(Class<?> currentClass, String iconName) {
        InputStream is = currentClass.getResourceAsStream("icons/" + iconName);
        if (is == null) {
            throw new IllegalArgumentException("Given icon does not exist");
        }
        byte[] bytes = readAllBytes(is);
        return new ImageIcon(bytes);
    }

    /**
     * Helping method that reads all bytes from the given input stream
     *
     * @param is input stream
     * @return byte array
     * @throws IllegalArgumentException if could not read from the given stream
     */
    private static byte[] readAllBytes(InputStream is) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int nRead;
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not read the given icon");
        }

        return buffer.toByteArray();
    }
}
