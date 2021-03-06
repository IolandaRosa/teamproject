package groupf.taes.ipleiria.spots;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.LinkedList;
import java.util.List;

import modelo.Spot;
import modelo.SpotsManager;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView freeSpotsTxt;
    private TextView occupiedSpotsTxt;
    private TextView lastInfoDateTxt;
    private static List<Marker> markers;
    public static Activity dashActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dashActivity = this;
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(DashboardAuthActivity.getIntent(this));
            finish();
            return;
        }

        SpotsManager.INSTANCE.readSpotsDataFromDatabase();
        setContentView(R.layout.activity_dashboard);
        freeSpotsTxt = findViewById(R.id.txtNumberFreeSpots);
        occupiedSpotsTxt = findViewById(R.id.txtNumberOcuppiedSpots);
        lastInfoDateTxt = findViewById(R.id.lastInfoDate);
        markers = new LinkedList<>();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

       for(Spot s : SpotsManager.INSTANCE.getParkingSpotsA()) {
            if (s.getStatus() == 0) {
                String location = s.getLocationGeo();
                String[] geo = location.split(",");
                LatLng markerPosition = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                Marker marker = mMap.addMarker(new MarkerOptions().position(markerPosition).title(s.getSpotId()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                markers.add(marker);
            }
        }

        freeSpotsTxt.setText(String.valueOf(SpotsManager.INSTANCE.getFreeSpotsParkA()));
        occupiedSpotsTxt.setText(String.valueOf(SpotsManager.INSTANCE.getOcuppiedSpotsParkA()));


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

    public boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(message);

        builder.setNeutralButton(R.string.OK, null);

        builder.show();
    }

    public void disconnectInternet() {
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        wm.setWifiEnabled(false);
    }

    public void onClick_btnLogin(View view) {
        startActivity(LoginActivity.getIntent(this));
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    public static List<Marker> getMarkers() {
        return markers;
    }
}


