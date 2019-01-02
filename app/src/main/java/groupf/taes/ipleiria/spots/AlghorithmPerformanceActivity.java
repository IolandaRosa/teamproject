package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import modelo.InternetConnectionManager;

public class AlghorithmPerformanceActivity extends AppCompatActivity {

    private TextView ocupationRateTxt;
    private TextView bestRatedTxt;
    private TextView closerLocationTxt;
    private TextView myFavouritesTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alghorithm_performance);

        this.ocupationRateTxt = findViewById(R.id.txtOccupationRate);
        this.bestRatedTxt = findViewById(R.id.txtBestRatedTime);
        this.closerLocationTxt=findViewById(R.id.txtCloserLocationTime);
        this.myFavouritesTxt=findViewById(R.id.txtMyFavouritesTime);

        computeOccupationRate();
        getAlgorithmMediumTime();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, AlghorithmPerformanceActivity.class);
    }

    public void computeOccupationRate() {
        FirebaseDatabase.getInstance().getReference().child("DailyOccupationRate").addValueEventListener(new ValueEventListener() {
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String multiline="";
                if(dataSnapshot.child(date).getValue()==null){
                    multiline = "Park A - No data available\nPark D - No data available";
                }
                else{
                    multiline="Park A - "+String.format("%.2f",Double.parseDouble(dataSnapshot.child(date).child("A").getValue().toString()))+"%\nPark D - "+String.format("%.2f",Double.parseDouble(dataSnapshot.child(date).child("D").getValue().toString()))+"%";
                }
                ocupationRateTxt.setText(multiline);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getAlgorithmMediumTime(){
        FirebaseDatabase.getInstance().getReference().child("Performance_Alghorithms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {

                    String bestRated = dataSnapshot.child("Best_Rated").getValue().toString();
                    String closerLocation = dataSnapshot.child("Closer_Location").getValue().toString();
                    String myFavourites = dataSnapshot.child("My_Favourites").getValue().toString();

                    double v = Double.parseDouble(bestRated);
                    int seconds = (int)Math.floor(v/1000);
                    String format = String.format("%2d s %.3f ms",seconds,Math.abs(seconds * 1000 - v));

                    bestRatedTxt.setText(format);

                    v = Double.parseDouble(closerLocation);
                    seconds = (int)Math.floor(v/1000);
                    format = String.format("%2d s %.3f ms",seconds,Math.abs(seconds * 1000 - v));
                    closerLocationTxt.setText(format);

                    v = Double.parseDouble(myFavourites);
                    seconds = (int)Math.floor(v/1000);
                    format = String.format("%2d s %.3f ms",seconds,Math.abs(seconds * 1000 - v));
                    myFavouritesTxt.setText(format);

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

    public void onClick_btnDisplayDataInsertionArea(View view) {
        startActivity(DatePickActivity.getIntent(this));
    }
}
