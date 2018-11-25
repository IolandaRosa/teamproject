package modelo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import groupf.taes.ipleiria.spots.R;

public class SpotAdapter extends ArrayAdapter<Spot> {

    private ArrayList<Spot>spots;

    public SpotAdapter(@NonNull Context context, ArrayList<Spot> spots) {
        super(context, 0,spots);
        this.spots=spots;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Spot spot=getItem(position);

        //Se a vista for null então a lista ainda nao for carregada e vai carregada
        if(convertView==null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_list, parent, false);
        }

        TextView spotId = convertView.findViewById(R.id.spotId);
        TextView spotPark = convertView.findViewById(R.id.spotPark);
        TextView spotStatus = convertView.findViewById(R.id.spotStatus);
        TextView spotRate = convertView.findViewById(R.id.spotRate);

        spotId.setText("Name: "+spot.getSpotId());
        spotPark.setText("Park: "+spot.getPark());
        spotStatus.setText("Status: "+SpotsManager.INSTANCE.toStringStatus(spot.getStatus()));
        spotRate.setText("Rate: "+String.valueOf(spot.getRating()));


        Button button=convertView.findViewById(R.id.btnDelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersManager.INSTANCE.getFavouriteSpotsList().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot data : dataSnapshot.getChildren()){
                            Spot s = data.getValue(Spot.class);

                            if(s.getSpotId().equals(spot.getSpotId())){
                                data.getRef().removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

                spots.remove(position);
                notifyDataSetChanged();

            }
        });

        return convertView;
    }
}
