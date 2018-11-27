package groupf.taes.ipleiria.spots;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
    private static final int PERMISSION_LOCATION_REQUEST = 0;

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
    private LatLng currentLocation = null;
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // FirebaseAuth.getInstance().signOut();
        super.onCreate(savedInstanceState);
        currentPark = 0;
      // SpotsManager.getINSTANCE().writeSpotsOnDatabase();
        SpotsManager.INSTANCE.readSpotsDataFromDatabase();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(DashboardActivity.getIntent(this));
            return;
        }

        setContentView(R.layout.activity_dashboard_auth);
        markers = new LinkedList<>();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        UsersManager.INSTANCE.loadCurrentUser(UsersManager.INSTANCE.getUserProfileInfo());

        freeSpotsTxt = findViewById(R.id.txtNumberFreeSpots);
        occupiedSpotsTxt = findViewById(R.id.txtNumberOcuppiedSpots);
        lastInfoDateTxt = findViewById(R.id.lastInfoDate);

        mDrawerList = (ListView) findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();



        spinner = findViewById(R.id.spinner);
        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.maps, android.R.layout.simple_spinner_item);
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

        // Para saber a localização do dispositivo
      //  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        /*mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub
                currentLocation = new LatLng(arg0.getLatitude(),arg0.getLongitude());
                int a = 2;
                //mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
            }
        });*/
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
                      //  checkPermission();
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
        SpotsManager.INSTANCE.getParkingSpotsD();

        putMarkers();

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");

        SharedPreferences sharedPref = getSharedPreferences("SpotsPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!checkInternetConnection() && sharedPref.contains("dateLastInfo")) {
            String str = sharedPref.getString("dateLastInfo", null);
            SpotsManager.INSTANCE.setDateOfData(str);
            lastInfoDateTxt.setText(str);
            //  String date = df.format(sharedPref.)
            //  lastInfoDateTxt.setText(date);
        } else {
            //  String date = df.format(SpotsManager.getINSTANCE().getDateOfData());
            lastInfoDateTxt.setText(SpotsManager.INSTANCE.getDateOfData());
            editor.putString("dateLastInfo", SpotsManager.INSTANCE.getDateOfData());
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
            spots = SpotsManager.INSTANCE.getParkingSpotsA();
            freeSpots = SpotsManager.INSTANCE.getFreeSpotsParkA();
            ocuppiedSpots = SpotsManager.INSTANCE.getOcuppiedSpotsParkA();
        } else {
            LatLng parkD = new LatLng(39.733890, -8.821281);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(parkD));
            spots = SpotsManager.INSTANCE.getParkingSpotsD();
            freeSpots = SpotsManager.INSTANCE.getFreeSpotsParkD();
            ocuppiedSpots = SpotsManager.INSTANCE.getOcuppiedSpotsParkD();
        }

        for (Spot s : spots) {
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


    //mudar aqui se ele ja tiver preferencias entao, nao mostra a atividade
    public void findMeASpot() {

        if (currentUser.getFindPreference() == null) {
            startActivity(ChooseAPreferenceActivity.getIntent(this).putExtra("user", currentUser));
        } else {
            //
            LatLng choosenSpot = null;
            //LatLng currentLocation = null;
            switch (currentUser.getFindPreference()) {
                case BEST_RATED:
                    break;

                case CLOSER_LOCATION:
                    // choosenSpot = closestSpot(currentLocation);
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", currentUser).putExtra("preference", 1));

                   /* String uri = "http://maps.google.com/maps?&daddr=" + choosenSpot.latitude + "," + choosenSpot.longitude;
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent); */
                    break;

                case FAVOURITE_SPOTS:

                    break;
            }

        }
    }



    public float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    private LatLng closestSpot(LatLng currentLocation) {
        LatLng location;
        Spot choosenSpot = null;
        LatLng spotCoordenates = null;
        float smallerDistance = Float.MAX_VALUE;
        float currentDistance = 0;
        for (Spot spot : SpotsManager.INSTANCE.getFreeParkingSpots()) {
            spotCoordenates = getCoordenatesFromString(spot.getLocationGeo());

            // NOVO CÓDIGO PARA SABER A LOCALIZAçAO
            // ISTO É ASSINCRINO R PORQUE NÃO TEM TEMPO!! ESTA A ESOIRA

            Task<Location> loc = this.mFusedLocationClient.getLastLocation();

            while (!loc.isComplete()) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            loc.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    //location = new LatLng(location.getLatitude(), location.getLongitude())/*arg0.getLatitude(),arg0.getLongitude())*/;
                    if (location != null) {
                        System.out.println("location: "+location.getLatitude()+location.getLongitude());
                    }
                }
            });

            // Código retirado de : https://developers.google.com/android/guides/tasks

            /*
            int numCores = Runtime.getRuntime().availableProcessors();
            ThreadPoolExecutor executor = new ThreadPoolExecutor(numCores * 2, numCores *2,
                    60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());

            // PUUUUUUMMMMMM !!! SSSSSSHHHHHHH !!!
            loc.addOnCompleteListener(executor, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    String ola  = " CHEGUE AQUI ";
                }
            });


            //loc.getResult().getLatitude();
            //loc.getResult().getLongitude();

            // END DO NOVO CÓDIGO


            // Estoira aqui -- currentLocation is null
            /*if (currentLocation == null){
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub
                        currentLocation = new LatLng(arg0.getLatitude(),arg0.getLongitude());
                        int a = 2;
                        //mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    }
                });
            }*/
            currentDistance = distance(loc.getResult().getLatitude(), loc.getResult().getLongitude(), spotCoordenates.latitude, spotCoordenates.longitude);
            if (currentDistance < smallerDistance) {
                smallerDistance = currentDistance;
                choosenSpot = spot;
            }

        }
        return getCoordenatesFromString(choosenSpot.getLocationGeo());

    }

    private LatLng getCoordenatesFromString(String s) {
        String[] coordenates = s.split(",");
        return new LatLng(Double.parseDouble(coordenates[0]), Double.parseDouble(coordenates[1]));
    }

    /*   PolylineOptions rectOptions = new PolylineOptions()
                            .add(new LatLng(39.735235, Float.parseFloat(a)))
                            .add(new LatLng(39.735187, -8.820460))
                            .add(new LatLng(39.735132, -8.820341)).width(2f).color(Color.RED);
                    mMap.addPolyline(rectOptions);*/

           /* if (canGetLocation() == false) {
                showSettingsAlert();
                //DO SOMETHING USEFUL HERE. ALL GPS PROVIDERS ARE CURRENTLY ENABLED
            } else {
            }*/

                 /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();


            InternetConnectionManager.INSTANCE.showErrorMessage(this,R.string.app_name);
            PolylineOptions rectOptions = new PolylineOptions()
                    .add(new LatLng(longitude, latitude))
                    .add(new LatLng(37.45, -122.0))  // North of the previous point, but at the same longitude
                    .add(new LatLng(37.45, -122.2))  // Same latitude, and 30km to the west
                    .add(new LatLng(37.35, -122.2))  // Same longitude, and 16km to the south
                    .add(new LatLng(37.35, -122.0)); // Closes the polyline.

                // Get back the mutable Polyline
            Polyline polyline = mMap.addPolyline(rectOptions);

        */

    public void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissão granted
                    return;
                } else {
                    // permissão denied
                    showErrorMessage(R.string.errorPermissionLocationDenied);
                }
                return;
            }
        }
    }


    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(message);

        //builder.setNeutralButton(R.string.OK, null);
        builder.setNeutralButton(R.string.OK, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.show();
    }


}