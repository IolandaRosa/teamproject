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
import android.view.View;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import modelo.Spot;
import modelo.SpotsManager;
import modelo.User;

public class ChooseAPreferenceActivity extends AppCompatActivity {
    private static final int PERMISSION_LOCATION_REQUEST = 0;
    private Button btnFavouriteSpot;
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng currentLocation = null;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apreference);

        currentUser = (User) this.getIntent().getSerializableExtra("user");
        btnFavouriteSpot = findViewById(R.id.btnOneOfFavourites);
        if (currentUser.getFavouriteSpots().size() == 0) {
            btnFavouriteSpot.setVisibility(View.INVISIBLE);
        } else {
            btnFavouriteSpot.setVisibility(View.VISIBLE);
        }
     //   checkPermission();
       // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ChooseAPreferenceActivity.class);
    }

    public void onClickBtnBestRatedSpot(View view) {

    }

    public void onClickBtnCloserToMe(View view) {
        startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", currentUser).putExtra("preference", 1));
    }


    public void onClickBtnOneOfFavouriteSpots(View view) {

    }


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
