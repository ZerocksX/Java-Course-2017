package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that implements basic {@link ILocalizationProvider} methods
 *
 * @author Pavao Jerebić
 */
public abstract class AbstractLocalizationProvider implements ILocalizationProvider {

    /**
     * Listeners
     */
    private List<ILocalizationListener> listeners;

    /**
     * Constructor that initializes listeners list
     */
    public AbstractLocalizationProvider() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addLocalizationListener(ILocalizationListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeLocalizationListener(ILocalizationListener listener) {
        listeners.remove(listener);
    }

    /**
     * Method that should be called when a change happens, triggers all listeners
     */
    public void fire() {
        List<ILocalizationListener> finalListeners = new ArrayList<>(listeners);
        for (ILocalizationListener listener : finalListeners) {
            listener.localizationChanged();
        }
    }
}
