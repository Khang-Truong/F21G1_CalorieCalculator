package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class RecommendationsPage extends AppCompatActivity {
    TextView textViewName, textViewAge, textViewGender, textViewHeight, textViewWeight, textViewBMR, textViewShowCal;
    Button buttonSave;
    RadioGroup radioGroup;
    DBHelper db;
    SharedPreferences sharedPreferences;
    String TDEE;
    double bmr, weightDouble, heightDouble, ageDouble, sedentary, lightActive, moderateActive, veryActive, extraActive;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations_page);

        textViewName = findViewById(R.id.textViewNameR);
        textViewAge = findViewById(R.id.textViewAgeR);
        textViewGender = findViewById(R.id.textViewGenderR);
        textViewHeight = findViewById(R.id.textViewHeightR);
        textViewWeight = findViewById(R.id.textViewWeightR);
        textViewBMR = findViewById(R.id.textViewBMRR);
        textViewShowCal = findViewById(R.id.textViewShowCal);
        buttonSave = findViewById(R.id.buttonSaveDatabase);
        radioGroup = findViewById(R.id.radioGroupUpdate);

        db = new DBHelper(RecommendationsPage.this);

        //call sharePreferences to get data from it
        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("Name", null);
        String password = sharedPreferences.getString("Password", null);
        int userId = sharedPreferences.getInt("UserId", -1);

        df=new DecimalFormat("#.##");

        try{
            //retrieve data from database by using data from sharePreferences as parameters
            Log.d("Detail", userId + "!");
            String[] myDetail = db.getDetail(name, password, userId);
            Log.d("Detail", myDetail[0]);
            String gender = myDetail[0];
            String age = myDetail[1];
            String height = myDetail[2];
            String weight = myDetail[3];

            //calculate BMR (The Basal Metabolic Rate is the amount of calories your body needs while resting)
            weightDouble=Double.parseDouble(weight);
            heightDouble=Double.parseDouble(height);
            ageDouble=Double.parseDouble(age);
            if(gender.equals("male")){
                //reference: https://www.checkyourhealth.org/eat-healthy/cal_calculator.php
                //Adult male: 66 + (6.3 x body weight in lbs.) + (12.9 x height in inches) - (6.8 x age in years) = BMR
                bmr=66+(6.3*weightDouble)+(12.9*heightDouble)-(6.8*ageDouble);
            }else{
                //Adult female: 655 + (4.3 x weight in lbs.) + (4.7 x height in inches) - (4.7 x age in years) = BMR
                bmr=655+(4.3*weightDouble)+(4.7*heightDouble)-(4.7*ageDouble);
            }

            bmr = Double.parseDouble(df.format(bmr));
            textViewName.setText(name);
            textViewAge.setText(age);
            textViewGender.setText(gender);
            textViewHeight.setText(height + " inches");
            textViewWeight.setText(weight + " lbs");
            textViewBMR.setText(String.format("%.2f calories", bmr));
        }catch (Exception e){
            e.printStackTrace();
        }

        radioGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            sedentary = Double.parseDouble(df.format(bmr*1.2));
            lightActive = Double.parseDouble(df.format(bmr*1.375));
            moderateActive = Double.parseDouble(df.format(bmr*1.55));
            veryActive = Double.parseDouble(df.format(bmr*1.725));
            extraActive = Double.parseDouble(df.format(bmr*1.9));

            String result = "Your recommended calories amount is: ";
            if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonSedentary) {
                textViewShowCal.setText(result + sedentary);
                TDEE = String.format("%.2f", sedentary);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton1to3) {
                textViewShowCal.setText(result + lightActive);
                TDEE = String.format("%.2f", lightActive);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton3to5) {
                textViewShowCal.setText(result + moderateActive);
                TDEE = String.format("%.2f", moderateActive);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton6to7) {
                textViewShowCal.setText(result + veryActive);
                TDEE = String.format("%.2f", veryActive);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonHard) {
                textViewShowCal.setText(result + extraActive);
                TDEE = String.format("%.2f", extraActive);
            }
        });

        buttonSave.setOnClickListener((View view) -> {
            Toast.makeText(RecommendationsPage.this, "Save to Database Successful!", Toast.LENGTH_SHORT).show();
            db.updateTDEE(TDEE, String.valueOf(userId)); //update TDEE in user table and calendar table
            startActivity(new Intent(RecommendationsPage.this,Calendar.class));
        });
    }
}