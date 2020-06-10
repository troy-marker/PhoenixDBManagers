package com.phoenixhosman.phoenixdbmanagers;

public class ObjectSubMenu {
    /**
     * The name of the sub-menu
     */
    private String mName;
    /**
     * The name of the parent menu
     */
    private String mParent;
    /**
     * Class constructor
     *
     * @param parent The name of the parent menu
     * @param name The menu name
     */
    ObjectSubMenu(String parent, String name) {
        this.mParent = parent;
        this.mName = name;
    }
    /**
     * Get the parent name
     *
     * @return Sting - the menu items parent
     */
    String getParent() {
        return mParent;
    }
    /**
     * Get the menu name
     *
     * @return String - the menu name
     */
    public String getName() {
        return mName;
    }

}
