package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import modelo.IncidentReport;
import modelo.IncidentReportAdapter;
import modelo.IncidentsReportsManager;

public class IncidentsReportsListActivity extends AppCompatActivity {

    private ListView incidentsList;
    private List<IncidentReport> incidents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_incidents_reports_list);

        incidentsList = findViewById(R.id.incidentsList);

        incidents = new ArrayList<>(IncidentsReportsManager.INSTANCE.getIncidents());

        if (incidents.size() == 0) {
            showErrorMessageEmptyList();
            incidentsList.setVisibility(View.GONE);
        }

        IncidentReportAdapter adapter = new IncidentReportAdapter(this, (ArrayList<IncidentReport>) incidents);

        incidentsList.setAdapter(adapter);

        incidentsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showIncidentDetails(i);
            }
        });

    }

    private void showIncidentDetails(int position) {
        IncidentReport incident = incidents.get(position);

        startActivity(IncidentReportDetailsActivity.getIntent(this, incident));
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, IncidentsReportsListActivity.class);
    }


    public void showErrorMessageEmptyList() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);

        builder.setIconAttribute(android.R.attr.alertDialogIcon);
        builder.setTitle(R.string.app_name);
        builder.setMessage(R.string.incidentsEmptyList);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                //startActivity(DashboardAuthActivity.getIntent(FavouriteSpotsListActivity.this));
            }
        });

        builder.show();
    }

}
