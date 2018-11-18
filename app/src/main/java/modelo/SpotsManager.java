package modelo;

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

public class SpotsManager {
    private static final SpotsManager INSTANCE = new SpotsManager();
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private List<Spot> spots;
    private int freeSpots = 0;
    private int ocuppiedSpots = 0;
    private String dateOfData = null;


    public SpotsManager() {
        // Write a message to the database
        database = FirebaseDatabase.getInstance();

//        database.setPersistenceEnabled(true);
        dbRef = database.getReference().child("ParkingSpots");
        dbRef.keepSynced(true);
        spots = new LinkedList<>();


        //writeSpotsOnDatabase();
       // readSpotsDataFromDatabase();
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
        // Attach a listener to read the data at our posts reference


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for (DataSnapshot d : children) {
                    Spot spot = new Spot(d.getKey(), d.child("Park").getValue().toString(), d.child("LocationGeo").getValue().toString(), Integer.parseInt(d.child("Status").getValue().toString()));
                    spots.add(spot);
                    if (spot.getStatus() == 0) {
                        freeSpots++;
                    } else {
                        ocuppiedSpots++;
                    }
                }

                DateFormat df = new SimpleDateFormat("EEE, d MMM yyyy, HH:mm:ss");
                String date = df.format(Calendar.getInstance().getTime());
                setDateOfData(date);
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

    public int getFreeSpots() {
        return freeSpots;
    }

    public int getOcuppiedSpots() {
        return ocuppiedSpots;
    }

    public String getDateOfData() {
        return dateOfData;
    }

    public void setDateOfData(String dateOfData) {
        this.dateOfData = dateOfData;
    }


}
