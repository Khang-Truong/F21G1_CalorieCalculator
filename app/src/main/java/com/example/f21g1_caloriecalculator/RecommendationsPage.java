package com.example.f21g1_caloriecalculator;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.DecimalFormat;

public class RecommendationsPage extends AppCompatActivity {

    TextView recommendation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations_page);

        recommendation=findViewById(R.id.textViewRecommend);

        try{
            Bundle bundle=getIntent().getExtras();
            String name = bundle.getString("NAME");
            String gender = bundle.getString("GENDER");
            String age = bundle.getString("AGE");
            String height = bundle.getString("HEIGHT");
            String weight = bundle.getString("WEIGHT");

            //calculate BMR (The Basal Metabolic Rate is the amount of calories your body needs while resting)
            double bmr=0;
            double weightDouble=Double.parseDouble(weight);
            double heightDouble=Double.parseDouble(height);
            double ageDouble=Double.parseDouble(age);

            if(gender=="male"){
                //reference: https://www.checkyourhealth.org/eat-healthy/cal_calculator.php
//                Adult male: 66 + (6.3 x body weight in lbs.) + (12.9 x height in inches) - (6.8 x age in years) = BMR
                bmr=66+(6.3*weightDouble)+(12.9*heightDouble)-(6.8*ageDouble);
            }else{
//                Adult female: 655 + (4.3 x weight in lbs.) + (4.7 x height in inches) - (4.7 x age in years) = BMR
                bmr=655+(4.3*weightDouble)+(4.7*heightDouble)-(4.7*ageDouble);
            }

            //calculate calorie based on activity
            double sedentary=bmr*1.2;
            double lightActive=bmr*1.375;
            double moderateActive=bmr*1.55;
            double veryActive=bmr*1.725;
            double extraActive=bmr*1.9;

            DecimalFormat df=new DecimalFormat("#.##");

            String outputStr="User name: "+name+"\n"+
                    "Gender: "+gender+"\n"+
                    "Age: "+age+"\n"+
                    "Height (inches): "+height+"\n"+
                    "Weight (lbs): "+weight+"\n"+"\n"+
                    "Basal Metabolic Rate (BMR): "+bmr+"\n"+"\n"+
                    "Your total calorie needs, "+"\n"+
                    "If your are sedentary: "+df.format(sedentary)+"\n"+
                    "If your do exercise 1-3 days/week: "+df.format(lightActive)+"\n"+
                    "If you do exercise 3-5 days/week: "+df.format(moderateActive)+"\n"+
                    "If you do exercise 6-7 days/week: "+df.format(veryActive)+"\n"+
                    "If you do very hard exercise and physical job or 2x training: "+df.format(extraActive);
            recommendation.setText(outputStr);
            recommendation.setTextSize(Dimension.SP,20);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}