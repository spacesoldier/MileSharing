package com.soloway.city.milesharing.activity.navigationDrawer;

import android.widget.BaseAdapter;
import com.soloway.transport.milesharing.R;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class NavigationDrawerAdapter extends BaseAdapter {
    private String[] titles;

    public NavigationDrawerAdapter(Activity context) {
        titles = context.getResources().getStringArray(R.array.menu_items);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public String getItem(int position) {
        return titles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String  title = getItem(position);
        NavigationDrawerView view = (convertView == null) ? new NavigationDrawerView(parent.getContext()) : (NavigationDrawerView) convertView;
        view.bindData(title);
        return view;
    }
}