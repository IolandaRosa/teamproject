package groupf.taes.ipleiria.spots;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private TextView freeSpotsTxt;
    private TextView occupiedSpotsTxt;
    private TextView lastInfoDateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpotsManager.getINSTANCE();
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
        int countFreeSpots = 0;


        for(Spot s : SpotsManager.getINSTANCE().getSpots()) {
            if (s.getStatus() == 0) {
                String location = s.getLocationGeo();
                String[] geo = location.split(",");
                LatLng marker = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
                mMap.addMarker(new MarkerOptions().position(marker).title(s.getSpotId()));
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                countFreeSpots++;
            }
        }

        freeSpotsTxt.setText(String.valueOf(countFreeSpots));
        int occupiedSpots = SpotsManager.getINSTANCE().getSpots().size() - countFreeSpots;
        occupiedSpotsTxt.setText(String.valueOf(occupiedSpots));

        DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        lastInfoDateTxt.setText(date);




    }



}
