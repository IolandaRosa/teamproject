package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class DashboardAuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       // FirebaseAuth.getInstance().signOut();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_auth);

        if(FirebaseAuth.getInstance().getCurrentUser()==null){
            startActivity(DashboardActivity.getIntent(this));
            return;
        }

    }


    public void onClick_btnMyProfile(View view) {
        startActivity(ProfileActivity.getIntent(this));
    }



    public static Intent getIntent(Context context) {
        return new Intent(context, DashboardAuthActivity.class);
    }


}
