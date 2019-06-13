package pk.bmicalc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class ViewActivity extends AppCompatActivity {
    TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        tvHistory = (TextView)findViewById(R.id.tvHistory);

        String data = MainActivity.db.gethistory();

        if(data.length() == 0)
        {
            tvHistory.setText("No records");
        }
        else
        {
            tvHistory.setText(data);
        }
        tvHistory.setMovementMethod(new ScrollingMovementMethod());



    }
}
