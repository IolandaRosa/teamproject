package modelo;

import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public enum IncidentsReportsManager {
    INSTANCE;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private List<IncidentReport> incidents;

    private StorageReference storageRef;


    IncidentsReportsManager() {
        database = FirebaseDatabase.getInstance();

        dbRef = database.getReference().child("ReportOfIncidents");
        dbRef.keepSynced(true);

      //  incidents = new LinkedList<>();

        FirebaseStorage storage = FirebaseStorage.getInstance();

        this.storageRef = storage.getReference().child("incidents");
    }

    public void setIncidentsList(List<IncidentReport> incidents) {
        this.incidents = incidents;
    }

    public void getIncidentsReporstFromDatabase() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                incidents = new LinkedList<>();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot d : children) {
                    int id = -1;
                    String description = null;
                    String location = null;
                    String spotId = null;
                    int hasPhoto = -1;


                    Object idObject = d.child("id").getValue();
                    if (idObject != null) {
                        id = Integer.parseInt(idObject.toString());
                    }

                    Object descriptionObject = d.child("description").getValue();
                    if (descriptionObject != null) {
                        description = descriptionObject.toString();
                    }

                    Object locationObject = d.child("location").getValue();
                    if (locationObject != null) {
                        location = locationObject.toString();
                    }

                    Object spotObject = d.child("spotId").getValue();
                    if (spotObject != null) {
                        spotId = spotObject.toString();
                    }

                    Object photoObject = d.child("hasPhoto").getValue();
                    if (photoObject != null) {
                        hasPhoto = Integer.parseInt(photoObject.toString());
                    }

                    IncidentReport i = new IncidentReport(id, description, location, spotId, hasPhoto);
                    incidents.add(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public int getNextId() {
        int id;
        if (incidents.size() == 0) {
            id = 1;
        } else {
            id = incidents.size() + 1;
        }
        return id;
    }


    public void uploadPhoto(Bitmap bm) {
        int id = getNextId();

        StorageReference photoRef = this.storageRef.child(id + ".jpg");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = photoRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        });
    }

    public void createNewIncidentReport(String description, Location location, String spotId, int hasPhoto) {
        String loc = null;
        if (location != null) {
            loc = location.getLatitude() + "," + location.getLongitude();
        }


        int id = getNextId();

        IncidentReport incident = new IncidentReport(id, description, loc, spotId, hasPhoto);

        dbRef.child(String.valueOf(id)).setValue(incident);
    }

    public List<IncidentReport> getIncidents() {
        return this.incidents;
    }

    // para testes
    public void addIncidentToDatabase(int id, String description, String loc, String spotId) {


        IncidentReport incident = new IncidentReport(id, description, loc, spotId, 0);

        dbRef.child(String.valueOf(id)).setValue(incident);
    }

    public void removeIncidentFromDatabase(int id) {
        dbRef.child(String.valueOf(id)).removeValue();
    }


}
