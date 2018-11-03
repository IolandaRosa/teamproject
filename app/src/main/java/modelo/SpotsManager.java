package modelo;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;

public class SpotsManager {
    private static final SpotsManager INSTANCE = new SpotsManager();
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private List<Spot> spots;

    public SpotsManager() {
        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference().child("ParkingSpots");
        spots = new LinkedList<>();
       // writeSpotsOnDatabase();
        readSpotsDataFromDatabase();
    }

    public void writeSpotsOnDatabase() {
        spots.add(new Spot("A-1", "A", "39.734859,-8.820784", 0));
        spots.add(new Spot("A-2", "A", "39.734884,-8.820745", 1));
        spots.add(new Spot("A-3", "A", "39.734909,-8.820708", 0));
        spots.add(new Spot("A-4", "A", "39,734905,-8.820718", 1));

        for(Spot s : spots) {
           // dbRef.child("ParkingSpots").child(s.getSpotId()).child("Park").setValue(s.getPark());
            dbRef.child(s.getSpotId()).child("Park").setValue(s.getPark());
            dbRef.child(s.getSpotId()).child("LocationGeo").setValue(s.getLocationGeo());
            dbRef.child(s.getSpotId()).child("Status").setValue(s.getStatus());
        }
    }

    public void readSpotsDataFromDatabase() {

        DatabaseReference ref = database.getReference().child("ParkingSpots");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot d : children) {
                    Spot spot = new Spot(d.getKey(), d.child("Park").getValue().toString(), d.child("LocationGeo").getValue().toString(), Integer.parseInt(d.child("Status").getValue().toString()));
                    spots.add(spot);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });



    }

    public static SpotsManager getINSTANCE() {
        return INSTANCE;
    }

    public List<Spot> getSpots() {
        return spots;
    }
}
