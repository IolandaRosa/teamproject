package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import modelo.User;

public class ChooseAPreferenceActivity extends AppCompatActivity {
    private Button btnFavouriteSpot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_apreference);

        User user = (User) this.getIntent().getSerializableExtra("user");
        btnFavouriteSpot = findViewById(R.id.btnOneOfFavourites);
        if (user.getFavouriteSpots().size() == 0) {
            btnFavouriteSpot.setVisibility(View.INVISIBLE);
        } else {
            btnFavouriteSpot.setVisibility(View.VISIBLE);
        }
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, ChooseAPreferenceActivity.class);
    }

    public void onClickBtnBestRatedSpot(View view) {
    }

    public void onClickBtnCloserToMe(View view) {
    }

    public void onClickBtnOneOfFavouriteSpots(View view) {
    }

}
