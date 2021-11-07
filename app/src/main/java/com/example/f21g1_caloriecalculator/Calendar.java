package com.example.f21g1_caloriecalculator;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.Inet4Address;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Calendar extends AppCompatActivity {

    CalendarView CalendarView;
    Button buttonSaveData;
    Button buttonGoFunctionPage;
    TextView textViewCalBurned;
    TextView textViewTDE;



    int currentDay;
    int currentMonth;
    int currentYear;
    SharedPreferences sharedPreferences;
    DBHelper db;
    int userId;
    String name;
    String password;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name", null);
        password = sharedPreferences.getString("Password", null);
        userId = sharedPreferences.getInt("UserId", -1);
        db = new DBHelper(Calendar.this);

        CalendarView=findViewById(R.id.calendarView);
        textViewTDE=findViewById(R.id.textViewTDE);
        buttonSaveData=findViewById(R.id.buttonSaveData);
        buttonGoFunctionPage=findViewById(R.id.buttonGoFunctionPage);
        textViewCalBurned=findViewById(R.id.textViewCalBurned);







        CalendarView.setDate(System.currentTimeMillis(),false,true );




//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String numberOfData=sharedPreferences.getString("EditTextNow","");
//        textViewExerciseRecord.setText(numberOfData);




        // check the current day, month, Year in integer.
        LocalDate currentdate = LocalDate.now();
         currentDay = currentdate.getDayOfMonth();
         currentMonth = getMonth();
         currentYear = currentdate.getYear();




        //============== display==================
        //display in Edit Text Box
        String key= String.valueOf(currentYear)+String.valueOf(currentMonth)+String.valueOf(currentDay);
        try {
            db.insertTDEE(userId, key, db.getTDEE(userId));
        } catch (Exception e) {

        }

        String nowData;
        try {
            nowData = db.getTDEE(userId);
            Log.d("Calendar", nowData);
        } catch (Exception e) {
            nowData = null;
        }

        textViewTDE.setText(nowData);

        //display in CaloriesBurningBox
        String CaloriesBurnedAmount;
        try {
            CaloriesBurnedAmount= db.getExerciseCal(userId, key);
        } catch (Exception e) {
            CaloriesBurnedAmount = null;
        }

        textViewCalBurned.setText(CaloriesBurnedAmount);







        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Date",key);
        editor.commit();



        CalendarView.setOnDateChangeListener((@NonNull CalendarView calendarView, int i, int i1, int i2) ->{

            textViewTDE.setText("");
            
            // if click, set date to the chosen day
            currentDay=i2;
            currentMonth=i1+1;
            currentYear=i;



            //============== display==================
            //display in Edit Text Box
            String CurrentKey=String.valueOf(currentYear)+String.valueOf(currentMonth)+String.valueOf(currentDay);
            try {
                db.insertTDEE(userId, CurrentKey, db.getTDEE(userId));
            } catch (Exception e) {

            }
            String currentData;
            try {
                currentData = db.getTDEE(userId);
            } catch (Exception e) {
                currentData = null;
            }

            textViewTDE.setText(currentData);

            //display in CaloriesBurningBox
            String CaloriesBurnedAmount2;
            try {
                CaloriesBurnedAmount2= db.getExerciseCal(userId, CurrentKey);
            } catch (Exception e) {
                CaloriesBurnedAmount2 = null;
            }

            textViewCalBurned.setText(CaloriesBurnedAmount2);





            //sharePreference this date
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("Date",CurrentKey);
            editor1.commit();
        });


        buttonSaveData.setOnClickListener((View view) ->{

            String findingKey= String.valueOf(currentYear)+String.valueOf(currentMonth)+String.valueOf(currentDay);
            String value= textViewTDE.getText().toString();





            if (db.getTDEE(userId, findingKey) != null) {
                db.updateTDEE(userId, findingKey, value);
            } else {
                db.insertTDEE(userId, findingKey, value);
            }

//            userInputNote.put(findingKey,value);
            Toast.makeText(this, currentYear+" "+currentMonth+" "+currentDay, Toast.LENGTH_SHORT).show();
        });

        buttonGoFunctionPage.setOnClickListener((View view)-> {
                startActivity(new Intent(Calendar.this,UserActivity.class));
        });
    }


    protected void onPause() {
        super.onPause();

        TextView textViewTDE= findViewById(R.id.textViewTDE);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!textViewTDE.getText().toString().isEmpty())
        editor.putString("EditTextNow", textViewTDE.getText().toString());
        editor.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getMonth() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        return month;
    }

//    public String displayCaloriesInBurningBox(int userId, String date){
//        String Calories= db.getExerciseCal(userId,date);
//        return Calories;
//    }
}