package org.scpr.reader;


import android.content.Context;


// A clickable item in the Nav Drawer.
public class NavMenuItem implements NavDrawerItem
{

    public static final int ITEM_TYPE = 1;

    private int mId;
    private String mLabel;
    private int mIcon;

    private NavMenuItem()
    {
    }

    public static NavMenuItem create(int id, String label, String icon, Context context)
    {
        NavMenuItem item = new NavMenuItem();
        item.setId(id);
        item.setLabel(label);

        item.setIcon(
            context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));

        return item;
    }


    @Override
    public int getType()
    {
        return ITEM_TYPE;
    }


    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }


    public String getLabel()
    {
        return mLabel;
    }

    public void setLabel(String label)
    {
        mLabel = label;
    }


    public int getIcon()
    {
        return mIcon;
    }

    public void setIcon(int icon)
    {
        mIcon = icon;
    }


    @Override
    public boolean isEnabled()
    {
        return true;
    }

}
