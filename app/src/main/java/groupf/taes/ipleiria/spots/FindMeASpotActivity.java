package groupf.taes.ipleiria.spots;

import android.Manifest;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

import modelo.InternetConnectionManager;
import modelo.Spot;
import modelo.SpotsManager;
import modelo.User;

public class FindMeASpotActivity extends AppCompatActivity {
    private static final int PERMISSION_LOCATION_REQUEST = 0;
    private int optionForSpot;
    private int currentPark;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_me_aspot);

        optionForSpot = this.getIntent().getIntExtra("preference", -1);
        currentUser = (User) this.getIntent().getSerializableExtra("user"); // por causa dos favourites
        currentPark=this.getIntent().getIntExtra("park",0);
        checkPermission();
    }


    private void initializeMapsApp(LatLng choosenSpot) {
        String uri = "http://maps.google.com/maps?&daddr=" + choosenSpot.latitude + "," + choosenSpot.longitude;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }

    private LatLng closestSpot() {
        Spot choosenSpot = null;
        LatLng spotCoordenates;
        float smallerDistance = Float.MAX_VALUE;
        float currentDistance = 0;
        for (Spot spot : SpotsManager.INSTANCE.getFreeParkingSpots()) {
            spotCoordenates = getCoordenatesFromString(spot.getLocationGeo());

            //vai buscar o private static FusedLocationProviderClient mFusedLocationClient;
            Task<Location> loc = DashboardAuthActivity.getLocation();

            loc.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
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

    private static LatLng getCoordenatesFromString(String s) {
        String[] coordenates = s.split(",");
        return new LatLng(Double.parseDouble(coordenates[0]), Double.parseDouble(coordenates[1]));
    }

    public static float distance(double lat_a, double lng_a, double lat_b, double lng_b) {
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
        Spot best=null;

        switch (optionForSpot) {
            case 0:
                List<Spot> spots = null;
                best = bestRatedPerPark(spots, currentPark);
                System.out.println(best.getRating() + best.getSpotId()+best.getPark());
                initializeMapsApp (getCoordenatesFromString(best.getLocationGeo()));

                break;
            case 1:
                choosenSpot = closestSpot();
                initializeMapsApp(choosenSpot);
                break;
            case 2:
                List<Spot> favouriteSpots = currentUser.getFavouriteSpots();
                if(favouriteSpots.isEmpty()){
                    InternetConnectionManager.INSTANCE.showErrorMessage(FindMeASpotActivity.this,R.string.emptySpotsList);
                    return;
                }
                best = getBestRatedSpot(favouriteSpots);
                System.out.println(best.getRating() + best.getSpotId());
                //LatLng bestRatedCoordinates = getCoordenatesFromSting(bestRatedSpot.getLocationGeo());
                initializeMapsApp (getCoordenatesFromString(best.getLocationGeo()));
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
                     showSpot();
                     return;
                } else {
                    // permissão denied
                    showErrorMessage(R.string.errorPermissionLocationDenied);
                }
            }
        }
    }


    private void showErrorMessage(int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(message);

        builder.setNeutralButton(R.string.OK, new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });

        builder.show();
    }

    public static Spot getBestRatedSpot(List<Spot> spots) {
        Spot best = spots.get(0);
        for (Spot s : spots) {
            if (s.getStatus()==0 && s.getRating() >= best.getRating()) {
                best = s;
            }
        }

        return best;
    }

    public static Spot getClosestSpot(List<Spot> spots) {
        Spot choosenSpot = null;
        LatLng spotCoordenates;
        float smallerDistance = Float.MAX_VALUE;
        float currentDistance = 0;
        for (Spot spot : spots) {


            spotCoordenates = getCoordenatesFromString(spot.getLocationGeo());

            //vai buscar o private static FusedLocationProviderClient mFusedLocationClient;
            Task<Location> loc = DashboardAuthActivity.getLocation();

            loc.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
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
        return choosenSpot;

    }


    public Spot bestRatedPerPark(List<Spot> spots, int currentPark) {
        // Saber qual o parque a pesquisar currentPark;
        if (currentPark == 0) {
            // Ligação à BD para saber quais são com mais RATED
            spots = SpotsManager.INSTANCE.getParkingSpotsA();
        } else {
            // Ligação à BD para saber quais são com mais RATED
            spots = SpotsManager.INSTANCE.getParkingSpotsD();
        }

        return getBestRatedSpot(spots);
    }


}
