package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;
import hr.fer.zemris.java.hw11.jnotepadpp.local.LocalizationProviderBridge;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Class that connects {@link LocalizationProviderBridge} and main form of the program
 *
 * @author Pavao Jerebić
 */
public class FormLocalizationProvider extends LocalizationProviderBridge {
    /**
     * Constructor that connects frame to the {@link LocalizationProviderBridge} and when window is closed disconnects it
     *
     * @param parent parent provider
     * @param frame  main frame
     */
    public FormLocalizationProvider(ILocalizationProvider parent, JFrame frame) {
        super(parent);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {
                connect();
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {
                disconnect();
            }
        });
    }
}
