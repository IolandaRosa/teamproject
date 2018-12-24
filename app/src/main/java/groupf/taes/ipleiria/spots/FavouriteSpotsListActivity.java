package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import modelo.Spot;
import modelo.SpotAdapter;
import modelo.UsersManager;

public class FavouriteSpotsListActivity extends PerformanceButtonActivity /*AppCompatActivity*/ {

    private List<Spot> spots;
    private ListView spotsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_favourite_spots_list);

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
                try{
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        Spot spot = data.getValue(Spot.class);
                        spots.add(spot);
                    }

                    if(spots.isEmpty()){

                        showErrorMessage(FavouriteSpotsListActivity.this,R.string.emptySpotsList);
                        spotsList.setVisibility(View.GONE);
                        return;
                    }

                    SpotAdapter spotAdapter = new SpotAdapter(FavouriteSpotsListActivity.this, (ArrayList<Spot>) spots);

                    spotsList.setAdapter(spotAdapter);
                }
                catch(Exception e){
                    Log.d("Exception",e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void showErrorMessage(Context context, int message) {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);

        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                //startActivity(DashboardAuthActivity.getIntent(FavouriteSpotsListActivity.this));
            }
        });

        builder.show();
    }

    @Override
    protected View childView() {
        return getLayoutInflater().inflate(R.layout.activity_favourite_spots_list,null);
    }


}
