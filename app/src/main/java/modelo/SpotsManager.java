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

public enum SpotsManager {
    INSTANCE;
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private List<Spot> parkingSpotsA;
    private List<Spot> parkingSpotsD;
    private List<Spot> parkingSpotsTest;
   /* private int freeSpots = 0;
    private int ocuppiedSpots = 0; */
    private String dateOfData = null;
    private int freeSpotsParkA = 0;
    private int ocuppiedSpotsParkA = 0;
    private int freeSpotsParkD = 0;
    private int ocuppiedSpotsParkD = 0;

    private int freeSpotsPark = 0;
    private List<Spot> freeParkingSpots;

    private List<Spot> parkingSpotsOld;
    private List<Spot> parkingSpots;
    private boolean firstTime;

    SpotsManager() {
        // Write a message to the database
        database = FirebaseDatabase.getInstance();

//        database.setPersistenceEnabled(true);
        dbRef = database.getReference().child("ParkingSpots");
        dbRef.keepSynced(true);
        parkingSpots = new LinkedList<>();
        parkingSpotsA = new LinkedList<>();
        parkingSpotsD = new LinkedList<>();
        parkingSpotsTest = new LinkedList<>();
        freeParkingSpots = new LinkedList<>();
        parkingSpotsOld = new LinkedList<>();
        firstTime = true;
        //writeSpotsOnDatabase();
       // readSpotsDataFromDatabase();
    }

    public void writeSpotsOnDatabase() {
        List<Spot> spots = new LinkedList<>();

        spots.add(new Spot("A-1", "A", "39.734859,-8.820784", 0, 2));
        spots.add(new Spot("A-2", "A", "39.734884,-8.820745", 1, 3));
        spots.add(new Spot("A-3", "A", "39.734909,-8.820708", 0, 1));
        spots.add(new Spot("A-4", "A", "39,734905,-8.820718", 1, 4));
        spots.add(new Spot("D-1", "D", "39.733888,-8.821332", 0, 5));
        spots.add(new Spot("D-2", "D", "39.733917,-8.821326", 1, 2));
        spots.add(new Spot("D-3", "D", "39.733937,-8.821340", 0, 1));
        spots.add(new Spot("D-4", "D", "39,734942,-8.821347", 1, 4));


        for(Spot s : spots) {
            // dbRef.child("ParkingSpots").child(s.getSpotParked()).child("Park").setValue(s.getPark());
            dbRef.child(s.getSpotId()).child("Park").setValue(s.getPark());
            dbRef.child(s.getSpotId()).child("LocationGeo").setValue(s.getLocationGeo());
            dbRef.child(s.getSpotId()).child("Status").setValue(s.getStatus());
            dbRef.child(s.getSpotId()).child("Rating").setValue(s.getRating());
        }
    }


    public void readSpotsDataFromDatabase() {
        // Attach a listener to read the data at our posts reference


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                freeSpotsParkA = 0;
                freeSpotsParkD = 0;
                ocuppiedSpotsParkA = 0;
                ocuppiedSpotsParkD = 0;
                parkingSpotsA = new LinkedList<>();
                parkingSpotsD = new LinkedList<>();

                parkingSpotsA = new LinkedList<>();
                parkingSpotsD = new LinkedList<>();

                //mudei aqui


                parkingSpotsOld = parkingSpots;
                parkingSpots = new LinkedList<>();


                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot d : children) {
                    //System.out.printf("spoats amanger spot: " + d);
                    Spot spot = new Spot(d.getKey(), d.child("Park").getValue().toString(), d.child("LocationGeo").getValue().toString(), Integer.parseInt(d.child("Status").getValue().toString()), Integer.parseInt(d.child("Rating").getValue().toString()));
                    parkingSpots.add(spot);
                    if (spot.getPark().equalsIgnoreCase("A")) {
                        parkingSpotsA.add(spot);
                        if (spot.getStatus() == 0) {
                            freeSpotsParkA++;
                        } else {
                            ocuppiedSpotsParkA++;
                        }
                    } else {
                        parkingSpotsD.add(spot);
                        if (spot.getStatus() == 0) {
                            freeSpotsParkD++;
                        } else {
                            ocuppiedSpotsParkD++;
                        }
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

    public String getDateOfData() {
        return dateOfData;
    }

    public void setDateOfData(String dateOfData) {
        this.dateOfData = dateOfData;
    }


    public List<Spot> getParkingSpotsA() {
        return parkingSpotsA;
    }

    public List<Spot> getParkingSpotsD() {
        return parkingSpotsD;
    }

    public int getFreeSpotsParkA() {
        return freeSpotsParkA;
    }

    public int getOcuppiedSpotsParkA() {
        return ocuppiedSpotsParkA;
    }

    public int getFreeSpotsParkD() {
        return freeSpotsParkD;
    }

    public int getOcuppiedSpotsParkD() {
        return ocuppiedSpotsParkD;
    }

    public String toStringStatus(int status) {
        return status==0?"Free    ":"Occupied";
    }


   public List<Spot> getFreeParkingSpots(int park) {

       List<Spot> freeSpots = new LinkedList<>();
       List<Spot> freeParkingSpotsCompare = park == 0? parkingSpotsA : parkingSpotsD;
       if(park == 0)
       {
           for (Spot spot: freeParkingSpotsCompare) {
                if(spot.getStatus() == 0)
                {
                    freeSpots.add(spot);
                }
           }
       }
       return freeSpots;
   }

    public void setSpotStatusToOccupied(String id) {
        dbRef.child(id).child("Status").setValue(1);
    }

   /*
   public void addFindPreferenceToAUser(String id, FindPreference findPreference) {
        mDatabase.child(id).child("findPreference").setValue(findPreference);
    }
    */

    //teste
    public void setParkingSpotsTest(List<Spot> spots)
    {
        parkingSpotsTest.addAll(spots);
    }

    public List<Spot> getParkingSpotsTest() {

        for (Spot spot:parkingSpotsTest) {
            if(spot.getStatus() == 1)
                parkingSpotsTest.remove(spot);
        }

        return parkingSpotsTest;
    }

    public void addSpotToDatabase(String spotId, String park, String locationGeo, int status, int rating) {
      //  Spot spot = new Spot(spotId, park, locationGeo, status, rating);
   //     dbRef.child(spotId).setValue(spot);
        dbRef.child(spotId).child("Park").setValue(park);
        dbRef.child(spotId).child("LocationGeo").setValue(locationGeo);
        dbRef.child(spotId).child("Status").setValue(status);
        dbRef.child(spotId).child("Rating").setValue(rating);
    }


    public void removeSpotFromDatabase(String spotId) {
        dbRef.child(spotId).removeValue();
    }

    public List<Spot> getParkingSpots() {
        return parkingSpots;
    }

    public List<Spot> getParkingSpotsOld() {
        return parkingSpotsOld;
    }
}


