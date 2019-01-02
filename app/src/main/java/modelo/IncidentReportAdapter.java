package modelo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import groupf.taes.ipleiria.spots.R;

public class IncidentReportAdapter extends ArrayAdapter<IncidentReport> {

    private ArrayList<IncidentReport> incidents;

    public IncidentReportAdapter(@NonNull Context context, ArrayList<IncidentReport> incidents) {
        super(context, 0, incidents);
        this.incidents = incidents;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final IncidentReport incident = getItem(position);

        //Se a vista for null então a lista ainda nao for carregada e vai carregada
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.incidents_list, parent, false);
        }

        TextView description = convertView.findViewById(R.id.txtViewDescription);
        TextView spotLoc = convertView.findViewById(R.id.txtViewSpot);
        TextView location = convertView.findViewById(R.id.txtViewLocation);

        if (incident.getDescription().length() < 20) {
            description.setText(incident.getDescription());
        } else {
            String d = incident.getDescription().substring(0, 20);
            description.setText(d + "...");
        }


        if (incident.getSpotId() != null) {
            spotLoc.setText(incident.getSpotId());
            location.setVisibility(View.GONE);
        } else {
            spotLoc.setVisibility(View.GONE);
            location.setVisibility(View.VISIBLE);
            location.setText(incident.getLocation());
        }

        return convertView;
    }
}
