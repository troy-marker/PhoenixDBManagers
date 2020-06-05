package com.phoenixhosman.phoenixdbmanagers;

/**
 * The type Object menu.
 */
public class ObjectMenu {
    /**
     * Private string holder variable for the menu name
     */
    private String mName;

    /**
     * Constructor for a menu item
     */
    ObjectMenu(String name) {
        mName = name;
    }

    /**
     * Gets the menu name.
     *
     * @return String - the menu name
     */
    public String getName() {
        return mName;
    }
}
