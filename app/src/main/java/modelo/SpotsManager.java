package modelo;

import android.provider.ContactsContract;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.LinkedList;
import java.util.List;

public class SpotsManager {
    private static final SpotsManager INSTANCE = new SpotsManager();
    private DatabaseReference dbRef;
    private List<Spot> spots;

    public SpotsManager() {
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        dbRef = database.getReference();
        spots = new LinkedList<>();
        writeSpotsOnDatabase();
    }

    public void writeSpotsOnDatabase() {
        spots.add(new Spot("A-1", "A", "39.734859,-8.820784", 0));
        spots.add(new Spot("A-2", "A", "39.734884,-8.820745", 1));
        spots.add(new Spot("A-3", "A", "39.734909,-8.820708", 0));
        spots.add(new Spot("A-4", "A", "39,734905,-8.820718", 1));

        for(Spot s : spots) {
            dbRef.child("ParkingSpots").child(s.getSpotId()).child("Park").setValue(s.getPark());
            dbRef.child("ParkingSpots").child(s.getSpotId()).child("LocationGeo").setValue(s.getLocationGeo());
            dbRef.child("ParkingSpots").child(s.getSpotId()).child("Status").setValue(s.getStatus());
        }
    }

    public static SpotsManager getINSTANCE() {
        return INSTANCE;
    }

    public List<Spot> getSpots() {
        return spots;
    }
}
