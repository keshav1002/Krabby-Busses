package mindwave.com.krabbybusses;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class busRoute3 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    SupportMapFragment supportMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        supportMapFragment = SupportMapFragment.newInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_route3);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bus_route3, menu);
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
        } else if (id == R.id.nav_aboutUs){
            Intent intent = new Intent(this, aboutUs.class);
            startActivity(intent);
        } else if (id == R.id.showMore){
            Toast.makeText(this, "More bus routes will be released in the next update. Stay tuned", Toast.LENGTH_LONG).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng startingLocation = new LatLng(6.933601222698944, 79.85540101795198);
        LatLng destination = new LatLng(6.413246242205084, 81.33221626281738);
        LatLng wayPoint1 = new LatLng(6.954607319973443, 80.20508766174316);
        LatLng wayPoint2 = new LatLng(6.716735580977222, 80.38434505462646);
        LatLng wayPoint3 = new LatLng(6.3347061616951335, 80.85323810577393);

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startingLocation, 7));


        googleMap.addMarker(new MarkerOptions().position(startingLocation).title("Starting Location (Bastian Mawatha)"));
        googleMap.addMarker(new MarkerOptions().position(destination).title("Ending Location (Kataragama)"));
        googleMap.addCircle(new CircleOptions().center(wayPoint1));
        googleMap.addCircle(new CircleOptions().center(wayPoint2));

        PolylineOptions polylineOptions = new PolylineOptions();

        polylineOptions.add(startingLocation, wayPoint1, wayPoint2, wayPoint3, destination);
        googleMap.addPolyline(polylineOptions);
    }
}
