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
    EditText textViewExerciseRecord;
    Button buttonSaveData;
    Button buttonGoFunctionPage;
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
        textViewExerciseRecord=findViewById(R.id.textViewExerciseRecord);
        buttonSaveData=findViewById(R.id.buttonSaveData);
        buttonGoFunctionPage=findViewById(R.id.buttonGoFunctionPage);





        CalendarView.setDate(System.currentTimeMillis(),false,true );




//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String numberOfData=sharedPreferences.getString("EditTextNow","");
//        textViewExerciseRecord.setText(numberOfData);




        // check the current day, month, Year in integer.
        LocalDate currentdate = LocalDate.now();
         currentDay = currentdate.getDayOfMonth();
         currentMonth = getMonth();
         currentYear = currentdate.getYear();


         //display the text in this date
        String key= String.valueOf(currentYear)+String.valueOf(currentMonth)+String.valueOf(currentDay);
//        textViewExerciseRecord.setText(userInputNote.get(key));
        String nowData;

        try {

            nowData = db.getConsumedCal(userId, key);
            Log.d("Calendar", nowData);
        } catch (Exception e) {
            nowData = null;
        }
        if (nowData != null)
            textViewExerciseRecord.setText(nowData);



        CalendarView.setOnDateChangeListener((@NonNull CalendarView calendarView, int i, int i1, int i2) ->{

            textViewExerciseRecord.setText("");
            
            // if click, set date to the chosen day
            currentDay=i2;
            currentMonth=i1+1;
            currentYear=i;




            String CurrentKey=String.valueOf(currentYear)+String.valueOf(currentMonth)+String.valueOf(currentDay);
            String currentData;

            try {
                currentData = db.getConsumedCal(userId, CurrentKey);
            } catch (Exception e) {
                currentData = null;
            }


            if (currentData != null)
                textViewExerciseRecord.setText(currentData);
        });


        buttonSaveData.setOnClickListener((View view) ->{

            String findingKey= String.valueOf(currentYear)+String.valueOf(currentMonth)+String.valueOf(currentDay);
            String value= textViewExerciseRecord.getText().toString();





            if (db.getConsumedCal(userId, findingKey) != null) {
                db.updateConsumedCal(userId, findingKey, value);
            } else {
                db.insertConsumedCal(userId, findingKey, value);
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

        EditText textViewExerciseRecord= findViewById(R.id.textViewExerciseRecord);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(!textViewExerciseRecord.getText().toString().isEmpty())
        editor.putString("EditTextNow", textViewExerciseRecord.getText().toString());
        editor.commit();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getMonth() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        return month;
    }
}