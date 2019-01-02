package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import modelo.IncidentReport;

public class IncidentReportDetailsActivity extends PerformanceButtonActivity {
    private static IncidentReport currentIncident;

    private TextView txtDescription;
    private TextView txtLocation;
    private TextView txtSpotLocation;
    private ImageView image;

    private StorageReference storageRef;


    @Override
    protected View childView() {
        return getLayoutInflater().inflate(R.layout.activity_incident_report_details,null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        this.storageRef = storage.getReference().child("incidents");

        txtDescription = findViewById(R.id.txtReportDetailDescription);
        txtLocation = findViewById(R.id.txtReportDetailLocation);
        txtSpotLocation = findViewById(R.id.txtReportDetailSpot);
        image = findViewById(R.id.imageView);

        if (currentIncident.getHasPhoto() == 1) {
            getImage();
        }

        if (currentIncident.getSpotId() == null) {
            findViewById(R.id.textViewLocationDetail).setVisibility(View.VISIBLE);
            txtLocation.setVisibility(View.VISIBLE);
            txtSpotLocation.setVisibility(View.GONE);
            findViewById(R.id.textViewSpotDetail).setVisibility(View.GONE);
            txtLocation.setText(currentIncident.getLocation());
        } else {
            txtSpotLocation.setText(currentIncident.getSpotId());
        }

        txtDescription.setText(currentIncident.getDescription());
    }


    private void getImage() {
        int id = currentIncident.getId();

        try {
            final File localFile = File.createTempFile("Images", "bmp");
            storageRef.child(id + ".jpg").getFile(localFile).addOnSuccessListener(new OnSuccessListener< FileDownloadTask.TaskSnapshot >() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    image.setImageBitmap(BitmapFactory.decodeFile(localFile.getAbsolutePath()));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Intent getIntent(Context context, IncidentReport incident) {
        currentIncident = incident;
        return new Intent(context, IncidentReportDetailsActivity.class);
    }

}
