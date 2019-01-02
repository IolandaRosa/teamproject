package groupf.taes.ipleiria.spots;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import modelo.IncidentsReportsManager;
import modelo.InternetConnectionManager;
import modelo.Spot;
import modelo.SpotsManager;

public class ReportIncidentActivity extends PerformanceButtonActivity {
    private static final int PERMISSION_STORAGE_REQUEST = 0;
    private Button btnGetLocation;
    private Button btnChooseSpotPark;
    private Button btnUpload;
    private TextView txtViewInfoLocationObtained;
    private TextView txtChoosePark;
    private TextView txtChooseSpot;
    private TextView txtInfoUploadedSuccess;
    private EditText txtDescription;
    private Spinner spinnerPark;
    private Spinner spinnerSpot;

    private Location currentLocation;
    private SpinnerAdapter spinnerAdapter;
    private String spotSelected = null;
    private int spotSelectedIndex = 0;
    private List<String> spotsIds;

    private Bitmap bitmap = null;

    private int hasPhoto = 0;

    @Override
    protected View childView() {
        return getLayoutInflater().inflate(R.layout.activity_report_incident, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        txtViewInfoLocationObtained = findViewById(R.id.textViewInfoLocationObtained);
        btnGetLocation = findViewById(R.id.btnGetCurrentLocation);
        btnChooseSpotPark = findViewById(R.id.btnChooseParkSpot);
        txtChoosePark = findViewById(R.id.txtViewChooseAPark);
        txtChooseSpot = findViewById(R.id.txtViewChooseASpot);
        spinnerPark = findViewById(R.id.spinnerParks);
        spinnerSpot = findViewById(R.id.spinnerSpots);
        btnUpload = findViewById(R.id.btnUploadPhoto);
        txtDescription = findViewById(R.id.editTxtDescription);
        txtInfoUploadedSuccess = findViewById(R.id.txtViewInfoUploadedSuccess);
    }

    private void putSpots(int park) {
        spotsIds = new LinkedList<>();
        List<Spot> spots = new LinkedList<>();

        if (park == 0) {
            spots = SpotsManager.INSTANCE.getParkingSpotsA();
        } else {
            // park D
            spots = SpotsManager.INSTANCE.getParkingSpotsD();
        }
        for(Spot s : spots) {
            spotsIds.add(s.getSpotId());
        }

        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spotsIds);
        spinnerSpot.setAdapter(spinnerAdapter);

    }

    private void setSpotSelected(int position) {
        spotSelectedIndex = position;
        spotSelected = spotsIds.get(spotSelectedIndex);
    }


    public static Intent getIntent(Context context) {
        return new Intent(context, ReportIncidentActivity.class);
    }

    public void btnGetCurrentLocationClick(View view) {
        btnGetLocation.setVisibility(View.GONE);
        txtViewInfoLocationObtained.setVisibility(View.VISIBLE);
        btnChooseSpotPark.setVisibility(View.GONE);

        Task<Location> loc = DashboardAuthActivity.getLocation();

        loc.addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    setCurrentLocation(location);
                }
            }
        });

    }

    public void setCurrentLocation(Location location) {
        this.currentLocation = location;
    }

    public void btnChooseAParkSpotClick(View view) {
        btnGetLocation.setVisibility(View.GONE);
        btnChooseSpotPark.setVisibility(View.GONE);
        txtChooseSpot.setVisibility(View.VISIBLE);
        txtChoosePark.setVisibility(View.VISIBLE);
        spinnerSpot.setVisibility(View.VISIBLE);
        spinnerPark.setVisibility(View.VISIBLE);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.maps, android.R.layout.simple_spinner_item);
        spinnerPark.setAdapter(spinnerAdapter);
        putSpots(0);
        spinnerPark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                putSpots(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });

        spinnerSpot.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //posicao seleciondada//id para mapeamento de BD
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSpotSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty
            }
        });
    }

    public void btnCreateIncidentReportClick(View view) {
        String description = txtDescription.getText().toString();

        if (description.trim().equalsIgnoreCase("")) {
            txtDescription.setError(getResources().getString(R.string.errorReportDescriptionEmpty));
            return;
        }

        if (currentLocation == null && spotSelected == null) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.errorReportLocationNotPut);
            return;
        }

        if (bitmap != null) {
            IncidentsReportsManager.INSTANCE.uploadPhoto(bitmap);
        }

        IncidentsReportsManager.INSTANCE.createNewIncidentReport(description, currentLocation, spotSelected, hasPhoto);

        showSuccessMessage();
    }


    public void btnUploadPhotoClick(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_STORAGE_REQUEST);
        } else {
            uploadImage();
        }
    }

    private void uploadImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, 0);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case PERMISSION_STORAGE_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permissão granted
                    uploadImage();
                    return;
                } else {
                    // permissão denied
                    InternetConnectionManager.INSTANCE.showErrorMessage(ReportIncidentActivity.this,R.string.errorPermissionStorageDenied);
                }
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == this.RESULT_CANCELED) {
            return;
        }

        if (data != null) {
            Uri contentURI = data.getData();
            try {
                Bitmap bm = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                // String path = saveImage(bitmap);
                bitmap = bm;
                btnUpload.setVisibility(View.GONE);
                txtInfoUploadedSuccess.setVisibility(View.VISIBLE);
                hasPhoto = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void showSuccessMessage() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.incidentReportedWithSuccess);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishActivity();
            }
        });


        builder.show();
    }

    private void finishActivity() {
        this.finish();
    }


}
