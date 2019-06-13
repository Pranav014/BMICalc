package pk.bmicalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CaclulatorActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    TextView tvHeading, tvLocation;
    Spinner spnFeet, spnInches;
    EditText etWeight;
    Button btnCalculate, btnView;
    GoogleApiClient gac;
    Location loc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caclulator);
        tvHeading = (TextView)findViewById(R.id.tvHeading);
        spnFeet = (Spinner) findViewById(R.id.spnFeet);
        spnInches = (Spinner) findViewById(R.id.spnInches);
        etWeight = (EditText)findViewById(R.id.etWeight);
        btnCalculate = (Button)findViewById(R.id.btnCalculate);
        btnView = (Button)findViewById(R.id.btnView);
        tvLocation = (TextView)findViewById(R.id.tvLocation);

        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(orientation);

        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this);
        builder.addApi(LocationServices.API);
        builder.addConnectionCallbacks(this);
        builder.addOnConnectionFailedListener(this);
        gac = builder.build();

        Intent i = getIntent();
        String msg = i.getStringExtra("n");
        tvHeading.setText("Welcome "+ msg);

        final Integer[] feetarray = new Integer[]{3,4,5,6};
        ArrayAdapter<Integer> adf = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, feetarray);
        spnFeet.setAdapter(adf);

        final Integer[] incharray = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11};
        ArrayAdapter<Integer> adi = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, incharray);
        spnInches.setAdapter(adi);

        btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int feet = Integer.parseInt(spnFeet.getSelectedItem().toString());
                int Inch = Integer.parseInt(spnInches.getSelectedItem().toString());
                String weight1 = etWeight.getText().toString();
                if(weight1.length() == 0){
                    etWeight.setError("Please state your weight");
                    etWeight.requestFocus();
                    return;
                }
                else {
                    int weight = Integer.parseInt(weight1);

                    float meter = (float) ((feet / 3.28) + Inch * 0.0254);

                    float bmi = weight / (meter * meter);

                    Intent i1 = new Intent(CaclulatorActivity.this, ResultActivity.class);
                    i1.putExtra("bmi", bmi);
                    startActivity(i1);
                    finish();
                }
            }
        });
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CaclulatorActivity.this, ViewActivity.class);
                startActivity(i);


            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to close this Application?");
        builder.setCancelable(false);//To ensure user selectas one of the dialogue button

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish(); //Closes Current Activity
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel(); //Closes the Dialog Box
            }
        });
        //Creating Dialogue Box
        AlertDialog alert = builder.create();
        alert.setTitle("Exit");
        alert.show();

    }

    @Override
    protected void onResume() {

        super.onResume();
        if(gac != null)
            gac.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(gac != null)
            gac.disconnect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.m1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.website){
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://www.calculator.net/bmi-calculator.html"));
            startActivity(i);
        }
        if(item.getItemId() == R.id.about){
            Toast.makeText(this, "This app is made by Pranav Khismatrao(PK)...", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        loc = LocationServices.FusedLocationApi.getLastLocation(gac);
        if(loc != null){
            double lat = loc.getLatitude();
            double lon = loc.getLongitude();
            Geocoder g = new Geocoder(CaclulatorActivity.this, Locale.ENGLISH);

            try {
                List<Address> address = g.getFromLocation(lat,lon,1);
                Address add = address.get(0);
                String msg = add.getCountryName()+" "+add.getAdminArea()+" "+add.getSubAdminArea()+" "+add.getLocality()+" "+add.getSubLocality();
                tvLocation.setText(msg);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else{
            tvLocation.setText("Please Enable GPS....");

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
