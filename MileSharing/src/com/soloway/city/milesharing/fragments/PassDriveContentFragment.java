package com.soloway.city.milesharing.fragments;

import com.soloway.transport.milesharing.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PassDriveContentFragment extends Fragment {
	private Button cancelButton;
	private Button goButton;
	private SeekBar seekBar;
	private TextView tvLength;
	private PassDriveFragment parent;
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View viewHierarchy = inflater.inflate(R.layout.fragment_pdcontent, container, false);
		cancelButton = (Button) viewHierarchy.findViewById(R.id.buttonCancel);
		goButton = (Button) viewHierarchy.findViewById(R.id.buttonGo);
		seekBar = (SeekBar) viewHierarchy.findViewById(R.id.seekBar);
		seekBar.setProgress(200);
		
		seekBar.incrementProgressBy(50);
		seekBar.setMax(2000);
		
		tvLength = (TextView) viewHierarchy.findViewById(R.id.textLength);
		tvLength.setText("200 m");
		parent = (PassDriveFragment) getParentFragment();
		
		seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

		    @Override
		    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		        progress = progress / 10;
		        progress = progress * 10;
		        tvLength.setText(String.valueOf(progress)+" m");
		    }

		    @Override
		    public void onStartTrackingTouch(SeekBar seekBar) {

		    }

		    @Override
		    public void onStopTrackingTouch(SeekBar seekBar) {

		    }
		});
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				clos();
			}
		});
		
		goButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				parent.go();
			}
		});
		
		
		String mytag = this.getTag();

		
		return viewHierarchy;
		
	}
	
	public void setParent(PassDriveFragment par){
		parent = par;
	}
	
	public void clos(){
		//parent = (PassDriveFragment) getParentFragment();
		//parent = (PassDriveFragment)  getFragmentManager().findFragmentByTag("fragmentPassDriver");
		
		parent.clos();
		//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();		
	}
	
}