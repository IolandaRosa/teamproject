package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import modelo.Spot;
import modelo.SpotsManager;

public class AlghorithmPerformanceActivity extends AppCompatActivity {

    private TextView ocupationRateTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alghorithm_performance);

        this.ocupationRateTxt = findViewById(R.id.txtOccupationRate);

        computeOccupationRate();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, AlghorithmPerformanceActivity.class);
    }

    public void computeOccupationRate() {
        SpotsManager.INSTANCE.getDbRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
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

                    String multiline="Park A - "+String.format("%.2f",occupationRateA)+"%\nPark D - "+String.format("%.2f",occupationRateD)+"%";
                    ocupationRateTxt.setText(multiline);


                } catch (
                        Exception e)

                {
                    Log.d("e", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
