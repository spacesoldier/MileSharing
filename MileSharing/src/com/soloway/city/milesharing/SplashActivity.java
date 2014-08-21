package com.soloway.city.milesharing;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.soloway.transport.milesharing.MainMilesharingActivity;
import com.soloway.transport.milesharing.R;


public class SplashActivity extends Activity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void onResume() {
	  super.onResume();
	  if (checkPlayServices()) {
		  ImageView tapNext = (ImageView) findViewById(R.id.splashImage);
		  tapNext.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {
//			        Intent myIntent=new Intent(view.getContext(),MapActivity.class );
			        Intent myIntent=new Intent(view.getContext(),MainMapActivity.class );
//			        Intent myIntent=new Intent(view.getContext(),MainMilesharingActivity.class );
			        //myIntent.putExtra("prefs", PREFS_NAME);
			        startActivity(myIntent);
			        finish();
			    }
	        });
		  
	  }
	}
	
	private boolean checkPlayServices() {
		  int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		  if (status != ConnectionResult.SUCCESS) {
		    if (GooglePlayServicesUtil.isUserRecoverableError(status)) {
		      showErrorDialog(status);
		    } else {
		      Toast.makeText(this, "This device is not supported.", 
		          Toast.LENGTH_LONG).show();
		      finish();
		    }
		    return false;
		  }
		  return true;
		} 

		void showErrorDialog(int code) {
		  GooglePlayServicesUtil.getErrorDialog(code, this, 
		      REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
		}
		
		static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 1001;

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  switch (requestCode) {
		    case REQUEST_CODE_RECOVER_PLAY_SERVICES:
		      if (resultCode == RESULT_CANCELED) {
		        Toast.makeText(this, "Google Play Services must be installed.",
		            Toast.LENGTH_SHORT).show();
		        finish();
		      }
		      return;
		  }
		  super.onActivityResult(requestCode, resultCode, data);
		}


}
