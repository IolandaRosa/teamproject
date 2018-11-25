package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import modelo.InternetConnectionManager;
import modelo.Spot;
import modelo.SpotAdapter;
import modelo.UsersManager;

public class FavouriteSpotsListActivity extends AppCompatActivity {

    private List<Spot> spots;
    private ListView spotsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_spots_list);

        this.spots=new ArrayList<>();
        this.spotsList = findViewById(R.id.spotsList);

        this.getSpotsList(UsersManager.INSTANCE.getFavouriteSpotsList());
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, FavouriteSpotsListActivity.class);
    }

    private void getSpotsList(DatabaseReference ref) {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    Spot spot = data.getValue(Spot.class);
                    spots.add(spot);
                }

                if(spots.isEmpty()){

                    InternetConnectionManager.INSTANCE.showErrorMessage(FavouriteSpotsListActivity.this,R.string.emptySpotsList);
                    spotsList.setVisibility(View.GONE);
                    return;
                }

                SpotAdapter spotAdapter = new SpotAdapter(FavouriteSpotsListActivity.this, (ArrayList<Spot>) spots);

                spotsList.setAdapter(spotAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }




}