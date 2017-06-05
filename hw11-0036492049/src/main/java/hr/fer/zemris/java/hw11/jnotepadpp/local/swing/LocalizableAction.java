package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;

/**
 * Localized {@link AbstractAction}
 *
 * @author Pavao Jerebić
 */
public abstract class LocalizableAction extends AbstractAction {

    /**
     * Key
     */
    private String key;
    /**
     * Provider
     */
    private ILocalizationProvider prov;

    /**
     * Constructor that sets key and provider and adds a {@link hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationListener}
     *
     * @param key  key
     * @param prov provider
     */
    public LocalizableAction(String key, ILocalizationProvider prov) {
        this.key = key;
        this.prov = prov;
        prov.addLocalizationListener(() -> {
            putValue(Action.NAME, prov.getString(key));
            putValue(Action.SHORT_DESCRIPTION, prov.getString(key + "_sd"));
        });
    }

    /**
     * Getter for key
     *
     * @return key
     */
    public String getKey() {
        return key;
    }

    /**
     * Getter for provider
     *
     * @return provider
     */
    public ILocalizationProvider getProv() {
        return prov;
    }
}
