package groupf.taes.ipleiria.spots;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import modelo.Spot;
import modelo.SpotsManager;

public class DashboardActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpotsManager.getINSTANCE();
        setContentView(R.layout.activity_dashboard);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        for(Spot s : SpotsManager.getINSTANCE().getSpots()) {
            String location = s.getLocationGeo();
            String[] geo = location.split(",");
           /* AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage(geo[0] + " - " + geo[1]); */
            //Toast.makeText(this, geo[0] + " - " + geo[1], Toast.LENGTH_LONG);
            LatLng marker = new LatLng(Float.parseFloat(geo[0]), Float.parseFloat(geo[1]));
            mMap.addMarker(new MarkerOptions().position(marker).title(s.getSpotId()));
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }


    }



}
