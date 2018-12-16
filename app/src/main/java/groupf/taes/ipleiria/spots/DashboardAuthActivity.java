package groupf.taes.ipleiria.spots;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

import modelo.InternetConnectionManager;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.User;
import modelo.UsersManager;

public class DashboardAuthActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
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

    private  static int currentPark;
    private LatLng currentLocation = null;
    private static FusedLocationProviderClient mFusedLocationClient;

    private Marker choosenMarker = null;


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
        onChangeSpotStatus();

        //mapFragment.getMapAsync(this);

        // Para saber a localização do dispositivo
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void onChangeSpotStatus(){
        FirebaseDatabase firebase = FirebaseDatabase.getInstance();

        DatabaseReference reference = firebase.getReference();
        reference.child("ParkingSpots").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                // Devolve todos os atributos daquele estacionamento
                Iterable<DataSnapshot> spot = dataSnapshot.getChildren();
                putMarkers();


                String id = dataSnapshot.getKey();

                // Saber o estado do parque:
                Object infoPark = dataSnapshot.getValue();
                System.out.println(infoPark);

                System.out.println(dataSnapshot.child("Status").getValue().toString());


                // Devolve chave-valor com a location do que foi mudado
                // Usar loc.getValue() para devolver valor "-39.xxxxx, 8.xxxxx"
                DataSnapshot loc = spot.iterator().next();

                // Apenas para saber as coordenadas de GPS do spot que mudou de estado
                String[] location = loc.getValue().toString().split(",");

                for (DataSnapshot child : spot) {
                    Toast.makeText(DashboardAuthActivity.this, "Mudou o spot: " + child.getValue(), Toast.LENGTH_LONG).show();
                }



                // Saber a minha localização física
                Task<Location> lastLocation = getLocation();

                // Calculo da distancia

                float distance = FindMeASpotActivity.distance(Double.parseDouble(location[0]), Double.parseDouble(location[1]), lastLocation.getResult().getLatitude(), lastLocation.getResult().getLongitude());

                // Verificar se a a distancia do ponto infeiror a 5
                if (distance < 20){
                    setParkingInSpot(id);
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*
        reference.child("ParkingSpots")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        listSpotsNew = dataSnapshot.getChildren();
                        for (DataSnapshot child : listSpotsNew) {

                            Toast.makeText(DashboardAuthActivity.this, "Mudou o spot: " + child.getValue(), Toast.LENGTH_LONG).show();
                        }
                        putMarkers();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
*/
    }

    private void setParkingInSpot(String idSpotChanged) {
        askUserIfHeParkInSpot("Do you park in this spot?", idSpotChanged);
    }

    public static Task<Location> getLocation() {
        Task<Location> loc = mFusedLocationClient.getLastLocation();

        while (!loc.isComplete()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return loc;
    }

    //Menu Hamburguer
    private void addDrawerItems() {
        mAdapter = ArrayAdapter.createFromResource(this, R.array.dashboardIems, android.R.layout.simple_list_item_1);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentUser = UsersManager.INSTANCE.getCurrentUser();
                switch (position) {
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
                    case 3:
                  //      setClickListenerForMarker();

                        mDrawerLayout.closeDrawers();
                        setClickListenerForMarker();

                        //   mMap.setOnMarkerClickListener(this);
                        break;
                    case 6:
                        startActivity(ChangePasswordActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 7:
                        UsersManager.INSTANCE.logoutUser();
                        startActivity(DashboardActivity.getIntent(DashboardAuthActivity.this));
                        break;
                }
            }
        });
    }

    private void setClickListenerForMarker () {
        if (SpotsManager.INSTANCE.getFreeParkingSpots(currentPark).size() == 0) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.noFreeSpots);
        } else {
            mMap.setOnMarkerClickListener(this);
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.infoForUserParkManually);
        }
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

        SharedPreferences sharedPref = getSharedPreferences("SpotsPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!checkInternetConnection() && sharedPref.contains("dateLastInfo")) {
            String str = sharedPref.getString("dateLastInfo", null);
            SpotsManager.INSTANCE.setDateOfData(str);
            lastInfoDateTxt.setText(str);

        } else {

            lastInfoDateTxt.setText(SpotsManager.INSTANCE.getDateOfData());
            editor.putString("dateLastInfo", SpotsManager.INSTANCE.getDateOfData());
            editor.commit();
        }

    }

    public void putMarkers() {
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
        startActivity(ProfileActivity.getIntent(this).putExtra("user", currentUser).putExtra("currentPark", currentPark));
    }

    public static List<Marker> getMarkers() {
        return markers;
    }


    //mudar aqui se ele ja tiver preferencias entao, nao mostra a atividade
    public void findMeASpot() {

       // UsersManager.INSTANCE.loadCurrentUser(UsersManager.INSTANCE.getUserProfileInfo());

        if (currentUser.getFindPreference() == null) {
            startActivity(ChooseAPreferenceActivity.getIntent(this).putExtra("user", currentUser));
        } else {
            LatLng choosenSpot = null;
            //LatLng currentLocation = null;
            switch (currentUser.getFindPreference()) {
                case BEST_RATED:
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", currentUser).putExtra("preference", 0).putExtra("park",currentPark));
                    break;

                case CLOSER_LOCATION:
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", currentUser).putExtra("preference", 1).putExtra("park",currentPark));
                    break;

                case FAVOURITE_SPOTS:
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", currentUser).putExtra("preference", 2).putExtra("park",currentPark));
                    break;
            }
        }
    }

    public static int getCurrentPark() {
        return currentPark;
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.

        marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        this.choosenMarker = marker;

        for (Marker m : markers) {
            if (!m.getTitle().equalsIgnoreCase(marker.getTitle())) {
                // coloca os outros a verde caso o user clique em marcadores spots de seguida
                m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
        }

        askUserIfWantToParkInSpot(R.string.msgAskUserIfWantToPark);

        //  initializeMapsApp(marker.getPosition());

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }

    private void initializeMapsApp(LatLng choosenSpot) {
        String uri = "http://maps.google.com/maps?&daddr=" + choosenSpot.latitude + "," + choosenSpot.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }


    private void askUserIfWantToParkInSpot(int msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setPositiveButton(R.string.Yes, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SpotsManager.INSTANCE.setSpotStatusToOccupied(choosenMarker.getTitle());
                UsersManager.INSTANCE.setSpotUserIsParked(choosenMarker.getTitle());
                initializeMapsApp(choosenMarker.getPosition());
            }
        });

        builder.setNegativeButton(R.string.No, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                choosenMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    private void askUserIfHeParkInSpot(String message, final String idSpotChanged){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.Yes, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                for (Marker m :markers) {
                    m.getId();
                    if (m.getTitle().compareTo(idSpotChanged) == 0){
                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                    }
                }
            }
        });

        builder.setNegativeButton(R.string.No, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

}