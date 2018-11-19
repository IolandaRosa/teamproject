package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import modelo.User;
import modelo.UsersManager;

public class ProfileActivity extends AppCompatActivity {
    private TextView txtName;
    private TextView txtEmail;
    private TextView txtfindMeAPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            startActivity(DashboardActivity.getIntent(this));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtName=findViewById(R.id.txtViewName);
        txtEmail=findViewById(R.id.txtViewEmail);
        txtfindMeAPreference=findViewById(R.id.textViewPreference);

        this.getProfile(UsersManager.INSTANCE.getUserProfileInfo());
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    public void getProfile(DatabaseReference ref) {
        ref.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user =dataSnapshot.getValue(User.class);

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
}