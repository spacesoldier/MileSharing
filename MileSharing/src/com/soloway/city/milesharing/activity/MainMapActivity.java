package com.soloway.city.milesharing.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.util.Log;
import android.widget.*;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.*;
import com.soloway.city.milesharing.activity.cash.CashFragment;
import com.soloway.city.milesharing.activity.companion.CompanionFragment;
import com.soloway.city.milesharing.activity.journal.JournalFragment;
import com.soloway.city.milesharing.activity.navigationDrawer.NavigationDrawerFragment;
import com.soloway.city.milesharing.activity.profile.MyProfileFragment;
import com.soloway.city.milesharing.activity.rout.RoutFragment;
import com.soloway.city.milesharing.core.RoleHelper;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.gson.Gson;
import com.soloway.city.milesharing.api.UserProfile;
import com.soloway.city.milesharing.api.UserSession;
import com.soloway.city.milesharing.api.UserSessionManager;
import com.soloway.city.milesharing.fragments.NotifyFragment;
import com.soloway.city.milesharing.fragments.PassDriveFragment;
import com.soloway.city.milesharing.routing.GMapV2GetRouteDirection;
import com.soloway.transport.milesharing.R;

public class MainMapActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks, LocationListener,
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, OnMapLongClickListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in
     * {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    GoogleMap googleMap;

    private org.w3c.dom.Document document;
    private GMapV2GetRouteDirection v2GetRouteDirection;

    LatLng fromPosition;
    LatLng toPosition;
    MarkerOptions markerOptions;
    Location location;
    LocationManager locationManager;
    LocationClient mLocationClient;

    SharedPreferences prefs;
    UserSession session;
    UserSessionManager sessionManager;

    private MyApplication myApp;

    private Marker marker;
    private RelativeLayout infoLayout;
    private RelativeLayout mapLayout;
    private TextView tvDis;
    private TextView tvDur;
    private Button btnGo;
    private ImageView btnClose;

    int dur;
    int dis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_map);


        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        infoLayout = (RelativeLayout) findViewById(R.id.info);
        mapLayout = (RelativeLayout) findViewById(R.id.mapLayout);
        tvDis = (TextView) findViewById(R.id.distance);
        tvDur = (TextView) findViewById(R.id.time);
        btnGo = (Button) findViewById(R.id.go);
        btnClose = (ImageView) findViewById(R.id.close);
        myApp = (MyApplication) getApplicationContext();
        // MAP STUFF

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        // Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment of activity_main.xml
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);

            // Getting GoogleMap object from the fragment
            googleMap = fm.getMap();

            // Enabling MyLocation Layer of Google Map
            googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service
            // LOCATION_SERVICE
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Last Location
            // Location location =
            // locationManager.getLastKnownLocation(provider);

            locationManager.requestLocationUpdates(provider, 20000, 0, this);
            googleMap.setOnMapLongClickListener(this);
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    infoLayout.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            // ///
            // //MY PART
            // ///
            v2GetRouteDirection = new GMapV2GetRouteDirection();

            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.getUiSettings().setAllGesturesEnabled(true);
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            markerOptions = new MarkerOptions();

            if (location != null) {
                fromPosition = new LatLng(location.getLatitude(),
                        location.getLongitude());
            } else {
                mLocationClient = new LocationClient(this, this, this);//
            }

            // /Check previous session
            prefs = this.getSharedPreferences("com.soloway.city.milesharing",
                    Context.MODE_PRIVATE);
            String sessionJson = prefs.getString("USER_SESSION", "");
            Gson gson = new Gson();
            session = gson.fromJson(sessionJson, UserSession.class);

            if (session == null) {
                session = new UserSession();
            }
            prefs.edit().putString("USER_SESSION", gson.toJson(session));

            sessionManager = new UserSessionManager();

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);

        fromPosition = latLng;

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

    private boolean noBtns = true;

    private void showNotifyDialog() {
        FragmentManager fragMan = getSupportFragmentManager();
        FragmentTransaction fragTransaction = fragMan.beginTransaction();
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mapLayout);
        Fragment myFrag = new NotifyFragment();
        fragTransaction.add(rl.getId(), myFrag, "fragmentNotify");
        fragTransaction.commit();
    }

    private void showRole() {
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.mapLayout);
        Fragment fragPD = new PassDriveFragment();
        getSupportFragmentManager().beginTransaction()
                .add(rl.getId(), fragPD, "fragmentPassDriver").commit();
    }

    @Override
    public void onMapLongClick(LatLng point) {

        if (googleMap != null) {
            toPosition = new LatLng(point.latitude, point.longitude);
            googleMap.clear();
            GetRouteTask getRoute = new GetRouteTask();
            getRoute.execute();

            if (marker != null) {
                marker.remove();
            }

            marker = googleMap.addMarker(new MarkerOptions().position(point).draggable(false).visible(true));

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder = builder.include(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()));
            builder = builder.include(toPosition);
            LatLngBounds bounds = builder.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, this.getResources()
                    .getDisplayMetrics().widthPixels,
                    this.getResources().getDisplayMetrics().heightPixels, 100);
            googleMap.animateCamera(cu);
        }
    }


    //DialogInterface loginDialog = null;
    AlertDialog loginDialog = null;

    UserSession tmpSession = null;
    TextView errorTextView_global = null;

    public void hideLoginDialog() {

        if (tmpSession.isOnline()) {
            if (errorTextView_global != null && errorTextView_global.getVisibility() == View.VISIBLE) {
                Animation animation = new TranslateAnimation(0,
                        0, 0, 1000);
                animation.setDuration(1000);
                errorTextView_global.startAnimation(animation);
                errorTextView_global.setVisibility(View.GONE);
            }

            session.setSessionId(tmpSession.getSessionId());
            session.setTokenId(tmpSession.getTokenId());
            session.setUserId(tmpSession.getUserId());

            session.setOnline(tmpSession.isOnline());

            dialog.dismiss();

        } else {

            // mainMapActivity.showLoginDialog(false, false);

        }


        if (loginDialog != null) {
            loginDialog.dismiss();
        }
    }

    public void showLoginDialog(final boolean first_attempt, final boolean reg) {

        final AlertDialog.Builder loginRegDialog = new AlertDialog.Builder(this);
        loginDialog = loginRegDialog.create();

        loginRegDialog.setIcon((reg) ? R.drawable.ic_user_create
                : R.drawable.ic_user_login);
        loginRegDialog.setTitle((reg) ? R.string.user_register_title
                : R.string.user_login_title);

        View linearlayout = getLayoutInflater().inflate(
                R.layout.fragment_user_login, null);
        loginRegDialog.setView(linearlayout);

        final EditText loginEdit = (EditText) linearlayout
                .findViewById(R.id.txt_your_login);
        final EditText passEdit = (EditText) linearlayout
                .findViewById(R.id.txt_your_pass);
        final TextView errorTextView = (TextView) linearlayout
                .findViewById(R.id.lbl_login_error);
        errorTextView_global = errorTextView;
        final LinearLayout signUpLayout = (LinearLayout) linearlayout
                .findViewById(R.id.new_user_call);
        final LinearLayout newUserData = (LinearLayout) linearlayout
                .findViewById(R.id.new_user_data);
        final Button registerEnable = (Button) linearlayout
                .findViewById(R.id.newUserCall);
        errorTextView.setTextColor(Color.parseColor("#FF3322"));
        errorTextView.setVisibility(View.GONE);
        signUpLayout.setVisibility(View.GONE);
        newUserData.setVisibility((reg) ? View.VISIBLE : View.GONE);
        if (!first_attempt && reg) {
            passEdit.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                    | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

        registerEnable.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                loginRegDialog.setIcon(R.drawable.ic_user_create);
                loginRegDialog.setTitle(R.string.user_register_title);

                Animation animation = new TranslateAnimation(0, 0, 0, 1000);
                animation.setDuration(1000);
                errorTextView.startAnimation(animation);
                signUpLayout.startAnimation(animation);
                newUserData.startAnimation(animation);
                errorTextView.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.GONE);
                newUserData.setVisibility(View.VISIBLE);

                myApp.viewToClick.performClick();
                showLoginDialog(false, true);

            }
        });

        if (!first_attempt && !reg) {
            Animation animation = new TranslateAnimation(0, 0, 0, 1000);
            animation.setDuration(1000);
            errorTextView.startAnimation(animation);
            signUpLayout.startAnimation(animation);
            errorTextView.setVisibility(View.VISIBLE);
            signUpLayout.setVisibility(View.VISIBLE);
        }

        loginRegDialog.setPositiveButton(
                (reg) ? R.string.register : R.string.login,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        UserProfile authUser = new UserProfile();
                        if (reg) {

                            EditText firstNameEdit = (EditText) newUserData
                                    .findViewById(R.id.txt_first_name);
                            authUser.setFirstName(firstNameEdit.getText()
                                    .toString());
                            EditText lastNameEdit = (EditText) newUserData
                                    .findViewById(R.id.txt_last_name);
                            authUser.setSecondName(lastNameEdit.getText()
                                    .toString());

                            EditText emailEdit = (EditText) newUserData
                                    .findViewById(R.id.txt_email);
                            authUser.setEmail(emailEdit.getText().toString());

                        }
                        authUser.setUserLogin(loginEdit.getText().toString());

                        String hashPass = passEdit.getText().toString();
                        MessageDigest md;
                        try {
                            md = MessageDigest.getInstance("MD5");
                            md.update(passEdit.getText().toString().getBytes());

                            byte[] digest = md.digest();
                            StringBuilder sb = new StringBuilder();
                            for (int i = 0; i < digest.length; i++) {
                                sb.append(Integer.toString(
                                        (digest[i] & 0xff) + 0x100, 16)
                                        .substring(1));
                            }

                            hashPass = sb.toString();

                        } catch (NoSuchAlgorithmException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        authUser.setUserPassword(hashPass);

//						UserSession tmpSession = null;

                        if (reg) {
                            tmpSession = sessionManager.pushUser(authUser);
                        } else {
                            tmpSession = sessionManager.authUser(authUser,
                                    session);
                        }


                    }
                })

                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        loginRegDialog.create();
        AlertDialog dlg = loginRegDialog.show();

        myApp.viewToClick = dlg.getButton(DialogInterface.BUTTON_NEGATIVE);

    }

    private ProgressDialog dialog;

    class LoginRequestTask extends AsyncTask<UserProfile, String, String> {

        @Override
        protected String doInBackground(UserProfile... params) {

            try {
                DefaultHttpClient hc = new DefaultHttpClient();
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpPost postMethod = new HttpPost(
                        "http://78.47.251.3/users.php?push_user"
                                + params[0].getRegData());

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
                        2);

                String response = hc.execute(postMethod, res);

            } catch (Exception e) {
                System.out.println("Exp=" + e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {

            dialog = new ProgressDialog(MainMapActivity.this);
            dialog.setMessage("Загружаю данные...");
            dialog.setIndeterminate(true);
            dialog.setCancelable(true);
            dialog.show();
            super.onPreExecute();
        }
    }

    // ROUTE LOAD SPLASH
    private class GetRouteTask extends AsyncTask<String, Void, String> {

        private void showLogin() {
            if (prefs != null) {
                Gson gson = new Gson();
                String json = prefs.getString("USER_SESSION", "");
                session = gson.fromJson(json, UserSession.class);

                if (session != null) {
                    // attempt to login or register
                    boolean first_attempt = true;
                    showLoginDialog(first_attempt, false);

                } else {
                    // nice. is it the first launch? Ok. let's create session!
                    session = new UserSession();
                    Editor prefsEditor = prefs.edit();
                    Gson gsonBuffer = new Gson();
                    String jsonBuffer = gsonBuffer.toJson(session);
                    prefsEditor.putString("USER_SESSION", jsonBuffer);
                    prefsEditor.commit();

                    // cool. so, it's time to login or register. isn't it?
                    boolean first_attempt = true;
                    showLoginDialog(first_attempt, false);
                }

            }
        }

        private ProgressDialog Dialog;
        String response = "";

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(MainMapActivity.this);
            Dialog.setMessage("Loading route...");
            Dialog.show();
        }

        @Override
        protected String doInBackground(String... urls) {
            // Get All Route values
            document = v2GetRouteDirection.getDocument(fromPosition,
                    toPosition, GMapV2GetRouteDirection.MODE_DRIVING);
            response = "Success";
            return response;

        }

        @Override
        protected void onPostExecute(String result) {
            // googleMap.clear();
            if (response.equalsIgnoreCase("Success")) {

                dur = v2GetRouteDirection.getDurationValue(document);
                dis = v2GetRouteDirection.getDistanceValue(document);

                tvDis.setText(getString(R.string.distance) + " "
                        + String.valueOf((float) dis / 1000)
                        + " " + getString(R.string.km));
                tvDur.setText(getString(R.string.time) + " "
                        + String.valueOf((int) dur / 60)
                        + " " + getString(R.string.min));

                infoLayout.setVisibility(View.VISIBLE);
                btnGo.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        infoLayout.setVisibility(View.GONE);
                        showLogin();
                    }
                });
                btnClose.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        infoLayout.setVisibility(View.GONE);
                    }
                });

                ArrayList<LatLng> directionPoint = v2GetRouteDirection
                        .getDirection(document);
                PolylineOptions rectLine = new PolylineOptions().width(10)
                        .color(Color.BLUE);

                for (int i = 0; i < directionPoint.size(); i++) {
                    rectLine.add(directionPoint.get(i));
                }

                // Adding route on the map
                googleMap.addPolyline(rectLine);

            }

            Dialog.dismiss();

        }
    }

// OTHER STUFF

    private void removeFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (mapLayout.getVisibility() == View.GONE)
            getSupportFragmentManager().beginTransaction().remove(fragments.get(fragments.size() - 1)).commit();
    }

    private void showMapFragment() {
        if (mapLayout != null) {
            removeFragment();
            mapLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                showMapFragment();
                break;
            case 1:
                fragment = RoutFragment.newInstance((float) dis / 1000, (int) dur / 60, new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()), toPosition);
                break;
            case 2:
                fragment = CashFragment.newInstance();
                break;
            case 3:
                fragment = MyProfileFragment.newInstance();
                break;
            case 4:
                fragment = JournalFragment.newInstance();
                break;
            case 5:
                fragment = CompanionFragment.newInstance();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
            mapLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onNavigationDrawerRoleChanged() {
        CameraUpdate cu;
        switch (RoleHelper.getRole()) {
            case RoleHelper.ROLE_PASS:
                showMapFragment();
                if (googleMap != null) {
                    googleMap.clear();
                    cu = CameraUpdateFactory.newLatLng(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()));
                    googleMap.animateCamera(cu);
                }
                Toast.makeText(getApplicationContext(), getString(R.string.toast_pass), Toast.LENGTH_LONG).show();
                break;
            case RoleHelper.ROLE_DRIVER:
                showMapFragment();
                if (googleMap != null) {
                    googleMap.clear();
                    cu = CameraUpdateFactory.newLatLng(new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude()));
                    googleMap.animateCamera(cu);
                }
                Toast.makeText(getApplicationContext(), getString(R.string.toast_driver), Toast.LENGTH_LONG).show();
                break;
        }
    }

    @Override
    public void onNavigationDrawerSettingsSelected() {

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                // mTitle = getString(R.string.title_section1);
                break;
            case 2:
                // mTitle = getString(R.string.title_section2);
                break;
            case 3:
                // mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.pass_item:
                // newGame();

//			drawerListViewItems = getResources().getStringArray(
//					R.array.items_pass);
//			adapter.refreshItems(drawerListViewItems, imageId_p);
//			drawerListView.setAdapter(adapter);

                return true;
            case R.id.driver_item:

//			drawerListViewItems = getResources().getStringArray(
//					R.array.items_driver);
//			adapter.refreshItems(drawerListViewItems, imageId_d);
//			adapter.notifyDataSetChanged();
                // drawerListView.setAdapter(adapter);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    boolean firstTimeLocClientConnect = true;

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub

        location = mLocationClient.getLastLocation();
        if (location == null)
            location = new Location("network");
        fromPosition = new LatLng(location.getLatitude(),
                location.getLongitude());
        // mLocationClient.disconnect();

        if (firstTimeLocClientConnect) {
            LatLng ln = new LatLng(location.getLatitude(),
                    location.getLongitude());
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(ln));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            firstTimeLocClientConnect = false;
        }

    }

    @Override
    public void onDisconnected() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mLocationClient != null) {
            mLocationClient.connect();
        }
    }

    @Override
    public void onStop() {
        // If the client is connected
//        if (mLocationClient.isConnected()) {
//			/*
//			 * Remove location updates for a listener. The current Activity is
//			 * the listener, so the argument is "this".
//			 */
//            mLocationClient
//                    .removeLocationUpdates((com.google.android.gms.location.LocationListener) this);
//        }
        /*
         * After disconnect() is called, the client is considered "dead".
		 */
        mLocationClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

}
