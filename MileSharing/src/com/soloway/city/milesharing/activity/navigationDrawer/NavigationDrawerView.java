package com.soloway.city.milesharing.activity.navigationDrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.soloway.transport.milesharing.R;

/**
 * Created by pens on 23.08.14.
 */
public class NavigationDrawerView extends RelativeLayout {

        private TextView title;

        public NavigationDrawerView(Context c) {
        super(c);
        LayoutInflater inflater = LayoutInflater.from(c);
        if (inflater != null) {
            inflater.inflate(R.layout.navigation_drawer_item, this);

            title = (TextView)findViewById(R.id.title);
        }
    }

    public void bindData(String text) {
        title.setText(text);
//        joinButton.setTag(getTag());
//        joinButton.setOnClickListener(onJoinClickListener);
    }
}
