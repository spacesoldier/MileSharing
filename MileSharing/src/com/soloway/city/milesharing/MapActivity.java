package com.soloway.city.milesharing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.soloway.city.milesharing.routing.GMapV2GetRouteDirection;
import com.soloway.transport.milesharing.R;

public class MapActivity extends FragmentActivity implements LocationListener, OnClickListener, /*OnMapClickListener,*/ OnMapLongClickListener {
 
    GoogleMap googleMap;
    
    SharedPreferences prefs;
 
    private Button btnOk;
    private Button btnCancel;
    private CustomList adapter;
    Integer[] imageId_p = {
  	      R.drawable.pass_icon,
  	     
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none,
  	      R.drawable.none
  	  
  	  };
  
    Integer[] imageId_d = {
    	      R.drawable.driver_icon,
    	     
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none,
    	      R.drawable.none
    	  
    	  };
    List<Overlay> mapOverlays;
    GeoPoint point1, point2;
    LocationManager locManager;
    Drawable drawable;
    org.w3c.dom.Document document;
    GMapV2GetRouteDirection v2GetRouteDirection;
    
    LatLng fromPosition;
    LatLng toPosition;
    MarkerOptions markerOptions;
    Location location ;
    
    
    private String[] drawerListViewItems;
    private ListView drawerListView;
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
 
        // Showing status
        if(status!=ConnectionResult.SUCCESS){ // Google Play Services are not available
 
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();
 
        }else { // Google Play Services are available
 
            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
 
            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();
 
            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);
 
            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
 
            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();
 
            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);
 
            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);
 
            if(location!=null){
                onLocationChanged(location);
            }
            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            
            googleMap.setOnMapLongClickListener(this);
            
            
            /////
            ////MY PART
            /////
            v2GetRouteDirection = new GMapV2GetRouteDirection();
            
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));
            
            drawerListViewItems = getResources().getStringArray(R.array.items_pass);
            
            // get ListView defined in activity_main.xml
            drawerListView = (ListView) findViewById(R.id.left_drawer);
     
            adapter = new CustomList(MapActivity.this, drawerListViewItems, imageId_p);
            drawerListView.setAdapter(adapter);
          
            
            Polyline line = googleMap.addPolyline(new PolylineOptions()
            .add(new LatLng(51.5, -0.1), new LatLng(40.7, -74.0), new LatLng(30.7, -70.0))
            .width(5)
            .color(Color.RED));
            
            
            markerOptions = new MarkerOptions();
            
            
            fromPosition = new LatLng(location.getLatitude(), location.getLongitude());
            
            LatLng ln = new LatLng(location.getLatitude(),location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ln));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            
            
            //добавляет полигонную линию
            
            
            /*
            drawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Toast.makeText(MapActivity.this, "You Clicked at " +drawerListViewItems[+ position], Toast.LENGTH_SHORT).show();
                }
            });
            */
          
        }
        
        mainMapActivity = this;
    }
    @Override
    public void onLocationChanged(Location location) {
 
//        TextView tvLocation = (TextView) findViewById(R.id.tv_location);
 
        // Getting latitude of the current location
        double latitude = location.getLatitude();
 
        // Getting longitude of the current location
        double longitude = location.getLongitude();
 
        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
 
    }
 
    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
 
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.pass_item:
            //newGame();
        	
        	 
        	drawerListViewItems = getResources().getStringArray(R.array.items_pass);
        	adapter.refreshItems( drawerListViewItems, imageId_p);
        	drawerListView.setAdapter(adapter);

        	return true;
        case R.id.driver_item:


        	drawerListViewItems = getResources().getStringArray(R.array.items_driver);
        	adapter.refreshItems( drawerListViewItems, imageId_d);
        	adapter.notifyDataSetChanged();
        	//drawerListView.setAdapter(adapter);
        	
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private Marker marker;
    private LinearLayout layout;
    private LinearLayout layout_top;
    
    private MapActivity mainMapActivity;
    int dur;
    int dis;
    private OnClickListener buttonClickListener = new OnClickListener() {

        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.btn_ok:
                    prefs = mainMapActivity.getSharedPreferences("com.droiddevlab.milesharing", Context.MODE_PRIVATE);
                    String userId = prefs.getString("user_id", null);
                    if (userId != null){
                    	showRegisterDialog();// if registered - authorise (optional), check balance, receive ticket
                    }
                    else {
                    	// registration
                    }
                	//showRegisterDialog();
                    break;
                case R.id.btn_cancel:
                    
                	if (marker != null) {
            			marker.remove();
            		}
            		
            		Button btn_ok = (Button) layout.findViewById(R.id.btn_ok);
            		if (btn_ok != null) layout.removeView(btn_ok);
            		Button button_cancel = (Button)layout.findViewById(R.id.btn_cancel);
            		if (button_cancel != null) layout.removeView(button_cancel);
            		noBtns = true;
                	
                    break;
                case View.NO_ID:
                default:
                    
                    break;
            }
        }
    };
    
    private boolean noBtns = true;
    
	@Override
	public void onMapLongClick(LatLng point) {
//		googleMap.addMarker(new MarkerOptions().position(point).title("Go"));
		
		
		if (marker != null) {
			marker.remove();
        }
        marker = googleMap.addMarker(new MarkerOptions()
                .position(point).title("Go")
                .draggable(true).visible(true));
        
        toPosition = new LatLng(point.latitude, point.longitude);
        
        GetRouteTask getRoute = new GetRouteTask();
        getRoute.execute();
        
        
       
        layout = (LinearLayout) findViewById(R.id.bottom_box);
        layout_top = (LinearLayout) findViewById(R.id.top_box);

        if (noBtns)
        {
        	
        	Button btnOk = new Button(this);
        	btnOk.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        	btnOk.setText("GO");
        	btnOk.setId(R.id.btn_ok);
        	layout.addView(btnOk);

        	Button btnCancel = new Button(this);
            btnCancel.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            btnCancel.setText("Cancel");
            btnCancel.setId(R.id.btn_cancel);
            layout.addView(btnCancel);
        	
            btnOk.setOnClickListener(buttonClickListener);
            btnCancel.setOnClickListener(buttonClickListener);
            
            noBtns = false;
        }
        
	}
	
	

	public void showRegisterDialog() {
    }
	
	
	private class GetRouteTask extends AsyncTask<String, Void, String> {
        
        private ProgressDialog Dialog;
        String response = "";
        @Override
        protected void onPreExecute() {
              Dialog = new ProgressDialog(MapActivity.this);
              Dialog.setMessage("Loading route...");
              Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
              //Get All Route values
                    document = v2GetRouteDirection.getDocument(fromPosition, toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
                    response = "Success";
              return response;

        }

        @Override
        protected void onPostExecute(String result) {
              googleMap.clear();
              if(response.equalsIgnoreCase("Success")){
            	  
             dur =  v2GetRouteDirection.getDurationValue(document);
             dis  =  v2GetRouteDirection.getDistanceValue(document);
              
              
              ArrayList<LatLng> directionPoint = v2GetRouteDirection.getDirection(document);
              PolylineOptions rectLine = new PolylineOptions().width(10).color(
                          Color.BLUE);

              for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
              }
             // distanceBetween(1,1,1,1);
             // Location.distanceBetween(startLatitude, startLongitude, endLatitude, endLongitude, results);
              
              // Adding route on the map
              googleMap.addPolyline(rectLine);
              markerOptions.position(toPosition);
              markerOptions.draggable(true);
              googleMap.addMarker(markerOptions);

              }
             
              Dialog.dismiss();
        }
  }
	private String getRegData(){
		View transactionLayout = View.inflate(this, R.layout.register_dlg, null);

	    // Start with the payment amount
		EditText lastName = (EditText) transactionLayout.findViewById(R.id.lastname);
	    EditText firstName = (EditText) transactionLayout.findViewById(R.id.firstname);
	    EditText username = (EditText) transactionLayout.findViewById(R.id.username);
	    EditText password = (EditText) transactionLayout.findViewById(R.id.password);
	    
	    String result = "&first_name="+firstName.getText()+
	    		"&second_name="+lastName.getText()+
	    		"&login="+username.getText()+
	    		"&password="+password.getText();
	    return result;
	}
	
	private NameValuePair getLastName(){
		View transactionLayout = View.inflate(this, R.layout.register_dlg, null);
		EditText lastName = (EditText) transactionLayout.findViewById(R.id.lastname);
		return new BasicNameValuePair("second_name", lastName.getText().toString());
	}
	
	private NameValuePair getFirstName(){
		View transactionLayout = View.inflate(this, R.layout.register_dlg, null);
		EditText lastName = (EditText) transactionLayout.findViewById(R.id.firstname);
		return new BasicNameValuePair("first_name", lastName.getText().toString());
	}
	
	private NameValuePair getUserName(){
		View transactionLayout = View.inflate(this, R.layout.register_dlg, null);
		EditText lastName = (EditText) transactionLayout.findViewById(R.id.username);
		return new BasicNameValuePair("login", lastName.getText().toString());
	}
	
	private NameValuePair getPassword(){
		View transactionLayout = View.inflate(this, R.layout.register_dlg, null);
		EditText lastName = (EditText) transactionLayout.findViewById(R.id.password);
		return new BasicNameValuePair("password", lastName.getText().toString());
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
}
