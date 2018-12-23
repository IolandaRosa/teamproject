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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import modelo.Spot;
import modelo.SpotsManager;
import modelo.UsersManager;

public class StatisticsActivity extends AppCompatActivity {

    private TextView textRegistered;
    private TextView textLogged;
    private TextView textParked;
    private TextView textTopRated;
    private TextView textTopParked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        this.textRegistered = findViewById(R.id.txtTotalRegisteredUsers);
        this.textLogged = findViewById(R.id.txtTotalLoggedUsers);
        this.textParked = findViewById(R.id.textTotalParkedUsers);
        this.textTopRated = findViewById(R.id.textTopBestRated);
        this.textTopParked = findViewById(R.id.textTopMostParked);

        fillTotalsIformation();
        fillTopInformation();
    }

    private void fillTotalsIformation(){
        UsersManager.INSTANCE.getmDatabase().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    int totalLoggedUsers=0;
                    int totalRegistered=0;
                    int totalParked=0;

                    for(DataSnapshot d:children){
                        d.child("logged").getValue().toString();
                        if(d.child("logged").getValue().toString().compareToIgnoreCase("true")==0){
                            totalLoggedUsers++;
                        }

                        if(d.child("spotParked").getValue()!=null){
                            totalParked++;
                        }
                        totalRegistered++;
                    }

                    textLogged.setText(String.valueOf(totalLoggedUsers));
                    textRegistered.setText(String.valueOf(totalRegistered));
                    textParked.setText(String.valueOf(totalParked));
                }
                catch (Exception e){
                    Log.d("e",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void fillTopInformation(){
        SpotsManager.INSTANCE.getDbRef().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                    List<Integer> bests = new ArrayList<>();
                    List<Integer> mosts = new ArrayList<>();

                    List<Spot> unorderedSpots = new LinkedList<>();
                    List<Spot> orderedSpots = new LinkedList<>();

                    for (DataSnapshot d : children) {
                        Spot spot = new Spot(d.getKey(), d.child("Park").getValue().toString(), d.child("LocationGeo").getValue().toString(), Integer.parseInt(d.child("Status").getValue().toString()), Integer.parseInt(d.child("Rating").getValue().toString()), Integer.parseInt(d.child("TotalOfParkings").getValue().toString()));

                        bests.add(spot.getRating());
                        mosts.add(spot.getTotalOfParkings());
                        unorderedSpots.add(spot);

                    }

                    //Ordena de forma decrescente
                    Collections.sort(bests);
                    Collections.sort(mosts);

                    int k=bests.size()-1;

                    //Percorre a lista e adiciona os valores do best
                    for(int i=0;i<5;i++){
                        Integer bestValue = bests.get(k);
                        for(Spot s:unorderedSpots){
                            if(s.getRating()==bestValue && !orderedSpots.contains(s)){
                                orderedSpots.add(s);
                                break;
                            }
                        }
                        k--;
                    }

                    String multilineBest = "";

                    //Guarda as strings na variavel
                    for (int i = 0; i < 5; i++) {
                        multilineBest += "Spot: " + "\"" + orderedSpots.get(i).getSpotId() + "\"" + " Rated Value: " + String.valueOf(orderedSpots.get(i).getRating()) + " Stars\n";
                    }

                    //Preenche valores de most
                    orderedSpots.clear();

                    k=mosts.size()-1;

                    //Percorre a lista e adiciona os valores do best
                    for(int i=0;i<5;i++){
                        Integer bestValue = mosts.get(k);
                        for(Spot s:unorderedSpots){
                            if(s.getTotalOfParkings()==bestValue && !orderedSpots.contains(s)){
                                orderedSpots.add(s);
                                break;
                            }
                        }
                        k--;
                    }

                    String multilineMostParked = "";

                    for (int i = 0; i < 5; i++) {
                        multilineMostParked += "Spot: " + "\"" + orderedSpots.get(i).getSpotId() + "\"" + " Total Parkings: " + String.valueOf(orderedSpots.get(i).getTotalOfParkings()) +"\n";
                    }

                    textTopRated.setText(multilineBest);
                    textTopParked.setText(multilineMostParked);

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

    public static Intent getIntent(Context context) {
        return new Intent(context, StatisticsActivity.class);
    }


}
