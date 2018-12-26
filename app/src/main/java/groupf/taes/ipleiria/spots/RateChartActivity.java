package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class RateChartActivity extends AppCompatActivity {

    private String initalDate;
    private String finalDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_chart);

        this.initalDate = this.getIntent().getStringExtra("init");
        this.finalDate = this.getIntent().getStringExtra("final");

        TextView view18 = findViewById(R.id.textView18);
        view18.setText(initalDate);
        TextView view19 = findViewById(R.id.textView19);
        view19.setText(finalDate);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context,RateChartActivity.class);
    }
}
