package com.soloway.city.milesharing.fragments;

import com.soloway.transport.milesharing.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

public class NotifyFragment extends Fragment {
	ImageButton exitButton;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View viewHierarchy = inflater.inflate(R.layout.fragment_notification, container, false);
		exitButton = (ImageButton) viewHierarchy.findViewById(R.id.imageExit);
			
		
		exitButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						clos();
						//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
					}
		});
		
		return viewHierarchy;
		
	}
	
	public void clos(){
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();		
	}
	
}