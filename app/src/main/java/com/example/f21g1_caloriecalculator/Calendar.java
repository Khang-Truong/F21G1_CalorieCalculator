package com.example.f21g1_caloriecalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.HashMap;

public class Calendar extends AppCompatActivity {

    CalendarView Calendar;
    EditText textViewExerciseRecord;
    HashMap<String, String> capitalCities;
    Button buttonSaveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);


        Calendar=findViewById(R.id.calendarView);
        textViewExerciseRecord=findViewById(R.id.textViewExerciseRecord);
        buttonSaveData=findViewById(R.id.buttonSaveData);
        capitalCities=new HashMap<String, String>();



        Calendar.setDate(System.currentTimeMillis(),false,true );

        Calendar.setOnDateChangeListener((@NonNull CalendarView calendarView, int i, int i1, int i2) ->{


            // if user has selected the date, he can continue adding data
            if(i!=0||i1!=0||i2!=0){
                buttonSaveData.setOnClickListener((View view) ->{
                    String key= String.valueOf(i)+String.valueOf(i1)+String.valueOf(i2);
                    String value= textViewExerciseRecord.getText().toString();
                    capitalCities.put(key,value);
                    Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show();
                });
            }




            //display the text in current state

            String key= String.valueOf(i)+String.valueOf(i1)+String.valueOf(i2);
            textViewExerciseRecord.setText(capitalCities.get(key));
        });
    }
}