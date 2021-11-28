package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class MealActivity extends AppCompatActivity {

    ListView listViewMeals;
    ListView listViewDisplayMeals;
    EditText editTextMeal;
    EditText editTextEstimateCalories;
    Button buttonAdd;
    Button buttonRemove;
    Button buttonSubmit;
    List<String[]> myMealsList;
    List<String[]> displayList;
    int selectedNum;

    SharedPreferences sharedPreferences;
    DBHelper db;
    int userId;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        // Get the select item default = -1
        selectedNum = -1;

        // Get user Info to put data into database
        myMealsList = new ArrayList<>();
        myMealsList.add(new String[] {"Meals", "Calories"});
        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        userId = sharedPreferences.getInt("UserId", -1);
        date = sharedPreferences.getString("Date", null);
        db = new DBHelper(MealActivity.this);

        listViewMeals = findViewById(R.id.ListtViewMeals);
        listViewDisplayMeals = findViewById(R.id.ListViewDisplayMeal);
        editTextMeal = findViewById(R.id.editTextMealName);
        editTextEstimateCalories = findViewById(R.id.editTextEstimateCalories);

        buttonAdd = findViewById(R.id.buttonAddMeal);
        buttonRemove = findViewById(R.id.buttonRemoveMeal);
        buttonSubmit = findViewById(R.id.buttonSubmitMeal);
        MealAdapter mealAdapter = new MealAdapter(myMealsList);
        listViewMeals.setAdapter(mealAdapter);


        // If click add, data will be add to the temp list
        buttonAdd.setOnClickListener((View view) -> {
            if (editTextMeal.getText().toString().isEmpty() || editTextEstimateCalories.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill meal name and estimated calories!", Toast.LENGTH_SHORT).show();
            } else {
                myMealsList.add(new String[] {editTextMeal.getText().toString(), editTextEstimateCalories.getText().toString()});
                Log.d("List", myMealsList.size() + "!");
                Toast.makeText(this, "Meal " + editTextMeal.getText().toString() + " add successful!", Toast.LENGTH_SHORT).show();
                editTextMeal.setText("");
                editTextEstimateCalories.setText("");
                MealAdapter myMealAdapter = new MealAdapter(myMealsList);
                listViewMeals.setAdapter(myMealAdapter);

            }
        });


        // If click submit, the temp list will be upload
        buttonSubmit.setOnClickListener((View view) -> {
            if (myMealsList.isEmpty())
                Toast.makeText(this,"Please input some meal data!", Toast.LENGTH_SHORT).show();
            else {
                for (String[] strings : myMealsList) {
                    try {
                        Integer.parseInt(strings[1]);
                        Log.d("Meal Insert", db.insertMealData(userId, date, strings[0], strings[1]) + "!");
                    } catch (Exception e) {
                        continue;
                    }

                }
                Toast.makeText(this, "The meal data is saved!", Toast.LENGTH_SHORT).show();
                myMealsList.clear();
                MealAdapter myMealAdapter = new MealAdapter(myMealsList);
                listViewMeals.setAdapter(myMealAdapter);
                startActivity(new Intent(MealActivity.this,Calendar.class));
            }

        });


        // Select the temp list to remove
        listViewMeals.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            selectedNum = position;
        } );

        // Remove the select item in temp list
        buttonRemove.setOnClickListener((View view) -> {
            if (selectedNum == -1) {
                Toast.makeText(this, "Please select a meal!", Toast.LENGTH_SHORT).show();
            } else {
                myMealsList.remove(selectedNum);
                selectedNum = -1;
                MealAdapter myMealAdapter = new MealAdapter(myMealsList);
                listViewMeals.setAdapter(myMealAdapter);
            }

        });

        try {
            displayList = new ArrayList<>();
            displayList = db.getMealDetail(userId, date);

            MealAdapter mealAdapter1 = new MealAdapter(displayList);
            listViewDisplayMeals.setAdapter(mealAdapter1);
        } catch (Exception e) {

        }




    }
}