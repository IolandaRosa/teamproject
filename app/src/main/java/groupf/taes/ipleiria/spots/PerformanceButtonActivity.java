package groupf.taes.ipleiria.spots;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.widget.RelativeLayout;

public abstract class PerformanceButtonActivity extends AppCompatActivity {

    protected abstract View childView();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RelativeLayout baseLayout;
        ViewStub stub;
        baseLayout = (RelativeLayout)
                this.getLayoutInflater().inflate(R.layout.activity_performance_button_fragment, null);
        stub = (ViewStub) baseLayout.findViewById(R.id.base_content_stub);

        baseLayout.removeView(stub);
        baseLayout.addView(childView(), stub.getLayoutParams());

        super.setContentView(baseLayout);
    }

    public void onClick_btnComputePerformance(View view) {
        startActivity(AlghorithmPerformanceActivity.getIntent(PerformanceButtonActivity.this));
    }
}
