package com.soloway.city.milesharing.activity.rout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.soloway.transport.milesharing.R;

/**
 * Created by pens on 24.08.14.
 */
public class RoutFragment extends Fragment {

    private View fragmentView;
    private TextView distanceTextView;
    private TextView timeTextView;
    private float distance;
    private int time;

    public static RoutFragment newInstance(float distance, int time) {
        Bundle bundle = new Bundle();
        bundle.putFloat("distance", distance);
        bundle.putInt("time", time);
        return new RoutFragment();
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.rout_layout, container, false);
        if (fragmentView != null) {
            distanceTextView = (TextView) fragmentView.findViewById(R.id.distance);
            timeTextView = (TextView) fragmentView.findViewById(R.id.time);
            if (distance > 0) {
                distanceTextView.setText(String.valueOf(distance) + " " + getString(R.string.km));
            }
            if (time > 0) {
                timeTextView.setText(String.valueOf(time) + " " + getString(R.string.min));
            }
        }
        return fragmentView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }

        fragmentView = initView(inflater, container);
        return fragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            distance = args.getFloat("distance", 0);
            time = args.getInt("time", 0);
        }
    }

}
