package pk.bmicalc;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class ResultActivity extends AppCompatActivity {
    TextView tvResult, tvUnder, tvNormal, tvOver, tvObese;
    Button btnShare, btnSave, btnBack;
    SharedPreferences sp1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvResult = (TextView)findViewById(R.id.tvResult);
        tvUnder = (TextView)findViewById(R.id.tvUnder);
        tvNormal = (TextView)findViewById(R.id.tvNormal);
        tvOver = (TextView)findViewById(R.id.tvOver);
        tvObese = (TextView)findViewById(R.id.tvObese);
        btnSave = (Button)findViewById(R.id.btnSave);
        btnShare = (Button)findViewById(R.id.btnShare);
        sp1 = getSharedPreferences("MyP1", MODE_PRIVATE);
        btnBack = (Button)findViewById(R.id.btnBack);

        int orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
        setRequestedOrientation(orientation);

        float bmi = getIntent().getExtras().getFloat("bmi");
        final String bmi1 = "" + bmi;
        SharedPreferences.Editor editor = sp1.edit();
        editor.putString("bmi", bmi1);


        if(bmi<18.5){
            tvResult.setText("Your BMI is "+bmi+ " and You Are Underweight");
            tvUnder.setTextColor(Color.parseColor("#ff0000"));
            editor.putString("status","You Are UnderWeight.");
            editor.commit();


        }
        if(bmi>=18.5 && bmi<25){
            tvResult.setText("Your BMI is "+bmi+ " and You Are Normal");
            tvNormal.setTextColor(Color.parseColor("#ff0000"));
            editor.putString("status","You Are Normal.");
            editor.commit();
        }
        if(bmi>=25 && bmi<30){
            tvResult.setText("Your BMI is "+bmi+ " and You Are Overweight");
            tvOver.setTextColor(Color.parseColor("#ff0000"));
            editor.putString("status","You Are Overweight.");
            editor.commit();
        }
        if(bmi>=30){
            tvResult.setText("Your BMI is "+bmi+ " and You Are Obese");
            tvObese.setTextColor(Color.parseColor("#ff0000"));
            editor.putString("status","You Are Obese.");
            editor.commit();
        }

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg = "Name is "+sp1.getString("Name","")+"\n"+
                        "Age is "+sp1.getInt("Age",0)+"\n"+
                        "Phone number is "+sp1.getString("Phone","")+"\n"+
                        "Sex is "+sp1.getString("Sex","")+"\n"+
                        "BMI is "+sp1.getString("bmi","")+"\n"+
                        sp1.getString("status","");
                Toast.makeText(ResultActivity.this, msg, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, msg);
                startActivity(intent);
                Toast.makeText(ResultActivity.this, bmi1, Toast.LENGTH_SHORT).show();

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sp1.getString("Name","");
                int age = sp1.getInt("Age",0);
                String ph = sp1.getString("Phone","");
                String sex = sp1.getString("Sex","");
                String bmi = sp1.getString("bmi","");
                String status = sp1.getString("status","");
                Date currentTime = Calendar.getInstance().getTime();
                String time = "" + currentTime;
                Toast.makeText(ResultActivity.this, time, Toast.LENGTH_SHORT).show();
                MainActivity.db.addBMI(name,age,ph,sex,bmi,status,time);




            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = sp1.getString("Name","");
                Intent intent = new Intent(ResultActivity.this, CaclulatorActivity.class);
                intent.putExtra("n",name);
                startActivity(intent);
                finish();
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
