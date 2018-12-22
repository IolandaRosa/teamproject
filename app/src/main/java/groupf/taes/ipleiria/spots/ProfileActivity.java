package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import modelo.User;
import modelo.UsersManager;

import static modelo.FindPreference.BEST_RATED;
import static modelo.FindPreference.CLOSER_LOCATION;
import static modelo.FindPreference.FAVOURITE_SPOTS;

public class ProfileActivity extends AppCompatActivity {
    private static final CountingIdlingResource idlingResource = new CountingIdlingResource("profile");
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtfindMeAPreference;
    private User user;
    private int currentPark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            startActivity(DashboardActivity.getIntent(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //this.user = (User) this.getIntent().getSerializableExtra("user");
        currentPark = this.getIntent().getIntExtra("currentPark", -1);


      //  this.user=null;
        txtName=findViewById(R.id.txtViewName);
        txtEmail=findViewById(R.id.txtViewEmail);
        txtfindMeAPreference=findViewById(R.id.textViewPreference);

        /*if (user != null) {
            txtName.setText(user.getName());
            txtEmail.setText(user.getEmail());

            if(user.getFindPreference()!=null){
                txtfindMeAPreference.setText(UsersManager.INSTANCE.toStringPreference(user.getFindPreference()));
            }
        } else {  // principalmente por causa dos testes
            this.getProfile(UsersManager.INSTANCE.getUserProfileInfo());
        }*/

        this.getProfile(UsersManager.INSTANCE.getUserProfileInfo());
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }


    public void getProfile(DatabaseReference ref) {
        idlingResource.increment();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                idlingResource.decrement();
                user =dataSnapshot.getValue(User.class);

                txtName.setText(user.getName());
                txtEmail.setText(user.getEmail());

                if(user.getFindPreference()!=null){
                    txtfindMeAPreference.setText(UsersManager.INSTANCE.toStringPreference(user.getFindPreference()));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void onClick_updateMyProfile(View view) {
        if(user!=null){
            startActivity(UpdateProfileActivity.getIntent(this, user));
        }
    }


    public void onClick_btnFavouriteSpots(View view) {
        startActivity(FavouriteSpotsListActivity.getIntent(this));
    }

    public void onClick_findMeASpot(View view) {
        findMeASpot();
    }


    public void findMeASpot() {
        if (user.getFindPreference() == null) {
            startActivity(ChooseAPreferenceActivity.getIntent(this).putExtra("user", user));
        } else {
            LatLng choosenSpot = null;
            //LatLng currentLocation = null;
            switch (user.getFindPreference()) {
                case BEST_RATED:
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", user).putExtra("preference", 0).putExtra("park",currentPark));
                    break;

                case CLOSER_LOCATION:
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", user).putExtra("preference", 1).putExtra("park",currentPark));
                    break;

                case FAVOURITE_SPOTS:
                    startActivity(FindMeASpotActivity.getIntent(this).putExtra("user", user).putExtra("preference", 2).putExtra("park",currentPark));
                    break;

            }

        }
    }
    public static CountingIdlingResource getIdlingResource() {
        return idlingResource;
    }

}
