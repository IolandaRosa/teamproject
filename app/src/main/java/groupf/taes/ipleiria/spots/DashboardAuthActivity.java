package groupf.taes.ipleiria.spots;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

import modelo.IncidentsReportsManager;
import modelo.InternetConnectionManager;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.User;
import modelo.UsersManager;

public class DashboardAuthActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    private static final int PERMISSION_LOCATION_REQUEST = 0;
    public static final int distanceLimit = 100;
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
    private static FusedLocationProviderClient mFusedLocationClient;
    private Marker choosenMarker = null;
    private static Marker userSpotMarker = null;
    private String occupiedParkId = "";
    private  boolean execute = true;
    private Location loc = null;
    private Boolean isManual = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        currentPark = 0;

        execute = getIntent().getBooleanExtra("EXECUTE_READ_SPOTS",true);
        if(execute)//para evitar que seja executado mais do que uma vez
        {
            SpotsManager.INSTANCE.readSpotsDataFromDatabase();
        }


        IncidentsReportsManager.INSTANCE.getIncidentsReporstFromDatabase();

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

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
        }

        onChangeSpotStatus();

        SpotsManager.INSTANCE.updateDailyOccupationRate();

        // Para saber a localização do dispositivo
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public void onChangeSpotStatus(){

            FirebaseDatabase firebase = FirebaseDatabase.getInstance();

            DatabaseReference reference = firebase.getReference();

            reference.child("ParkingSpots").addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    List<Spot> spotsWithStateChanged = getOcuppiedSpotsChanged(SpotsManager.INSTANCE.getParkingSpotsOld(), SpotsManager.INSTANCE.getParkingSpots());

                    final Location lastLocation = getLocationG();

                    String[] location;

                    for (Spot spot : spotsWithStateChanged) {
                        location = spot.getLocationGeo().split(",");

                        //tive que por as coordenadas assim pq ele nao esta a conseguir por a localizacao
                        /*if (FindMeASpotActivity.distance(Double.parseDouble(location[0]), Double.parseDouble(location[1]), 39.734810, -8.820888) < distanceLimit
                                && UsersManager.INSTANCE.getCurrentUser().getSpotParked() == null)*/
                        if (lastLocation != null && FindMeASpotActivity.distance(Double.parseDouble(location[0]), Double.parseDouble(location[1]), lastLocation.getLatitude(), lastLocation.getLongitude()) < distanceLimit
                                && UsersManager.INSTANCE.getCurrentUser().getSpotParked() == null)

                        {
                            setParkingInSpot(spot.getSpotId());
                            break;
                        }
                    }


                    spotsWithStateChanged = getFreeSpotsChanged(SpotsManager.INSTANCE.getParkingSpotsOld(),SpotsManager.INSTANCE.getParkingSpots());

                    for (Spot spot : spotsWithStateChanged) {

                        if (UsersManager.INSTANCE.getCurrentUser().getSpotParked() != null && UsersManager.INSTANCE.getCurrentUser().getSpotParked().equals(spot.getSpotId()))
                            {
                                askUserYesOrNo(R.string.askUserIsLeavingTheSpot, false);
                                break;
                            }
                    }

                    putMarkers();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

    }
    private List<Spot> getFreeSpotsChanged(List<Spot> spotsBeforeChange, List<Spot> spotsChanged) {

        List<Spot> spotsResult = new LinkedList<>();

        for (Spot spotBeforeChange: spotsBeforeChange) {
            for (Spot spotChanged: spotsChanged) {
                if(spotBeforeChange.getSpotId().equals(spotChanged.getSpotId()) && spotBeforeChange.getStatus() == 1 && spotChanged.getStatus() == 0)
                {
                    spotsResult.add(spotChanged);
                }
            }
        }
        return spotsResult;
    }

    private List<Spot> getOcuppiedSpotsChanged(List<Spot> spotsBeforeChange, List<Spot> spotsChanged) {

        List<Spot> spotsResult = new LinkedList<>();

        for (Spot spotBeforeChange: spotsBeforeChange) {
            for (Spot spotChanged: spotsChanged) {
                if(spotBeforeChange.getSpotId().equals(spotChanged.getSpotId()) && spotBeforeChange.getStatus() == 0 && spotChanged.getStatus() == 1)
                {
                    spotsResult.add(spotChanged);
                }
            }


        }
        return spotsResult;
    }

    private void setParkingInSpot(String idSpotChanged) {
        if(!isManual){
            askUserIfHeParkInSpot(R.string.msgAskUserIfHeParked, idSpotChanged);
        }
        isManual=false;
    }

    public Location getLocationG()
    {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            loc = location;
                        }
                    }
                });

        return loc;
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
    public void addDrawerItems() {
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
                        findMeASpot();
                        break;
                    case 2:
                        startActivity(FavouriteSpotsListActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 3:
                        mDrawerLayout.closeDrawers();
                        isManual=true;
                        setClickListenerForMarker();
                        break;
                    case 4:
                        mySpotClicked();
                        break;
                    case 5:
                        mDrawerLayout.closeDrawers();
                        leaveSpotClicked();
                        break;
                    case 6:
                        startActivity(StatisticsActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 7:
                        startActivity(ChangePasswordActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 8:
                        mDrawerLayout.closeDrawers();
                        startActivity(IncidentsReportsListActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 9:
                        mDrawerLayout.closeDrawers();
                        startActivity(ReportIncidentActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 10:
                        UsersManager.INSTANCE.logoutUser();
                        startActivity(DashboardActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 11:
                        startActivity(AlghorithmPerformanceActivity.getIntent(DashboardAuthActivity.this));
                        break;
                    case 12:
                        startActivity(DatePickActivity.getIntent(DashboardAuthActivity.this));
                }
            }
        });
    }

    public void leaveSpotClicked() {
        if (currentUser.getSpotParked() == null) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.errorUserIsNotParked);
        } else {
            askUserYesOrNo(R.string.askUserIsLeavingTheSpot, false);
        }
    }

    private void setClickListenerForMarker () {
        if (currentUser.getSpotParked() != null) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.errorUserAlreadyParked);
        } else if (SpotsManager.INSTANCE.getFreeParkingSpots(currentPark).size() == 0) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.noFreeSpots);
        } else {
            mMap.setOnMarkerClickListener(this);
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.infoForUserParkManually);
        }
    }

    public void setupDrawer() {
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

    private void mySpotClicked() {
        mDrawerLayout.closeDrawers();

        if (currentUser.getSpotParked() == null) {

            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.errorUserIsNotParked);
            return;
        }

        String parkedSpotId = currentUser.getSpotParked();
        Spot spotParked = null;
        List<Spot> spotsA = SpotsManager.INSTANCE.getParkingSpotsA();

        for(Spot s : spotsA) {
            if (s.getSpotId().equalsIgnoreCase(parkedSpotId)) {
                spotParked = s;
                break;
            }
        }

        if (spotParked == null) {
            List<Spot> spotsD = SpotsManager.INSTANCE.getParkingSpotsD();

            for(Spot s : spotsA) {
                if (s.getSpotId().equalsIgnoreCase(parkedSpotId)) {
                    spotParked = s;
                    break;
                }
            }
        }

        String location = spotParked.getLocationGeo();
        String[] geo = location.split(",");
        LatLng markerPosition = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
        Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition).title(spotParked.getSpotId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        userSpotMarker = marker;

        for(Marker m : markers) {
            m.setVisible(false);
        }
        //    markers.add(marker);

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
            if (s.getStatus() == 0 ) {
                String location = s.getLocationGeo();
                String[] geo = location.split(",");
                LatLng markerPosition = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition).title(s.getSpotId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                markers.add(marker);


                //pode estar mais otimizado
                //primeira vez
            }else if(s.getSpotId().equals(occupiedParkId))
            {
                String location = s.getLocationGeo();
                String[] geo = location.split(",");
                LatLng markerPosition = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition).title(s.getSpotId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                markers.add(marker);

                //sempre que o user sai da app para conseguir ver o seu spot de uma cor diferente
            }else if(UsersManager.INSTANCE.getCurrentUser() != null)
            {
                if( s.getSpotId().equals(UsersManager.INSTANCE.getCurrentUser().getSpotParked()))
                {
                    String location = s.getLocationGeo();
                    String[] geo = location.split(",");
                    LatLng markerPosition = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                    Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition).title(s.getSpotId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    markers.add(marker);
                }
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
        if (currentUser.getFindPreference() == null) {
            startActivity(ChooseAPreferenceActivity.getIntent(this).putExtra("user", currentUser));
        } else {
            LatLng choosenSpot = null;

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

        if(currentUser.getSpotParked() == null){
            askUserYesOrNo(R.string.msgAskUserIfWantToPark, true);
        }


        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false;
    }


                                        // parking or leaving
    private void askUserYesOrNo(int msg, final boolean parking ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(msg);
        builder.setPositiveButton(R.string.Yes, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               if (parking) {
                   try{
                       SpotsManager.INSTANCE.setSpotStatusToOccupied(choosenMarker.getTitle(),true);
                       UsersManager.INSTANCE.setSpotUserIsParked(choosenMarker.getTitle());
                   }
                   catch (Exception e){
                       Log.d("Ex",e.getMessage());
                   }

               } else {
                    leaveSpot();
               }
               currentUser = UsersManager.INSTANCE.getCurrentUser();
            }
        });

        builder.setNegativeButton(R.string.No, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (parking) {
                    choosenMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }else
                {
                    SpotsManager.INSTANCE.setSpotStatusToOccupied(UsersManager.INSTANCE.getCurrentUser().getSpotParked(),false);
                }
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    private  void askUserIfHeParkInSpot(int message, final String idSpotChanged){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.Yes, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                occupiedParkId = idSpotChanged;
                putMarkers();
                for (Marker m :markers) {
                    m.getId();
                    if (m.getTitle().compareTo(idSpotChanged) == 0){

                        m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        UsersManager.INSTANCE.setSpotUserIsParked(m.getTitle());
                        SpotsManager.INSTANCE.setTotalParkingTimesOnSpot(m.getTitle());
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



    public static Marker getUserSpotMarker() {
        return userSpotMarker;
    }

    public void leaveSpot() {
        currentUser = UsersManager.INSTANCE.getCurrentUser();
        final String spotId = currentUser.getSpotParked();
        SpotsManager.INSTANCE.setSpotStatusToFree(currentUser.getSpotParked());
        UsersManager.INSTANCE.userLeaveSpot();
        LayoutInflater inflater = (LayoutInflater) DashboardAuthActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layout = inflater.inflate(R.layout.leave_spot_dialog, null);
        float density = DashboardAuthActivity.this.getResources().getDisplayMetrics().density;

        final PopupWindow pw = new PopupWindow(layout, (int)density*380, (int)density*420, true);

        pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pw.setOutsideTouchable(true);

        pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

        List<Spot> spots = currentUser.getFavouriteSpots();
        for(Spot s : spots) {
            if(s.getSpotId().equalsIgnoreCase(spotId)) {
                layout.findViewById(R.id.btnAddToFavourites).setVisibility(View.GONE);
                layout.findViewById(R.id.txtViewOneOfFavourites).setVisibility(View.VISIBLE);
            }
        }

        ((Button) layout.findViewById(R.id.btnClose)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();
            }
        });

        ((Button) layout.findViewById(R.id.btnAddToFavourites)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addSpotToFavourites(spotId);
                layout.findViewById(R.id.btnAddToFavourites).setVisibility(View.GONE);
                layout.findViewById(R.id.txtViewOneOfFavourites).setVisibility(View.VISIBLE);
            }
        });

        ((Button) layout.findViewById(R.id.btnSendRate)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                RatingBar ratingBar = (RatingBar) layout.findViewById(R.id.ratingBar);

                rateSpot(spotId, (int) ratingBar.getRating());

                layout.findViewById(R.id.btnSendRate).setVisibility(View.GONE);
                layout.findViewById(R.id.txtViewWantRateSpot).setVisibility(View.GONE);
                layout.findViewById(R.id.ratingBar).setVisibility(View.INVISIBLE);
                layout.findViewById(R.id.txtViewRateSended).setVisibility(View.VISIBLE);
            }
        });
    }

    private void addSpotToFavourites(String spotId) {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId(spotId);
        UsersManager.INSTANCE.addSpotToFavourites(currentUser, spot);
        currentUser = UsersManager.INSTANCE.getCurrentUser();
    }

    private void rateSpot(String spotId, int rate) {
        Spot spot = SpotsManager.INSTANCE.getSpotFromId(spotId);
        int spotRate = spot.getRating();

        int finalRate = ((spotRate + rate) / 2);

        if (finalRate > 5) {
            finalRate = 5;
        } else if (finalRate < 0) {
            finalRate = 0;
        }

        SpotsManager.INSTANCE.setSpotRate(spotId, finalRate);
    }
}