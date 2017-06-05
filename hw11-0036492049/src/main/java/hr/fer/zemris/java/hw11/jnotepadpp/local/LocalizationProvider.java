package hr.fer.zemris.java.hw11.jnotepadpp.local;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * {@link ILocalizationProvider} implementation as a Singleton<br/>
 * Use {@link #getInstance()} for getting a current instance
 *
 * @author Pavao Jerebić
 */
public class LocalizationProvider extends AbstractLocalizationProvider {
    /**
     * Current language
     */
    private String language;
    /**
     * Resource bundle
     */
    private ResourceBundle bundle;
    /**
     * Current {@link LocalizationProvider} instance
     */
    private static LocalizationProvider instance;

    /**
     * Constructor that sets language to english, and its respective resource bundle
     */
    private LocalizationProvider() {
        language = "en";
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle(
                "hr.fer.zemris.java.hw11.jnotepadpp.local.translations",
                locale
        );
    }

    /**
     * Returns current instance
     * Important: not thread safe
     *
     * @return current instance
     */
    public static LocalizationProvider getInstance() {
        if (instance == null) {
            instance = new LocalizationProvider();
        }
        return instance;
    }

    /**
     * Sets language and updates resource bundle
     *
     * @param language language
     * @throws IllegalArgumentException           if language is null
     * @throws java.util.MissingResourceException if a resource does not exist
     */
    public void setLanguage(String language) {
        if (language == null) {
            throw new IllegalArgumentException();
        }
        this.language = language;
        Locale locale = Locale.forLanguageTag(language);
        bundle = ResourceBundle.getBundle(
                "hr.fer.zemris.java.hw11.jnotepadpp.local.translations",
                locale
        );
        fire();
    }

    /**
     * Getter for language
     *
     * @return language
     */
    public String getLanguage() {
        return language;
    }

    @Override
    public String getString(String key) {
        if (key == null) {
            throw new IllegalArgumentException();
        }
        return bundle.getString(key);
    }
}
