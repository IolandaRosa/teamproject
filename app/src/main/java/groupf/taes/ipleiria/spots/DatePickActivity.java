package groupf.taes.ipleiria.spots;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.sql.Date;
import java.text.SimpleDateFormat;

import modelo.InternetConnectionManager;

public class DatePickActivity extends /*AppCompatActivity*/ PerformanceButtonActivity {
    private EditText dateInit;
    private EditText dateFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_date_pick);

        this.dateInit = findViewById(R.id.editTextInitDate);
        this.dateFinal = findViewById(R.id.editTextFinalDate);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context,DatePickActivity.class);
    }

    public void onClick_btnDesplayGraph(View view) {
        String initialDate = dateInit.getText().toString();
        String finalDate = dateFinal.getText().toString();

        //ver se é vazio
        if (initialDate.trim().isEmpty() || finalDate.trim().isEmpty()) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.emptyFields);
            return;
        }

        //ver formato
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        java.util.Date initialD = new java.util.Date();
        java.util.Date finalD = new java.util.Date();
        try {
            dateFormat.setLenient(false);
            initialD=dateFormat.parse(initialDate);
            finalD=dateFormat.parse(finalDate);
        } catch (Exception e) {
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.invalidDateFormat);
            return;
        }

        //ver se data inicial>final
        if(initialD.compareTo(finalD)>=0){
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.invalidInitialDate);
            return;
        }

        //ver se data inicial <actual
        if(initialD.compareTo(new java.util.Date())>=0){
            InternetConnectionManager.INSTANCE.showErrorMessage(this, R.string.invalidInitialDateActual);
            return;
        }

        //mostrar grafico
        startActivity(RateChartActivity.getIntent(this).putExtra("init", initialDate).putExtra("final", finalDate));
    }

    protected View childView() {
        return getLayoutInflater().inflate(R.layout.activity_date_pick,null);
    }
}
