package com.example.f21g1_caloriecalculator;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class RecommendationsPage extends AppCompatActivity {

    TextView textViewName;
    TextView textViewAge;
    TextView textViewGender;
    TextView textViewHeight;
    TextView textViewWeight;
    TextView textViewBMR;
    TextView textViewShowCal;
    Button buttonSubmitExercise;
    DBHelper db;
    SharedPreferences sharedPreferences;
    RadioGroup radioGroup;
    String TDEE;


    double sedentary;
    double lightActive;
    double moderateActive;
    double veryActive;
    double extraActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations_page);

        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        String name = sharedPreferences.getString("Name", null);
        String password = sharedPreferences.getString("Password", null);
        int userId = sharedPreferences.getInt("UserId", -1);
        textViewName = findViewById(R.id.textViewNameR);
        textViewAge = findViewById(R.id.textViewAgeR);
        textViewGender = findViewById(R.id.textViewGenderR);
        textViewHeight = findViewById(R.id.textViewHeightR);
        textViewWeight = findViewById(R.id.textViewWeightR);
        textViewBMR = findViewById(R.id.textViewBMRR);
        textViewShowCal = findViewById(R.id.textViewShowCal);
        buttonSubmitExercise = findViewById(R.id.buttonSubmitExercise);
        radioGroup = findViewById(R.id.radioGroup);

        db = new DBHelper(RecommendationsPage.this);

        try{
//            Bundle bundle=getIntent().getExtras();
//            String name = bundle.getString("NAME");
//            String gender = bundle.getString("GENDER");
//            String age = bundle.getString("AGE");
//            String height = bundle.getString("HEIGHT");
//            String weight = bundle.getString("WEIGHT");

            //calculate BMR (The Basal Metabolic Rate is the amount of calories your body needs while resting)
            Log.d("Detail", userId + "!");
            String[] myDetail = db.getDetail(name, password, userId);
            Log.d("Detail", myDetail[0]);
            String gender = myDetail[0];
            String age = myDetail[1];
            String height = myDetail[2];
            String weight = myDetail[3];

            double bmr=0;
            double weightDouble=Double.parseDouble(weight);
            double heightDouble=Double.parseDouble(height);
            double ageDouble=Double.parseDouble(age);

            if(gender.equals("male")){
                //reference: https://www.checkyourhealth.org/eat-healthy/cal_calculator.php
//                Adult male: 66 + (6.3 x body weight in lbs.) + (12.9 x height in inches) - (6.8 x age in years) = BMR
                bmr=66+(6.3*weightDouble)+(12.9*heightDouble)-(6.8*ageDouble);
            }else{
//                Adult female: 655 + (4.3 x weight in lbs.) + (4.7 x height in inches) - (4.7 x age in years) = BMR
                bmr=655+(4.3*weightDouble)+(4.7*heightDouble)-(4.7*ageDouble);
            }
            DecimalFormat df=new DecimalFormat("#.##");
            sedentary=bmr*1.2;
            lightActive=bmr*1.375;
            moderateActive=bmr*1.55;
            veryActive=bmr*1.725;
            extraActive=bmr*1.9;

            sedentary = Double.parseDouble(df.format(sedentary));
            lightActive = Double.parseDouble(df.format(lightActive));
            moderateActive = Double.parseDouble(df.format(moderateActive));
            veryActive = Double.parseDouble(df.format(veryActive));
            extraActive = Double.parseDouble(df.format(extraActive));





//            String outputStr="User name: "+name+"\n"+
//                    "Gender: "+gender+"\n"+
//                    "Age: "+age+"\n"+
//                    "Height (inches): "+height+"\n"+
//                    "Weight (lbs): "+weight+"\n"+"\n"+
//                    "Basal Metabolic Rate (BMR): "+bmr+"\n"+"\n"+
//                    "Your total calorie needs, "+"\n"+
//                    "If your are sedentary: "+df.format(sedentary)+"\n"+
//                    "If your do exercise 1-3 days/week: "+df.format(lightActive)+"\n"+
//                    "If you do exercise 3-5 days/week: "+df.format(moderateActive)+"\n"+
//                    "If you do exercise 6-7 days/week: "+df.format(veryActive)+"\n"+
//                    "If you do very hard exercise and physical job or 2x training: "+df.format(extraActive);
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

        buttonSubmitExercise.setOnClickListener((View view) -> {
            Toast.makeText(RecommendationsPage.this, "Save to Database Successful!", Toast.LENGTH_SHORT).show();
            db.updateTDEE(TDEE, name);
        });

    }
}