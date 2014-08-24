package com.soloway.city.milesharing.activity.journal;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.soloway.transport.milesharing.R;

/**
 * Created by pens on 24.08.14.
 */
public class JournalFragment extends Fragment {

    private View fragmentView;

    public static JournalFragment newInstance() {
        return new JournalFragment();
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        fragmentView = inflater.inflate(R.layout.journal_layout, container, false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        initView(inflater, container);
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
