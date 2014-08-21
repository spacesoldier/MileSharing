package com.soloway.city.milesharing.fragments;

import com.soloway.city.milesharing.MainMapActivity;
import com.soloway.transport.milesharing.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.TabSpec;

public class PassDriveFragment extends Fragment {
    private FragmentTabHost mTabHost;
    private PassDriveContentFragment frp,frd;
    //Mandatory Constructor
   
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_passdrive,container, false);


        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        //frp = new PassDriveContentFragment();
        //frd = new PassDriveContentFragment();
        //frp.setParent(this);
        //frd.setParent(this);
        
        setTargetFragment((Fragment) this, 1000);
        
        TabSpec photospec = mTabHost.newTabSpec("Photos");
     // setting Title and Icon for the Tab
        photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.driver_icon));
        //Intent photosIntent = new Intent(this, PhotosActivity.class);
       // mTabHost.addTab(arg0, arg1, arg2)
        //photospec.setContent(viewId)
        /*
        TabSpec spec = mTabHost.newTabSpec("positions").setIndicator("Positions",
        		getResources().getDrawable(R.drawable.pass_icon))
                .setContent(R.id.LayoutTabPassDrive);
        mTabHost.addTab(spec);
        
        
        TabSpec spec2 = mTabHost.newTabSpec("positions").setIndicator("Positions11",
        		getResources().getDrawable(R.drawable.pass_icon))
                .setContent(R.id.LayoutTabPassDrive);
        mTabHost.addTab(spec2);
        */
        
        mTabHost.addTab(mTabHost.newTabSpec("tabPass").setIndicator("Пассажир"),
        		PassDriveContentFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("tabDrive").setIndicator("Водитель"),
        		PassDriveContentFragment.class, null);


        return rootView;
    }
    
    public void go(){
    	MainMapActivity mma = (MainMapActivity) getActivity();
    	mma.showLoginDialog(true, false);
    	//clos();
		//getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();		
	}
    
    
    public void clos(){
		getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();		
	}
}