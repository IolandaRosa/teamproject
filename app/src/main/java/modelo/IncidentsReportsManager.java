package modelo;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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


    IncidentsReportsManager() {
        database = FirebaseDatabase.getInstance();

        dbRef = database.getReference().child("ReportOfIncidents");
        dbRef.keepSynced(true);

        incidents = new LinkedList<>();
    }

    public void getIncidentsReporstFromDatabase() {

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot d : children) {
               //     int id = -1;
                    String description = null;
                    String location = null;
                    String spotId = null;


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

                    IncidentReport i = new IncidentReport(description, location, spotId);
                    incidents.add(i);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

    }

    public void createNewIncidentReport(String description, Location location, String spotId) {
        String loc = null;
        if (location != null) {
            loc = location.getLatitude() + "," + location.getLongitude();
        }
        IncidentReport incident = new IncidentReport(description, loc, spotId);

        dbRef.push().setValue(incident);
    }


}
