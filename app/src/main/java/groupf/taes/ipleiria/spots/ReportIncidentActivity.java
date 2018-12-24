package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import modelo.Spot;
import modelo.SpotsManager;

public class ReportIncidentActivity extends AppCompatActivity {
    private Button btnGetLocation;
    private Button btnChooseSpotPark;
    private TextView txtViewInfoLocationObtained;
    private TextView txtChoosePark;
    private TextView txtChooseSpot;
    private Spinner spinnerPark;
    private Spinner spinnerSpot;

    private SpinnerAdapter spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_incident);

        txtViewInfoLocationObtained = findViewById(R.id.textViewInfoLocationObtained);
        btnGetLocation = findViewById(R.id.btnGetCurrentLocation);
        btnChooseSpotPark = findViewById(R.id.btnChooseParkSpot);
        txtChoosePark = findViewById(R.id.txtViewChooseAPark);
        txtChooseSpot = findViewById(R.id.txtViewChooseASpot);
        spinnerPark = findViewById(R.id.spinnerParks);
        spinnerSpot = findViewById(R.id.spinnerSpots);

        spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.maps, android.R.layout.simple_spinner_item);
        spinnerPark.setAdapter(spinnerAdapter);
        putSpots(0);
        spinnerPark.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //posicao seleciondada//id para mapeamento de BD
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  putSpots(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //empty

            }
        });

    }

    private void putSpots(int park) {
        List<String> spotsIds = new LinkedList<>();
        List<Spot> spots = new LinkedList<>();
        if (park == 0) {
            spots = SpotsManager.INSTANCE.getParkingSpotsA();
        } else {
            // park D
            spots = SpotsManager.INSTANCE.getParkingSpotsD();
        }
        for(Spot s : spots) {
            spotsIds.add(s.getSpotId());
        }

        spinnerAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, spotsIds);
        spinnerSpot.setAdapter(spinnerAdapter);
    }



    public static Intent getIntent(Context context) {
        return new Intent(context, ReportIncidentActivity.class);
    }

    public void btnGetCurrentLocationClick(View view) {
        btnGetLocation.setVisibility(View.GONE);
        txtViewInfoLocationObtained.setVisibility(View.VISIBLE);
        btnChooseSpotPark.setVisibility(View.GONE);
    }

    public void btnChooseAParkSpotClick(View view) {
        btnGetLocation.setVisibility(View.GONE);
        btnChooseSpotPark.setVisibility(View.GONE);
        txtChooseSpot.setVisibility(View.VISIBLE);
        txtChoosePark.setVisibility(View.VISIBLE);
        spinnerSpot.setVisibility(View.VISIBLE);
        spinnerPark.setVisibility(View.VISIBLE);
    }
}
