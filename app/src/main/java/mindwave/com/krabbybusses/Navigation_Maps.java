package mindwave.com.krabbybusses;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Navigation_Maps extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {
    SupportMapFragment supportMapFragment;

    private GoogleMap mMap;
    JSONParser jsonParser = new JSONParser();

    private static final String url_get_busRoute = "http://kingkong1002.netne.net/krabby_busses/get_busRoute.php";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_ROUTE = "route";
    private static final String TAG_BUSNO = "busNo";
    private static final String TAG_STARTLAT = "startLat";
    private static final String TAG_STARTLONG = "startLong";
    private static final String TAG_ENDLAT = "endLat";
    private static final String TAG_ENDLONG = "endLong";
    private static final String TAG_POINTS = "points";
    public String userSearchLocation;
    Random rnd = new Random();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportMapFragment = SupportMapFragment.newInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation__maps);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toast.makeText(this, "Search for your current location or use the find my location button to set your current location", Toast.LENGTH_LONG).show();

        final Context context = this;

        /*Code for setting users destination Location*/
        SearchView searchViewDestination = (SearchView) findViewById(R.id.destinationSearch);
        if (searchViewDestination != null) {
            searchViewDestination.setOnQueryTextListener(new OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    userSearchLocation = query;
                    try {
                        searchDestination(userSearchLocation);
                        // starting background task to update product
                        askingIfToClearMaps();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                    StrictMode.setThreadPolicy(policy);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }


        /*Code for setting users current Location*/

        SearchView searchCurrentLocation = (SearchView) findViewById(R.id.currentLocation);
        if (searchCurrentLocation != null) {
            searchCurrentLocation.setOnQueryTextListener(new OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    searchCurrentLocation(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    return false;
                }
            });
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation__maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.support.v4.app.FragmentManager sFragmentManager = getSupportFragmentManager();
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_busroute) {
            Intent intent = new Intent(this, Navigation_Maps.class);
            startActivity(intent);
        } else if (id == R.id.nav_tutorial) {

        } else if (id == R.id.nav_option) {

        } else if (id == R.id.bus154Route) {
            Intent intent = new Intent(this, busRoute154.class);
            startActivity(intent);
        } else if (id == R.id.bus01Route) {
            Intent intent = new Intent(this, busRoute1.class);
            startActivity(intent);
        } else if (id == R.id.bus02Route) {
            Intent intent = new Intent(this, busRoute2.class);
            startActivity(intent);
        } else if (id == R.id.bus03Route) {
            Intent intent = new Intent(this, busRoute3.class);
            startActivity(intent);
        } else if (id == R.id.bus100Route) {
            Intent intent = new Intent(this, busRoute100.class);
            startActivity(intent);
        } else if (id == R.id.bus101Route) {
            Intent intent = new Intent(this, busRoute101.class);
            startActivity(intent);
        } else if (id == R.id.nav_aboutUs) {
            Intent intent = new Intent(this, aboutUs.class);
            startActivity(intent);
        } else if (id == R.id.showMore) {
            Toast.makeText(this, "More bus routes will be released in the next update. Stay tuned", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        /*Checking if user's location is enabled*/

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS is not enabled on your phone");  // GPS not found
            builder.setMessage("Please make sure GPS is enabled and try again!"); // Want to enable?
            builder.setPositiveButton("Enable GPS", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    new Navigation_Maps();

                }
            });
            builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new MainActivity();
                }
            });
            builder.create().show();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
//        mMap.getUiSettings().setMyLocationButtonEnabled(false);
//
//        String gpsLocationProvider = LocationManager.GPS_PROVIDER;
//        String courierLocationProvider = LocationManager.NETWORK_PROVIDER;
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//
//
//        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
//            Context context = this;
//            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
//            alertDialog.setMessage("Please check your network and try again!");
//            alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    new Navigation_Maps();
//                }
//            });
//            alertDialog.setCancelable(true);
//            alertDialog.setIcon(R.drawable.ic_error);
//            alertDialog.show();
//        } else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
//            String toastMessage = "Location may not be accurate, Please connect your data to get the accurate location!";
//            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();
//
//            Location lastLocation = locationManager.getLastKnownLocation(gpsLocationProvider);
//
//            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 0);
//            mMap.setMyLocationEnabled(true);
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        } else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
//            Location lastLocation = locationManager.getLastKnownLocation(courierLocationProvider);
//
//            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
//            CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 0);
//            mMap.setMyLocationEnabled(true);
//            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        }
    }


    /*Method to search user's destination*/
    public void searchDestination(String searchLocation) throws IOException {
        final Context context = this;
        if (searchLocation == null || searchLocation == "") {
            Toast.makeText(this, "Enter a valid destination", Toast.LENGTH_LONG).show();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            final GoogleMap mMap = mapFragment.getMap();


            List<android.location.Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(searchLocation, 1);
            } catch (IOException e) {

            }

            android.location.Address address = addressList.get(0);

            final LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Destination").icon(BitmapDescriptorFactory.fromResource(R.drawable.destination)));
                            new getBusRoute().execute();
                            InputMethodManager inputManager = (InputMethodManager)
                                    getSystemService(Context.INPUT_METHOD_SERVICE);

                            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                                    InputMethodManager.HIDE_NOT_ALWAYS);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    mMap.addMarker(new MarkerOptions().position(latLng).title("Your Destination"));

                                    List<android.location.Address> addressList = null;
                                    Geocoder geocoder = new Geocoder(context);

                                    double latitute = latLng.latitude;
                                    double longitude = latLng.longitude;

                                    try {

                                        addressList = geocoder.getFromLocation(latitute, longitude, 1);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }


                            });


                    }
                }
            };


            builder.setMessage("Is this your destination?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            try {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (NullPointerException e) {

            }

        }

    }

    public void askingIfToClearMaps() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        mMap.clear();
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        alertDialogBuilder.setTitle("Clear Maps").setMessage("Do you want to clear the bus routes in the maps?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    /*Method to search user's currentLocation*/
    public void searchCurrentLocation(String searchLocation) {
        final Context context = this;
        if (searchLocation == null || searchLocation == "") {
            Toast.makeText(this, "Enter a valid destination", Toast.LENGTH_LONG).show();
        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            final GoogleMap mMap = mapFragment.getMap();


            List<android.location.Address> addressList = null;
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(searchLocation, 1);
            } catch (IOException e) {

            }

            android.location.Address address = addressList.get(0);

            final LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 0);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            mMap.addMarker(new MarkerOptions().position(latLng).title("Your Current Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.people_vector)));
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            Toast.makeText(context, "Try again or Use the location button to get your current location", Toast.LENGTH_LONG).show();
                    }
                }
            };


            builder.setMessage("Is this your current location?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

            try {
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (NullPointerException e) {

            }
        }
    }

    public void addBusStop(View view) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(6.865073, 79.863166))
                .title("Nearest Bus Stop")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.bus_stand_icon)));
    }

    class getBusRoute extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Context context = getApplicationContext();
            CharSequence text = "Searching the database for destination";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }


        @Override
        protected String doInBackground(String... params) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String destination = userSearchLocation.toString().toLowerCase();
                    int success;
                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("dest", destination));

                        JSONObject json = jsonParser.makeHttpRequest(url_get_busRoute, "GET", params);

                        Log.d("Bus Route Details", json.toString());

                        success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {

                            Context context = getApplicationContext();
                            CharSequence text = "Destination Found!!";
                            int duration = Toast.LENGTH_LONG;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();

                            JSONArray routeObj = json.getJSONArray(TAG_ROUTE);

                            for (int i = 0; i < routeObj.length(); i++) {

                                JSONObject route = routeObj.getJSONObject(i);
                                String busNo = route.getString(TAG_BUSNO);
                                double startLat = Double.parseDouble(route.getString(TAG_STARTLAT));
                                double startLong = Double.parseDouble(route.getString(TAG_STARTLONG));
                                double endLat = Double.parseDouble(route.getString(TAG_ENDLAT));
                                double endLong = Double.parseDouble(route.getString(TAG_ENDLONG));
                                String points = route.getString(TAG_POINTS);

                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(startLat, startLong))
                                        .title(String.valueOf(busNo))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                                mMap.addMarker(new MarkerOptions()
                                        .position(new LatLng(endLat, endLong))
                                        .title(String.valueOf(busNo)));

                                findDirections(startLat, startLong, endLat, endLong, points, "Driving");

                            }
                        } else {
                            Context context = getApplicationContext();
                            CharSequence text = "Destination Not Found, Please check your spelling(s) and try again";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(context, text, duration);
                            toast.show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            return null;
        }

        protected void onPostExecute(String file_url) {
            // dismiss the dialog once got all details
            //pDialog.dismiss();
        }
    }

    public void handleGetDirectionsResult(ArrayList<LatLng> directionPoints) {

        Polyline newPolyline;
        PolylineOptions rectLine = new PolylineOptions().width(10).color(Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));

        for (int i = 0; i < directionPoints.size(); i++) {
            rectLine.add(directionPoints.get(i));
        }

        newPolyline = mMap.addPolyline(rectLine);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(directionPoints.get(0).latitude, directionPoints.get(0).longitude)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

    }


    public void findDirections(double fromPositionDoubleLat, double fromPositionDoubleLong, double toPositionDoubleLat, double toPositionDoubleLong, String waypts, String mode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LAT, String.valueOf(fromPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.USER_CURRENT_LONG, String.valueOf(fromPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.DESTINATION_LAT, String.valueOf(toPositionDoubleLat));
        map.put(GetDirectionsAsyncTask.DESTINATION_LONG, String.valueOf(toPositionDoubleLong));
        map.put(GetDirectionsAsyncTask.WAYPOINTS, waypts);
        map.put(GetDirectionsAsyncTask.DIRECTIONS_MODE, mode);

        GetDirectionsAsyncTask asyncTask = new GetDirectionsAsyncTask(this);
        asyncTask.execute(map);

    }


    /*Method to get user's current location when they press on get current location using gps*/
    public void getCurrentLocation(View v) {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        String gpsLocationProvider = LocationManager.GPS_PROVIDER;
        String courierLocationProvider = LocationManager.NETWORK_PROVIDER;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED && connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            Context context = this;
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            alertDialog.setMessage("Please check your network and try again!");
            alertDialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    new Navigation_Maps();
                }
            });
            alertDialog.setCancelable(true);
            alertDialog.setIcon(R.drawable.ic_error);
            alertDialog.show();
        } else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            String toastMessage = "Location may not be accurate, Please connect your data to get the accurate location!";
            Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            try {
                Location lastLocation = locationManager.getLastKnownLocation(gpsLocationProvider);
                LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 0);
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (NullPointerException e) {
                String toastMessage1 = "Location services currently unavailable";
                Toast.makeText(this, toastMessage1, Toast.LENGTH_LONG).show();
            }

        } else if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) {
            try {
                Location lastLocation = locationManager.getLastKnownLocation(courierLocationProvider);

                LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                CameraPosition cameraPosition = new CameraPosition(latLng, 16, 0, 0);
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            } catch (NullPointerException e) {
                String toastMessage1 = "Location services currently unavailable";
                Toast.makeText(this, toastMessage1, Toast.LENGTH_LONG).show();
            }
        }
    }


}