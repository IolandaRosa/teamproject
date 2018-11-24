package groupf.taes.ipleiria.spots;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import modelo.Spot;
import modelo.SpotsManager;
import modelo.User;
import modelo.UsersManager;

public class DashboardAuthActivity extends AppCompatActivity implements OnMapReadyCallback {


    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<CharSequence> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    private Spinner spinner;
    private SpinnerAdapter spinnerAdapter;

    private GoogleMap mMap;
    private TextView freeSpotsTxt;
    private TextView occupiedSpotsTxt;
    private TextView lastInfoDateTxt;
    private static List<Marker> markers;

    private User currentUser;

    private int currentPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // FirebaseAuth.getInstance().signOut();
        super.onCreate(savedInstanceState);
        currentPark = 0;
      // SpotsManager.getINSTANCE().writeSpotsOnDatabase();
        SpotsManager.getINSTANCE().readSpotsDataFromDatabase();

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(DashboardActivity.getIntent(this));
            return;
        }

        setContentView(R.layout.activity_dashboard_auth);
        markers = new LinkedList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        freeSpotsTxt = findViewById(R.id.txtNumberFreeSpots);
        occupiedSpotsTxt = findViewById(R.id.txtNumberOcuppiedSpots);
        lastInfoDateTxt = findViewById(R.id.lastInfoDate);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        UsersManager.INSTANCE.loadCurrentUser(UsersManager.INSTANCE.getUserProfileInfo());

        spinner = findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this,R.array.maps,android.R.layout.simple_spinner_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //posicao seleciondada//id para mapeamento de BD
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentPark = position;
                putMarkers();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty

            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        addDrawerItems();
        setupDrawer();

        //mapFragment.getMapAsync(this);
    }

    private void addDrawerItems() {
        mAdapter = ArrayAdapter.createFromResource(this, R.array.dashboardIems, android.R.layout.simple_list_item_1);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentUser = UsersManager.INSTANCE.getCurrentUser();
                switch (position)
                {
                    case 0:
                        showProfile();
                        break;
                    case 1:
                        findMeASpot();
                        break;
                    case 2:
                        startActivity(FavouriteSpotsListActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 5:
                        startActivity(ChangePasswordActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 6:
                        UsersManager.INSTANCE.logoutUser();
                        startActivity(DashboardActivity.getIntent(DashboardAuthActivity.this));
                        break;
                }

            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.hamburgerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);

        mDrawerLayout.addDrawerListener(mDrawerToggle);
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, DashboardAuthActivity.class);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        SpotsManager.getINSTANCE().getParkingSpotsD();

        putMarkers();

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");

        SharedPreferences sharedPref = getSharedPreferences("SpotsPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!checkInternetConnection() && sharedPref.contains("dateLastInfo")) {
            String str = sharedPref.getString("dateLastInfo", null);
            SpotsManager.getINSTANCE().setDateOfData(str);
            lastInfoDateTxt.setText(str);
            //  String date = df.format(sharedPref.)
            //  lastInfoDateTxt.setText(date);
        } else{
            //  String date = df.format(SpotsManager.getINSTANCE().getDateOfData());
            lastInfoDateTxt.setText(SpotsManager.getINSTANCE().getDateOfData());
            editor.putString("dateLastInfo", SpotsManager.getINSTANCE().getDateOfData());
            editor.commit();
        }

    }

    public  void putMarkers()
    {
        markers = new LinkedList<>();
        mMap.clear();
        List<Spot> spots = new LinkedList<>();
        int freeSpots = 0;
        int ocuppiedSpots = 0;
        if (currentPark == 0) {
            LatLng parkD = new LatLng(39.734994, -8.820697);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(parkD));
            spots = SpotsManager.getINSTANCE().getParkingSpotsA();
            freeSpots = SpotsManager.getINSTANCE().getFreeSpotsParkA();
            ocuppiedSpots = SpotsManager.getINSTANCE().getOcuppiedSpotsParkA();
        } else {
            LatLng parkD = new LatLng(39.733890, -8.821281);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(parkD));
            spots = SpotsManager.getINSTANCE().getParkingSpotsD();
            freeSpots = SpotsManager.getINSTANCE().getFreeSpotsParkD();
            ocuppiedSpots = SpotsManager.getINSTANCE().getOcuppiedSpotsParkD();
        }

        for(Spot s : spots) {
            if (s.getStatus() == 0) {
                String location = s.getLocationGeo();
                String[] geo = location.split(",");
                LatLng markerPosition = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition).title(s.getSpotId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                markers.add(marker);
            }
        }

        freeSpotsTxt.setText(String.valueOf(freeSpots));
        occupiedSpotsTxt.setText(String.valueOf(ocuppiedSpots));
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showProfile() {
        currentUser = UsersManager.INSTANCE.getCurrentUser();
        startActivity(ProfileActivity.getIntent(this).putExtra("user", currentUser));
    }

    public static List<Marker> getMarkers() {
        return markers;
    }

    public void findMeASpot() {
        if (currentUser.getFindPreference() == null) {
            startActivity(ChooseAPreferenceActivity.getIntent(this).putExtra("user", currentUser));
        }
    }


}
