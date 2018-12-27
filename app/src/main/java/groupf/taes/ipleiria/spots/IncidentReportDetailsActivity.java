package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import modelo.IncidentReport;

public class IncidentReportDetailsActivity extends AppCompatActivity {
    private static IncidentReport currentIncident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incident_report_details);
    }

    public static Intent getIntent(Context context, IncidentReport incident) {
        currentIncident = incident;
        return new Intent(context, IncidentReportDetailsActivity.class);
    }

}
