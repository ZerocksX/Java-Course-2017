package hr.fer.zemris.java.hw11.jnotepadpp.local;

/**
 * Localization provider
 *
 * @author Pavao Jerebić
 */
public interface ILocalizationProvider {
    /**
     * Adds listener to a private collection
     *
     * @param listener listener
     */
    void addLocalizationListener(ILocalizationListener listener);

    /**
     * Removes given listener from private collection
     *
     * @param listener listener
     */
    void removeLocalizationListener(ILocalizationListener listener);

    /**
     * Gets string from localization properties
     *
     * @param key key
     * @return string representing key in certain localization properties
     */
    String getString(String key);
}
