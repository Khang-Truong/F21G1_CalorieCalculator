package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    Button buttonDetail;
    Button buttonUpdate;
    Button buttonMeal;
    Button buttonExercise;
    Button buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        buttonDetail = findViewById(R.id.buttonDetail);
        // To detail
        buttonDetail.setOnClickListener((View view) -> {
            startActivity(new Intent(UserActivity.this, RecommendationsPage.class));
        });

        buttonLogout = findViewById(R.id.buttonLogout);
        buttonLogout.setOnClickListener((View view) -> {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(UserActivity.this, MainActivity.class));
        });

        // To Exercise
        buttonExercise=findViewById(R.id.buttonExercise);
        buttonExercise.setOnClickListener((View view)-> {

            startActivity(new Intent(UserActivity.this,ExerciseActivity.class));

        });

        // To meal
        buttonMeal = findViewById(R.id.buttonMeal);
        buttonMeal.setOnClickListener((View view) -> {

            startActivity(new Intent(UserActivity.this, MealActivity.class));

        });

        // To update
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener((View view) -> {
            startActivity(new Intent(UserActivity.this, UserUpdate.class));
        });


    }
}