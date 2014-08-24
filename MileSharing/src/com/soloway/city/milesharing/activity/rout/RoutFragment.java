package com.soloway.city.milesharing.activity.rout;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.gms.maps.model.LatLng;
import com.soloway.transport.milesharing.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by pens on 24.08.14.
 */
public class RoutFragment extends Fragment {

    private View fragmentView;
    private TextView distanceTextView;
    private TextView timeTextView;
    private TextView fromTextView;
    private TextView toTextView;
    private float distance;
    private int time;
    private LatLng from;
    private LatLng to;

    public static RoutFragment newInstance(float distance, int time, LatLng from, LatLng to) {
        RoutFragment routFragment = new RoutFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat("distance", distance);
        bundle.putInt("time", time);
        bundle.putParcelable("from", from);
        bundle.putParcelable("to", to);
        routFragment.setArguments(bundle);
        return routFragment;
    }

    private View initView(LayoutInflater inflater, ViewGroup container) {
        View fragmentView = inflater.inflate(R.layout.rout_layout, container, false);
        if (fragmentView != null) {
            distanceTextView = (TextView) fragmentView.findViewById(R.id.distance);
            timeTextView = (TextView) fragmentView.findViewById(R.id.time);
            toTextView = (TextView) fragmentView.findViewById(R.id.whereTo);
            fromTextView = (TextView) fragmentView.findViewById(R.id.whereFrom);


            distanceTextView.setText(String.valueOf(distance) + " " + getString(R.string.km));
            timeTextView.setText(String.valueOf(time) + " " + getString(R.string.min));
            if (from != null) {
                fromTextView.setText(location2Adress(from));
            }
            if (to != null) {
                toTextView.setText(location2Adress(to));
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
            from = args.getParcelable("from");
            to = args.getParcelable("to");
        }
    }

    private StringBuilder location2Adress(LatLng coord) {
        StringBuilder strReturnedAddress = new StringBuilder();
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(coord.latitude, coord.longitude, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append(i < returnedAddress.getMaxAddressLineIndex() - 1 ? ", " : "");
                }
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strReturnedAddress;
    }
}
