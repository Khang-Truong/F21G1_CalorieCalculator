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
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Calendar extends AppCompatActivity {

    CalendarView CalendarView;
    Button buttonGoFunctionPage;
    TextView textViewCalBurned;
    TextView textViewTDE;
    TextView textViewCalIntake;
    TextView textViewSurplus;


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

        CalendarView = findViewById(R.id.calendarView);
        textViewTDE = findViewById(R.id.textViewTDE);
        buttonGoFunctionPage = findViewById(R.id.buttonGoFunctionPage);
        textViewCalBurned = findViewById(R.id.textViewCalBurned);
        textViewCalIntake = findViewById(R.id.textViewCalIntake);
        textViewSurplus = findViewById(R.id.textViewSurplus);


        double totalTDEE ;
        double totalCaloriesBurned ;
        double totalCaloriesIntake ;
        double caloriesSurplus;


        CalendarView.setDate(System.currentTimeMillis(), false, true);


        // check the current day, month, Year in integer.
        LocalDate currentdate = LocalDate.now();
        currentDay = currentdate.getDayOfMonth();
        currentMonth = getMonth();
        currentYear = currentdate.getYear();


        // Firstly, Get the TDEE from user table and insert into calendar table with userID and Key(Date);
        String key = String.valueOf(currentYear) + String.valueOf(currentMonth) + String.valueOf(currentDay); //CaloriesBurn's key
        if (db.getTDEE(userId, key) == null) {
            try {
                db.insertTDEE(userId, key, db.getTDEE(userId));
            } catch (Exception e) {
                Log.d("IO", e.getMessage());
            }
        }

        //============== display part==================
        // display TDEE from users table
        String nowData;
        try {
            nowData = db.getTDEE(userId);
            Log.d("Calendar", nowData);
        } catch (Exception e) {
            nowData = null;
        }
        textViewTDE.setText(nowData);

        //display Calories Burned from calendar table
        String CaloriesBurnedAmount;
        try {
            CaloriesBurnedAmount = db.getExerciseCal(userId, key);
        } catch (Exception e) {
            CaloriesBurnedAmount = null;
        }

        textViewCalBurned.setText(CaloriesBurnedAmount);


        //display Calories Intake from mealdata table;
        List<String> CaloriesIntakeAmount;
        try {
            CaloriesIntakeAmount = db.getMealData(userId, key);
            int calSum = 0;
            for (String s : CaloriesIntakeAmount) {
                calSum += Integer.parseInt(s);

            }
            textViewCalIntake.setText(String.valueOf(calSum));

        } catch (Exception e) {
            CaloriesIntakeAmount = null;
        }


        //========= Calculate surplus and display========

        if (textViewTDE.getText().toString().isEmpty()) {
            totalTDEE = 0;
        } else {
            totalTDEE = Double.parseDouble(textViewTDE.getText().toString());
        }
        if (textViewCalIntake.getText().toString().isEmpty()) {
            totalCaloriesIntake = 0;
        } else {
            totalCaloriesIntake = Double.parseDouble(textViewCalIntake.getText().toString());
        }
        if (textViewCalBurned.getText().toString().isEmpty()) {
            totalCaloriesBurned = 0;
        } else {
            totalCaloriesBurned = Double.parseDouble(textViewCalBurned.getText().toString());
        }


        caloriesSurplus = totalTDEE - (totalCaloriesIntake - totalCaloriesBurned);
        textViewSurplus.setText(String.format("%.2f",caloriesSurplus));
        //========= end Calculate surplus and display========

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Date", key);
        editor.commit();


        //*************************************Calendar change listener******************************************

        CalendarView.setOnDateChangeListener((@NonNull CalendarView calendarView, int i, int i1, int i2) -> {




            textViewCalIntake.setText("");
            textViewTDE.setText("");

            // if click, set date to the chosen day
            currentDay = i2;
            currentMonth = i1 + 1;
            currentYear = i;


            // need to have a new key(date) when click a specific day.
            // Than put the TDEE from user table to calender table
            String CurrentKey = String.valueOf(currentYear) + String.valueOf(currentMonth) + String.valueOf(currentDay);
            Log.i("Currentkey", CurrentKey);
            if (db.getTDEE(userId, CurrentKey) == null) {
                try {
                    db.insertTDEE(userId, CurrentKey, db.getTDEE(userId));
                } catch (Exception e) {

                }
            }

            //============== display==================
            //display TDEE from users table
            String currentData;
            try {
                currentData = db.getTDEE(userId);
            } catch (Exception e) {
                currentData = null;
            }

            textViewTDE.setText(currentData);

            //display Calories Burned from calendar table
            String CaloriesBurnedAmount2;
            try {
                CaloriesBurnedAmount2 = db.getExerciseCal(userId, CurrentKey);
            } catch (Exception e) {
                CaloriesBurnedAmount2 = null;
            }

            textViewCalBurned.setText(CaloriesBurnedAmount2);


            //display Calories Intake from mealdata table;
            List<String> CaloriesIntakeAmount2;
            try {
                CaloriesIntakeAmount2 = db.getMealData(userId, CurrentKey);
                int calSum = 0;
                Log.i("Cal", "0");
                for (String s : CaloriesIntakeAmount2) {
                    calSum += Integer.parseInt(s);
                    Log.i("Cal", i + "times");
                }
                Log.i("Cal", calSum + "!");
                textViewCalIntake.setText(String.valueOf(calSum));
            } catch (Exception e) {
                CaloriesIntakeAmount2 = null;
            }


            //========= Calculate surplus and display========


            double totalTDEEInDateChangeListener ;
            double totalCaloriesBurnedInDateChangeListener ;
            double totalCaloriesIntakeInDateChangeListener ;
            double caloriesSurplusInDateChangeListener;

            if (textViewTDE.getText().toString().isEmpty()) {

                totalTDEEInDateChangeListener = 0;
            } else {
                totalTDEEInDateChangeListener = Double.parseDouble(textViewTDE.getText().toString());
            }
            if (textViewCalIntake.getText().toString().isEmpty()) {
                totalCaloriesIntakeInDateChangeListener = 0;
            } else {
                totalCaloriesIntakeInDateChangeListener = Double.parseDouble(textViewCalIntake.getText().toString());
            }
            if (textViewCalBurned.getText().toString().isEmpty()) {
                totalCaloriesBurnedInDateChangeListener = 0;
            } else {
                totalCaloriesBurnedInDateChangeListener = Double.parseDouble(textViewCalBurned.getText().toString());
            }



            caloriesSurplusInDateChangeListener = totalTDEEInDateChangeListener - (totalCaloriesIntakeInDateChangeListener - totalCaloriesBurnedInDateChangeListener);
            textViewSurplus.setText(String.format("%.2f",caloriesSurplusInDateChangeListener));

            //========= End of Calculating surplus and display========

            //sharePreference again, override the old "Date" in share preferences
            SharedPreferences.Editor editor1 = sharedPreferences.edit();
            editor1.putString("Date", CurrentKey);
            editor1.commit();
        });


        buttonGoFunctionPage.setOnClickListener((View view) -> {
            startActivity(new Intent(Calendar.this, UserActivity.class));
        });
    }


    // to make the month into integer number;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getMonth() {
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        return month;
    }
}