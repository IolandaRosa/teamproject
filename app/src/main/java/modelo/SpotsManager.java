package modelo;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public enum SpotsManager {
    INSTANCE;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private List<Spot> parkingSpotsA;
    private List<Spot> parkingSpotsD;
    private List<Spot> parkingSpotsTest;
    private String dateOfData = null;
    private int freeSpotsParkA = 0;
    private int ocuppiedSpotsParkA = 0;
    private int freeSpotsParkD = 0;
    private int ocuppiedSpotsParkD = 0;

    private List<Spot> parkingSpotsOld;
    private List<Spot> parkingSpots;

    SpotsManager() {
        // Write a message to the database
        database = FirebaseDatabase.getInstance();

        dbRef = database.getReference().child("ParkingSpots");
        dbRef.keepSynced(true);
        parkingSpots = new LinkedList<>();
        parkingSpotsA = new LinkedList<>();
        parkingSpotsD = new LinkedList<>();
        parkingSpotsTest = new LinkedList<>();

        parkingSpotsOld = new LinkedList<>();
    }

    public void writeSpotsOnDatabase() {
        List<Spot> spots = new LinkedList<>();

        spots.add(new Spot("A-1", "A", "39.734859,-8.820784", 0, 2,0));
        spots.add(new Spot("A-2", "A", "39.734884,-8.820745", 1, 3,0));
        spots.add(new Spot("A-3", "A", "39.734909,-8.820708", 0, 1,0));
        spots.add(new Spot("A-4", "A", "39,734905,-8.820718", 1, 4,0));
        spots.add(new Spot("A-5", "A", "39.734928,-8.820685", 0, 3,0));
        spots.add(new Spot("A-6", "A", "39.734946,-8.820649", 0, 3,0));
        spots.add(new Spot("A-7", "A", "39.734964,-8.820616", 0, 3,0));
        spots.add(new Spot("A-8", "A", "39.734981,-8.820590", 0, 3,0));
        spots.add(new Spot("A-9", "A", "39.735001,-8.820561", 0, 3,0));
        spots.add(new Spot("A-10", "A", "39.735013,-8.820535", 0, 3,0));
        spots.add(new Spot("A-11", "A", "39.735034,-8.820499", 0, 3,0));
        spots.add(new Spot("A-12", "A", "39.735051,-8.820471", 0, 3,0));
        spots.add(new Spot("A-13", "A", "39.735072,-8.820447", 0, 3,0));
        spots.add(new Spot("A-14", "A", "39.735090,-8.820414", 0, 3,0));
        spots.add(new Spot("A-15", "A", "39.735109,-8.820379", 0, 3,0));


        spots.add(new Spot("D-1", "D", "39.733888,-8.821332", 0, 5,0));
        spots.add(new Spot("D-2", "D", "39.733917,-8.821326", 1, 2,0));
        spots.add(new Spot("D-3", "D", "39.733937,-8.821340", 0, 1,0));
        spots.add(new Spot("D-4", "D", "39,734942,-8.821347", 1, 4,0));

        spots.add(new Spot("D-5", "D", "39.733937,-8.8213403", 1, 5,0));
        spots.add(new Spot("D-6", "D", "39.734079, -8.821500", 1, 2,0));
        spots.add(new Spot("D-7", "D", "39.734067, -8.821479", 1, 1,0));
        spots.add(new Spot("D-8", "D", "39.734110, -8.821521", 1, 4,0));

        for(Spot s : spots) {
            // dbRef.child("ParkingSpots").child(s.getSpotParked()).child("Park").setValue(s.getPark());
            dbRef.child(s.getSpotId()).child("Park").setValue(s.getPark());
            dbRef.child(s.getSpotId()).child("LocationGeo").setValue(s.getLocationGeo());
            dbRef.child(s.getSpotId()).child("Status").setValue(s.getStatus());
            dbRef.child(s.getSpotId()).child("Rating").setValue(s.getRating());
            dbRef.child(s.getSpotId()).child("TotalOfParkings").setValue(s.getTotalOfParkings());
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

                parkingSpotsOld = parkingSpots;
                parkingSpots = new LinkedList<>();


                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                for (DataSnapshot d : children) {
                    //System.out.printf("spoats amanger spot: " + d);
                    Spot spot = new Spot(d.getKey(), d.child("Park").getValue().toString(), d.child("LocationGeo").getValue().toString(), Integer.parseInt(d.child("Status").getValue().toString()), Integer.parseInt(d.child("Rating").getValue().toString()),Integer.parseInt(d.child("TotalOfParkings").getValue().toString()));
                    parkingSpots.add(spot);
                   // occupationValues.put(spot.getSpotId(),  d.child("TotalOfParkings")==null?0:(Long)d.child("TotalOfParkings").getValue());
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

    public void setSpotStatusToOccupied(String id,boolean parking) {
        dbRef.child(id).child("Status").setValue(1);

        int index=getSpotIndexById(id);
        if(index!=-1 && parking){
            Spot spot = this.parkingSpots.get(index);
            spot.setTotalOfParkings();
            this.parkingSpots.set(index,spot);
            dbRef.child(id).child("TotalOfParkings").setValue(spot.getTotalOfParkings());
        }

    }

    public void setSpotStatusToOccupiedTest(String id) {
        dbRef.child(id).child("Status").setValue(1);

    }

    public void setSpotStatusToFree(String id) {
        dbRef.child(id).child("Status").setValue(0);
    }

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
        dbRef.child(spotId).child("Park").setValue(park);
        dbRef.child(spotId).child("LocationGeo").setValue(locationGeo);
        dbRef.child(spotId).child("Status").setValue(status);
        dbRef.child(spotId).child("Rating").setValue(rating);
        dbRef.child(spotId).child("TotalOfParkings").setValue(0);
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

    public Spot getSpotFromId(String spotId) {
        for (Spot s : getParkingSpots()) {
            if (s.getSpotId().equalsIgnoreCase(spotId)) {
                return s;
            }
        }
        return null;
    }

    public void setSpotRate(String spotId, int rating) {
        dbRef.child(spotId).child("Rating").setValue(rating);
    }

    public void setTotalParkingTimesOnSpot(String id) {

        int index=getSpotIndexById(id);
        if(index!=-1){
            Spot spot = this.parkingSpots.get(index);
            spot.setTotalOfParkings();
            this.parkingSpots.set(index,spot);
            dbRef.child(id).child("TotalOfParkings").setValue(spot.getTotalOfParkings());
        }
    }

    public int getSpotIndexById(String id){
        for (int i = 0; i < this.parkingSpots.size(); i++) {
            String spotId = parkingSpots.get(i).getSpotId();
            if(spotId.compareToIgnoreCase(id)==0)
                return i;
        }

        return -1;
    }

    public DatabaseReference getDbRef() {
        return dbRef;
    }

    public void updateDailyOccupationRate(){
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                int totalASpots = 0;
                int totalAOccupiedSpots = 0;
                int totalDSpots = 0;
                int totalDOccupiedSpots = 0;
                double occupationRateA = 100;
                double occupationRateD = 100;

                for (DataSnapshot d : children) {
                    Spot spot = new Spot(d.getKey(), d.child("Park").getValue().toString(), d.child("LocationGeo").getValue().toString(), Integer.parseInt(d.child("Status").getValue().toString()), Integer.parseInt(d.child("Rating").getValue().toString()), Integer.parseInt(d.child("TotalOfParkings").getValue().toString()));

                    if (spot.getPark().compareToIgnoreCase("A") == 0) {
                        totalASpots++;
                        if (spot.getStatus() == 1) {
                            totalAOccupiedSpots++;
                        }
                    } else {
                        totalDSpots++;
                        if (spot.getStatus() == 1) {
                            totalDOccupiedSpots++;
                        }
                    }
                }

                if (totalAOccupiedSpots > 0 && totalASpots > 0) {
                    occupationRateA = ((double) totalAOccupiedSpots / totalASpots) * 100;
                }

                if (totalDOccupiedSpots > 0 && totalDSpots > 0) {
                    occupationRateD = ((double) totalDOccupiedSpots / totalDSpots) * 100;
                }

                //Calculou a taxa de ocupação
                //guarda os valores
                putValuesOnDatabase(occupationRateA,occupationRateD);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void putValuesOnDatabase(final double occupationRateA, final double occupationRateD) {


        final DatabaseReference dBOccupationRate = FirebaseDatabase.getInstance().getReference().child("DailyOccupationRate");

        dBOccupationRate.addListenerForSingleValueEvent(new ValueEventListener() {
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(date).getValue()==null){
                    dBOccupationRate.child(date).setValue("A");
                    dBOccupationRate.child(date).setValue("D");
                }

                if(dataSnapshot.child(date).child("A").getValue()==null){
                    dBOccupationRate.child(date).child("A").setValue(String.valueOf(occupationRateA));
                    dBOccupationRate.child(date).child("D").setValue(String.valueOf(occupationRateD));
                }
                else{
                    double occupationA = Double.parseDouble(dataSnapshot.child(date).child("A").getValue().toString());
                    double occupationD = Double.parseDouble(dataSnapshot.child(date).child("D").getValue().toString());

                    dBOccupationRate.child(date).child("A").setValue(String.valueOf((occupationA+occupationRateA)/2));
                    dBOccupationRate.child(date).child("D").setValue(String.valueOf((occupationD+occupationRateD)/2));

                }

                dBOccupationRate.removeEventListener(this);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}


