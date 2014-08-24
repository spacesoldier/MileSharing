package com.soloway.city.milesharing.activity.settings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.soloway.transport.milesharing.R;

/**
 * Created by pens on 24.08.14.
 */
public class SettingsFragment extends Fragment {

    private View fragmentView;
    private SeekBar seekBar;
    private TextView km;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        fragmentView = inflater.inflate(R.layout.settings_layout, container, false);
        if (fragmentView != null) {
            km = (TextView) fragmentView.findViewById(R.id.km);
            km.setText(" 100 " + getString(R.string.m));
            seekBar = (SeekBar) fragmentView.findViewById(R.id.seekBar);
            seekBar.setProgress(0);

            seekBar.incrementProgressBy(50);
            seekBar.setMax(900);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    km.setText(" " +String.valueOf(progress+100) + " " + getString(R.string.m));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
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
