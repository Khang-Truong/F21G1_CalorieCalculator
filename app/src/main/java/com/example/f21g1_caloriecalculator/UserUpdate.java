package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class UserUpdate extends AppCompatActivity {

    TextView textViewHello;
    TextView textViewShowCalU;
    EditText editTextAgeU;
    EditText editTextHeightU;
    EditText editTextWeightU;
    Button buttonUpdateExercise;
    DBHelper db;
    SharedPreferences sharedPreferences;
    RadioGroup radioGroup;
    String TDEE;
    String gender;
    String age;
    String height;
    String weight;
    double bmr;
    double weightDouble;
    double heightDouble;
    double ageDouble;




    double sedentary;
    double lightActive;
    double moderateActive;
    double veryActive;
    double extraActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_upadate);

        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("Name", null);
        String password = sharedPreferences.getString("Password", null);
        int userId = sharedPreferences.getInt("UserId", -1);
        textViewHello = findViewById(R.id.textViewUserHello);
        textViewShowCalU = findViewById(R.id.textViewShowCalU);
        editTextAgeU = findViewById(R.id.editTextAgeU);
        editTextHeightU = findViewById(R.id.editTextHeightU);
        editTextWeightU = findViewById(R.id.editTextWeightU);
        buttonUpdateExercise = findViewById(R.id.buttonUpdateExercise);
        radioGroup = findViewById(R.id.radioGroupUpdate);

        db = new DBHelper(UserUpdate.this);

        String[] myDetail = db.getDetail(name, password, userId);

        gender = myDetail[0];
        age = myDetail[1];
        height = myDetail[2];
        weight = myDetail[3];

        bmr = 0;
        weightDouble = Double.parseDouble(weight);
        heightDouble = Double.parseDouble(height);
        ageDouble = Double.parseDouble(age);
        TDEE = db.getTDEE(userId);

        if(gender.equals("male")){
            //reference: https://www.checkyourhealth.org/eat-healthy/cal_calculator.php
//                Adult male: 66 + (6.3 x body weight in lbs.) + (12.9 x height in inches) - (6.8 x age in years) = BMR
            bmr=66+(6.3*weightDouble)+(12.9*heightDouble)-(6.8*ageDouble);
        }else{
//                Adult female: 655 + (4.3 x weight in lbs.) + (4.7 x height in inches) - (4.7 x age in years) = BMR
            bmr=655+(4.3*weightDouble)+(4.7*heightDouble)-(4.7*ageDouble);
        }

        DecimalFormat df=new DecimalFormat("#.##");
        sedentary = bmr*1.2;
        lightActive = bmr*1.375;
        moderateActive = bmr*1.55;
        veryActive = bmr*1.725;
        extraActive = bmr*1.9;

        sedentary = Double.parseDouble(df.format(sedentary));
        lightActive = Double.parseDouble(df.format(lightActive));
        moderateActive = Double.parseDouble(df.format(moderateActive));
        veryActive = Double.parseDouble(df.format(veryActive));
        extraActive = Double.parseDouble(df.format(extraActive));

        textViewShowCalU.setText("Your original TDEE is: "  + TDEE);

        // Get the TDEE update
        radioGroup.setOnCheckedChangeListener((RadioGroup group, int checkedId) -> {
            String result = "Your recommended calories amount is: ";
            if (!editTextHeightU.getText().toString().isEmpty()) {
                height = editTextHeightU.getText().toString();
                heightDouble = Double.parseDouble(height);
            }
            if (!editTextWeightU.getText().toString().isEmpty()) {
                weight = editTextWeightU.getText().toString();
                weightDouble = Double.parseDouble(weight);
            }
            if (!editTextAgeU.getText().toString().isEmpty()) {
                age = editTextAgeU.getText().toString();
                ageDouble = Double.parseDouble(age);
            }



            bmr = calculateBMR(heightDouble, weightDouble, ageDouble, gender);
            if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonSedentary) {
                bmr = bmr*1.2;
                textViewShowCalU.setText(result + df.format(bmr));
                TDEE = String.format("%.2f", bmr);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton1to3) {
                bmr = bmr*1.375;
                textViewShowCalU.setText(result + df.format(bmr));
                TDEE = String.format("%.2f", bmr);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton3to5) {
                bmr = bmr*1.55;
                textViewShowCalU.setText(result + df.format(bmr));
                TDEE = String.format("%.2f", bmr);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton6to7) {
                bmr = bmr*1.725;
                textViewShowCalU.setText(result + df.format(bmr));
                TDEE = String.format("%.2f", bmr);
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButtonHard) {
                bmr = bmr*1.9;
                textViewShowCalU.setText(result + df.format(bmr));
                TDEE = String.format("%.2f", bmr);
            }

        });


        // Update the condition to database
        buttonUpdateExercise.setOnClickListener((View view) -> {
            Toast.makeText(UserUpdate.this, "Save to Database Successful!", Toast.LENGTH_SHORT).show();
            if (!editTextHeightU.getText().toString().isEmpty()) {
                height = editTextHeightU.getText().toString();
                heightDouble = Double.parseDouble(height);
            }
            if (!editTextWeightU.getText().toString().isEmpty()) {
                weight = editTextWeightU.getText().toString();
                weightDouble = Double.parseDouble(weight);
            }
            if (!editTextAgeU.getText().toString().isEmpty()) {
                age = editTextAgeU.getText().toString();
                ageDouble = Double.parseDouble(age);
            }
            db.updateTDEE(TDEE, String.valueOf(userId));
            db.updateAge(userId, age);
            db.updateHeight(userId, height);
            db.updateWeight(userId, weight);
            startActivity(new Intent(UserUpdate.this, Calendar.class));
        });

    }

    private double calculateBMR(double height, double weight, double age, String gender) {
        double bmr = 0;
        if (gender.equals("male"))
            bmr = 66 + (6.3*weight)+(12.9*height)-(6.8*age);
        else
            bmr = 655 + (4.3*weight)+(4.7*height)-(4.7*age);

        return bmr;
    }
}