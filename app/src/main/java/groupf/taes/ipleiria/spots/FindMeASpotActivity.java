package groupf.taes.ipleiria.spots;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.security.Permission;

import modelo.Spot;
import modelo.SpotsManager;
import modelo.User;

import static android.os.SystemClock.sleep;

public class FindMeASpotActivity extends AppCompatActivity {
    private static final int PERMISSION_LOCATION_REQUEST = 0;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng currentLocation = null;
    private int optionForSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me_aspot);


       /* if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
        } */
        optionForSpot = this.getIntent().getIntExtra("preference", -1);
      //  mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        checkPermission();
    }


    private void initializeMapsApp(LatLng choosenSpot) {
        String uri = "http://maps.google.com/maps?&daddr=" + choosenSpot.latitude + "," + choosenSpot.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private LatLng closestSpot() {
        LatLng location;
        Spot choosenSpot = null;
        LatLng spotCoordenates = null;
        float smallerDistance = Float.MAX_VALUE;
        float currentDistance = 0;
        for (Spot spot : SpotsManager.INSTANCE.getFreeParkingSpots()) {
            spotCoordenates = getCoordenatesFromString(spot.getLocationGeo());

            // NOVO CÓDIGO PARA SABER A LOCALIZAçAO
            // ISTO É ASSINCRINO R PORQUE NÃO TEM TEMPO!! ESTA A ESOIRA

            // verificação feita antes


//            Task<Location> loc = this.mFusedLocationClient.getLastLocation();
//
//            while (!loc.isComplete()) {
//                try {
//                    Thread.sleep(1);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            loc.addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                @Override
//                public void onSuccess(Location location) {
//                    // Got last known location. In some rare situations this can be null.
//                    //location = new LatLng(location.getLatitude(), location.getLongitude())/*arg0.getLatitude(),arg0.getLongitude())*/;
//                    if (location != null) {
//                        System.out.println("location: "+location.getLatitude()+location.getLongitude());
//                    }
//                }
//            });

            Task<Location> loc = DashboardAuthActivity.getLocation();

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


    public static Intent getIntent(Context context) {
        return new Intent(context, FindMeASpotActivity.class);
    }

    public void showSpot() {
      //  User user = (User) this.getIntent().getSerializableExtra("user"); // por causa dos favourites
        LatLng choosenSpot = null;

        switch (optionForSpot) {
            case 0:
                // best rated
                break;
            case 1:
                choosenSpot = closestSpot();
                initializeMapsApp(choosenSpot);
                break;
            case 2:
                // one of favourites
                break;
        }
    }

    public void checkPermission() {
        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_LOCATION_REQUEST);
        } else {
            showSpot();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_LOCATION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissão granted
                    //mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                     showSpot();
                     return;
                } else {
                    // permissão denied
                    showErrorMessage(R.string.errorPermissionLocationDenied);
                }
               // return;
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
