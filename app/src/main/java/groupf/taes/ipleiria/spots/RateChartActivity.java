package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import modelo.HistoryValue;
import modelo.InternetConnectionManager;
import modelo.MyDateComp;

import static java.util.Calendar.DATE;

public class RateChartActivity extends PerformanceButtonActivity/*AppCompatActivity*/ {

    private Date initalDate;
    private Date finalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.initalDate = parseDate(this.getIntent().getStringExtra("init"));
        this.finalDate = parseDate(this.getIntent().getStringExtra("final"));

        if (initalDate != null && finalDate != null) {
            drawChart();
        }
    }

    private Date parseDate(String dateString) {
        try {
            String[] splitDate = dateString.split("/");

            dateString = splitDate[0] + "-" + splitDate[1] + "-" + splitDate[2];

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

            return dateFormat.parse(dateString);
        } catch (Exception e) {
            Log.d("Parse Exception", e.getMessage());
            return null;
        }
    }

    public void drawChart() {
        FirebaseDatabase.getInstance().getReference().child("DailyOccupationRate").addValueEventListener(new ValueEventListener() {

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            List<HistoryValue> historicA = new LinkedList<>();
            List<HistoryValue> historicD = new LinkedList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();

                try {
                    for (DataSnapshot d : children) {
                        String date = d.getKey();

                        Date parseDate = dateFormat.parse(date);

                        //se a data estiver dentro do intervalo adiciona à lista
                        if (parseDate.compareTo(initalDate) >= 0 && parseDate.compareTo(finalDate) <= 0) {
                            double valueA = Double.parseDouble(d.child("A").getValue().toString());
                            double valueD = Double.parseDouble(d.child("D").getValue().toString());

                            HistoryValue hA = new HistoryValue(parseDate, valueA);
                            HistoryValue hD = new HistoryValue(parseDate, valueD);

                            historicA.add(hA);
                            historicD.add(hD);
                        }


                    }
                } catch (Exception ex) {
                    Log.d("ex", ex.getMessage());
                }

                //ver se lista esta vazia
                if (historicA.isEmpty() && historicD.isEmpty()) {
                    InternetConnectionManager.INSTANCE.showErrorMessage(RateChartActivity.this, R.string.noDataFound);
                    return;
                }

                //Ordenar as listas com o comparador customizado
                MyDateComp myDateComp = new MyDateComp();

                Collections.sort(historicA, myDateComp);
                Collections.sort(historicD, myDateComp);

                //Construir linha do A
                DataPoint[] dataPointsA = new DataPoint[historicA.size()];
                for (int i = 0; i < historicA.size(); i++) {
                    DataPoint point = new DataPoint(historicA.get(i).getDate().getTime(), historicA.get(i).getValue());

                    dataPointsA[i] = point;

                }
                LineGraphSeries<DataPoint> seriesA = new LineGraphSeries<>(dataPointsA);
                seriesA.setTitle("Park A");

                //Construir linha do D
                DataPoint[] dataPointsD = new DataPoint[historicA.size()];
                for (int i = 0; i < historicA.size(); i++) {
                    DataPoint point = new DataPoint(historicD.get(i).getDate().getTime(), historicD.get(i).getValue());

                    dataPointsD[i] = point;

                }
                LineGraphSeries<DataPoint> seriesD = new LineGraphSeries<>(dataPointsD);
                seriesD.setTitle("Park D");
                seriesD.setColor(Color.GREEN);

                //Adicionar linhas ao grafico
                GraphView graph = (GraphView) findViewById(R.id.graph);
                graph.addSeries(seriesD);
                graph.addSeries(seriesA);

                //Para aparecer a legenda de cada parque
                graph.getLegendRenderer().setVisible(true);

                graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/YYYY");

                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            return sdf.format(new Date((long) value));
                        }

                        return super.formatLabel(value, isValueX);
                    }
                });

                graph.getGridLabelRenderer().setHorizontalLabelsAngle(90);

                //Colocar grafico para dar para aumentar e fazer scrool e os valores limites máximos a aparecer
                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(120);

                graph.getViewport().setXAxisBoundsManual(true);
                graph.getViewport().setMinX(historicA.get(0).getDate().getTime());
                Calendar calendar = Calendar.getInstance();

                calendar.setTime(historicA.get(historicA.size() - 1).getDate());
                calendar.add(DATE, 5);
                Date newDate = calendar.getTime();

                graph.getViewport().setMaxX(newDate.getTime());

                graph.getViewport().setScalable(true);
                graph.getViewport().setScrollable(true);

                FirebaseDatabase.getInstance().getReference().child("DailyOccupationRate").removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static Intent getIntent(Context context) {
        return new Intent(context,RateChartActivity.class);
    }

    protected View childView() {
        return getLayoutInflater().inflate(R.layout.activity_rate_chart, null);
    }


}
