package org.scpr.reader;

// A Section header in the Nav Drawer.
public class NavMenuSection implements NavDrawerItem
{

    public static final int SECTION_TYPE = 0;
    private int mId;
    private String mLabel;


    private NavMenuSection()
    {
    }

    public static NavMenuSection create(int id, String label)
    {
        NavMenuSection section = new NavMenuSection();
        section.setId(id);
        section.setLabel(label);
        return section;
    }


    @Override
    public int getType()
    {
        return SECTION_TYPE;
    }


    public String getLabel()
    {
        return mLabel;
    }

    public void setLabel(String label)
    {
        mLabel = label;
    }


    @Override
    public boolean isEnabled()
    {
        return false;
    }


    public int getId()
    {
        return mId;
    }

    public void setId(int id)
    {
        mId = id;
    }

}
