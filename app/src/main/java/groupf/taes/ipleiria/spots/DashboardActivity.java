package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import modelo.Spot;
import modelo.SpotsManager;

import static java.lang.Thread.activeCount;
import static java.lang.Thread.sleep;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView freeSpotsTxt;
    private TextView occupiedSpotsTxt;
    private TextView lastInfoDateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  disconnectInternet();
      //  checkInternetConnection();
        SpotsManager.getINSTANCE().readSpotsDataFromDatabase();
        /*try {
            sleep(400);
        } catch (Exception ex) {

        }*/
        setContentView(R.layout.activity_dashboard);

        freeSpotsTxt = findViewById(R.id.txtNumberFreeSpots);
        occupiedSpotsTxt = findViewById(R.id.txtNumberOcuppiedSpots);
        lastInfoDateTxt = findViewById(R.id.lastInfoDate);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        for(Spot s : SpotsManager.getINSTANCE().getSpots()) {
            if (s.getStatus() == 0) {
                String location = s.getLocationGeo();
                String[] geo = location.split(",");
                LatLng marker = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                mMap.addMarker(new MarkerOptions().position(marker).title(s.getSpotId()));
            }
        }

        freeSpotsTxt.setText(String.valueOf(SpotsManager.getINSTANCE().getFreeSpots()));
        occupiedSpotsTxt.setText(String.valueOf(SpotsManager.getINSTANCE().getOcuppiedSpots()));

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");

        SharedPreferences sharedPref = getSharedPreferences("SpotsPref", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (!checkInternetConnection() && sharedPref.contains("dateLastInfo")) {
            String str = sharedPref.getString("dateLastInfo", null);
            SpotsManager.getINSTANCE().setDateOfData(str);
            lastInfoDateTxt.setText(str);
          //  String date = df.format(sharedPref.)
          //  lastInfoDateTxt.setText(date);
        } else {
          //  String date = df.format(SpotsManager.getINSTANCE().getDateOfData());
            lastInfoDateTxt.setText(SpotsManager.getINSTANCE().getDateOfData());
            editor.putString("dateLastInfo", SpotsManager.getINSTANCE().getDateOfData());
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
      //  wm.disconnect();
        wm.setWifiEnabled(false);
    }


}


