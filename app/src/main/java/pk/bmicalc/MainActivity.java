package pk.bmicalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    EditText etName, etAge, etPhone;
    RadioButton rbMale, rbFemale;
    Button btnSubmit;
    SharedPreferences sp1;
    static DatabaseHandler db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = (EditText) findViewById(R.id.etName);
        etAge = (EditText) findViewById(R.id.etAge);
        etPhone = (EditText) findViewById(R.id.etPhone);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        sp1 = getSharedPreferences("MyP1", MODE_PRIVATE);
        db = new DatabaseHandler(this);
        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(orientation);




        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String age1 = etAge.getText().toString();
                String phone = etPhone.getText().toString();
                if(name.length() == 0){
                    etName.setError("Name is Empty");
                    etName.requestFocus();
                    return;
                }
                if(age1.length() == 0){
                    etAge.setError("You forgot to mention your age ");
                    etAge.requestFocus();
                    return;
                }
                if(phone.length() == 0){
                    etPhone.setError("Phone number is Empty");
                    etPhone.requestFocus();
                    return;
                }

                if(phone.length() == 10) {
                    int age = Integer.parseInt(age1);

                    SharedPreferences.Editor editor = sp1.edit();
                    editor.putString("Name", name);
                    editor.putInt("Age", age);
                    editor.putString("Phone", phone);
                    if (rbMale.isChecked()) {
                        editor.putString("Sex", "Male");
                    }
                    if (rbFemale.isChecked()) {
                        editor.putString("Sex", "Female");
                    }
                    editor.commit();
                    Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etAge.setText("");
                    etPhone.setText("");

                    Intent i = new Intent(MainActivity.this, CaclulatorActivity.class);
                    i.putExtra("n", name);
                    startActivity(i);
                    finish();
                }
                else{
                    etPhone.setError("Phone number must be 10 digit");
                    etPhone.setText("");
                    etPhone.requestFocus();
                    return;
                }



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
}
