package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Localization provider decorator that manages the connection status
 *
 * @author Pavao Jerebić
 */
public class LocalizationProviderBridge extends AbstractLocalizationProvider {
    /**
     * Listener
     */
    private ILocalizationListener listener;
    /**
     * Parent provider
     */
    private ILocalizationProvider parent;
    /**
     * Is connected
     */
    private boolean connected;

    /**
     * Constructor that initializes provider parent
     *
     * @param parent provider
     */
    public LocalizationProviderBridge(ILocalizationProvider parent) {
        this.parent = parent;
        connected = false;
    }

    /**
     * Method that disconnects this bridge from the provider
     */
    public void disconnect() {
        if (connected) {
            parent.removeLocalizationListener(listener);
        }
        connected = false;
    }

    /**
     * Method that connects this bridge to the provider
     */
    public void connect() {
        if (connected) {
            return;
        }

        if (listener == null) {
            listener = this::fire;
        }
        parent.addLocalizationListener(listener);
        connected = true;
    }

    @Override
    public String getString(String key) {
        return parent.getString(key);
    }
}
