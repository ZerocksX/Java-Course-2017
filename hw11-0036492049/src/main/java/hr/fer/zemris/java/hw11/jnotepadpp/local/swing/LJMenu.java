package hr.fer.zemris.java.hw11.jnotepadpp.local.swing;

import hr.fer.zemris.java.hw11.jnotepadpp.local.ILocalizationProvider;

import javax.swing.*;

/**
 * Localized {@link JMenu}
 *
 * @author Pavao Jerebić
 */
public class LJMenu extends JMenu {
    /**
     * Key
     */
    private String key;
    /**
     * Localization provider
     */
    private ILocalizationProvider prov;

    /**
     * Constructor that sets text, key and provider
     *
     * @param s    text
     * @param key  key
     * @param prov provider
     */
    public LJMenu(String s, String key, ILocalizationProvider prov) {
        super(s);
        this.key = key;
        this.prov = prov;
        prov.addLocalizationListener(this::updateMenu);
    }

    /**
     * Constructor that sets key and provider
     *
     * @param key  key
     * @param prov provider
     */
    public LJMenu(String key, ILocalizationProvider prov) {
        this("", key, prov);
    }

    /**
     * Helping method that updates this menu
     */
    private void updateMenu() {
        setText(prov.getString(key));
    }
}
