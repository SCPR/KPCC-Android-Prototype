package org.kpcc.reader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerItem>
{
    private LayoutInflater mInflater;

    public NavDrawerAdapter(Context context, int textViewResourceId, NavDrawerItem[] objects)
    {
        super(context, textViewResourceId, objects);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;
        NavDrawerItem menuItem = getItem(position);

        // Is this an item or a section?
        if (menuItem.getType() == (NavMenuItem.ITEM_TYPE))
        {
            view = getItemView(convertView, parent, menuItem);
        } else {
            view = getSectionView(convertView, parent, menuItem);
        }

        return view;
    }


    public View getItemView(View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem)
    {
        NavMenuItem menuItem = (NavMenuItem) navDrawerItem;
        NavMenuItemHolder navMenuItemHolder = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.drawer_item, parentView, false);
            TextView labelView = (TextView) convertView.findViewById(R.id.nav_menu_item_label);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.nav_menu_item_icon);

            navMenuItemHolder = new NavMenuItemHolder();
            navMenuItemHolder.setLabelView(labelView);
            navMenuItemHolder.setIconView(iconView);

            convertView.setTag(navMenuItemHolder);
        }

        if (navMenuItemHolder == null)
        {
            navMenuItemHolder = (NavMenuItemHolder) convertView.getTag();
        }

        navMenuItemHolder.getLabelView().setText(menuItem.getLabel());
        navMenuItemHolder.getIconView().setImageResource(menuItem.getIcon());

        return convertView;
    }


    public View getSectionView(View convertView, ViewGroup parentView, NavDrawerItem navDrawerItem)
    {
        NavMenuSection menuSection = (NavMenuSection) navDrawerItem;
        NavMenuSectionHolder navMenuSectionHolder = null;

        if (convertView == null)
        {
            convertView = mInflater.inflate(R.layout.drawer_section, parentView, false);
            TextView labelView = (TextView) convertView.findViewById(R.id.nav_menu_section_label);

            navMenuSectionHolder = new NavMenuSectionHolder();
            navMenuSectionHolder.setLabelView(labelView);

            convertView.setTag(navMenuSectionHolder);
        }

        if (navMenuSectionHolder == null)
        {
            navMenuSectionHolder = (NavMenuSectionHolder) convertView.getTag();
        }

        navMenuSectionHolder.getLabelView().setText(menuSection.getLabel());

        return convertView;
    }


    @Override
    public int getViewTypeCount()
    {
        return 2;
    }


    @Override
    public int getItemViewType(int position)
    {
        return getItem(position).getType();
    }


    @Override
    public boolean isEnabled(int position)
    {
        return getItem(position).isEnabled();
    }


    private static class NavMenuItemHolder
    {

        private TextView mLabelView;
        private ImageView mIconView;


        private TextView getLabelView()
        {
            return mLabelView;
        }

        private void setLabelView(TextView labelView)
        {
            mLabelView = labelView;
        }


        private ImageView getIconView()
        {
            return mIconView;
        }

        private void setIconView(ImageView iconView)
        {
            mIconView = iconView;
        }

    }


    private static class NavMenuSectionHolder
    {

        private TextView mLabelView;


        private TextView getLabelView()
        {
            return mLabelView;
        }

        private void setLabelView(TextView labelView)
        {
            mLabelView = labelView;
        }

    }

}
