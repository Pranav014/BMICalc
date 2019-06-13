package pk.bmicalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Admin on 12/23/2018.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    SQLiteDatabase db;
    Context context;

    public DatabaseHandler(Context context){
        super(context, "bmihistoryDB",null,1);
        this.context = context;
        db = this.getWritableDatabase();

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql ="create table bmihistory (name text,age INTEGER, phone VARCHAR(10) , sex text, bmi VARCHAR(10),status text, time VARCHAR[50])";
        db.execSQL(sql);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public  void addBMI(String name, int age, String phone , String sex, String bmi,String status, String time){
        ContentValues cv = new ContentValues();
        cv.put("name",name);
        cv.put("age",age);
        cv.put("phone",phone);
        cv.put("sex",sex);
        cv.put("bmi",bmi);
        cv.put("status",status);
        cv.put("time",time);
        long rid = db.insert("bmihistory",null,cv);
        if(rid<0){
            Toast.makeText(context, "Insert Issue", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context, "Rcord added", Toast.LENGTH_SHORT).show();
        }

    }
    public String gethistory(){
        Cursor cursor = db.query("bmihistory",null,null,null,null,null,null);
        StringBuffer sb = new StringBuffer();
        if(cursor.getCount()>0) {
            int namecolumn = cursor.getColumnIndex("name");
            int ageColumn = cursor.getColumnIndex("age");
            int PhColumn = cursor.getColumnIndex("phone");
            int sexColumn = cursor.getColumnIndex("sex");
            int bmiColumn = cursor.getColumnIndex("bmi");
            int statusColumn = cursor.getColumnIndex("status");
            int timeColumn = cursor.getColumnIndex("time");
            cursor.moveToFirst();

            do {
                String name = cursor.getString(namecolumn);
                String age = cursor.getString(ageColumn);
                String ph = cursor.getString(PhColumn);
                String sex = cursor.getString(sexColumn);
                String bmi = cursor.getString(bmiColumn);
                String status = cursor.getString(statusColumn);
                String time = cursor.getString(timeColumn);

                sb.append("Name:"+name + " \nAge: " + age + " \nPhone: " + ph + " \nSex: " + sex + " \nBMI: " + bmi + " \nResult: " + status + " \nTimestamp: " + time + "\n\n");


            } while (cursor.moveToNext());

        }
        return sb.toString();





    }
}
