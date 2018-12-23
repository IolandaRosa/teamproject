package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import modelo.User;
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

    }

    public static Intent getIntent(Context context) {
        return new Intent(context, StatisticsActivity.class);
    }


}
