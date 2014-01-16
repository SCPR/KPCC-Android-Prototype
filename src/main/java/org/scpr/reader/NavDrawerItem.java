package org.scpr.reader;

// An interface for a row in the Nav Drawer.
// For example, a section or a clickable menu item could implement this.
public interface NavDrawerItem
{

    public int getId();
    public String getLabel();
    public int getType();
    public boolean isEnabled();

}
