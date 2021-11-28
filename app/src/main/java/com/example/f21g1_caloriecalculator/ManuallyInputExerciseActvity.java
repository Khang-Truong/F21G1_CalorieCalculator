package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class ManuallyInputExerciseActvity extends AppCompatActivity {

    TextView textViewChooseExercise;
    Spinner spinnerExercise;
    EditText editTextTextPersonName;
    TextView textViewDisplayExerciseResult;
    Button buttonGetResult;
    Button buttonSaveData;


    SharedPreferences sharedPreferences;
    DBHelper db;
    int userId;
    String name;
    String password;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manually_input_exercise_actvity);


        textViewChooseExercise=findViewById(R.id.textViewChooseExercise);
        spinnerExercise=findViewById(R.id.spinnerExercise);
        editTextTextPersonName=findViewById(R.id.editTextTextPersonName);
        textViewDisplayExerciseResult=findViewById(R.id.textViewDIsplayExerciseResult);
        buttonGetResult=findViewById(R.id.buttonGetResult);
        buttonSaveData=findViewById(R.id.buttonSaveData);


        // input all the SharedPreferences data here
        sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name", null);
        password = sharedPreferences.getString("Password", null);
        userId = sharedPreferences.getInt("UserId", -1);
        date=sharedPreferences.getString("Date",null);

        db = new DBHelper(ManuallyInputExerciseActvity.this);




        /* ===== display the total calories burned, it depends on the exercising type in the spinner  ===== */
        buttonGetResult.setOnClickListener((View view)-> {
            double totalCaloriesBurned=0;
            if(editTextTextPersonName.getText().toString().isEmpty()){
                Toast.makeText(this, "Please input how much time you exercised", Toast.LENGTH_SHORT).show();
            }else {

                // if user choose walk, jog, bike
                if(spinnerExercise.getSelectedItemPosition()==0){
                    totalCaloriesBurned= 2.84*Double.parseDouble(editTextTextPersonName.getText().toString());
                }else if (spinnerExercise.getSelectedItemPosition()==1) {
                    totalCaloriesBurned = 8.616 * Double.parseDouble(editTextTextPersonName.getText().toString());
                }else if (spinnerExercise.getSelectedItemPosition()==2) {
                    totalCaloriesBurned = 8.9 * Double.parseDouble(editTextTextPersonName.getText().toString());
                }
                textViewDisplayExerciseResult.setText("You have burned "+String.format("%.2f",totalCaloriesBurned)+" calories");
            }

        });



        /* ===== update the ExerciseCal in the database calendar ===== */
        buttonSaveData.setOnClickListener((View view)-> {
            double totalCaloriesBurned=0;
            if(editTextTextPersonName.getText().toString().isEmpty()){
                Toast.makeText(this, "Please input how much time you exercised", Toast.LENGTH_SHORT).show();
            }else {

                // if user choose walk, jog, bike

                if(spinnerExercise.getSelectedItemPosition()==0){
                    if(db.getExerciseCal(userId,date)==null){
                        totalCaloriesBurned= 2.84*Double.parseDouble(editTextTextPersonName.getText().toString());
                        db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                    }else{
                        totalCaloriesBurned=Double.parseDouble(db.getExerciseCal(userId,date))+2.84*Double.parseDouble(editTextTextPersonName.getText().toString());
                        db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                    }
                    Toast.makeText(this, String.format("%.2f",2.84*Double.parseDouble(editTextTextPersonName.getText().toString()))+" calories have been added", Toast.LENGTH_SHORT).show();

                }else if (spinnerExercise.getSelectedItemPosition()==1) {
                    if (db.getExerciseCal(userId, date) == null) {
                        totalCaloriesBurned = 8.616 * Double.parseDouble(editTextTextPersonName.getText().toString());
                        db.updateExerciseCal(userId, date, String.format("%.2f", totalCaloriesBurned));
                    } else {
                        totalCaloriesBurned = Double.parseDouble(db.getExerciseCal(userId, date)) +  8.616 * Double.parseDouble(editTextTextPersonName.getText().toString());
                        db.updateExerciseCal(userId, date, String.format("%.2f", totalCaloriesBurned));
                    }
                    Toast.makeText(this, String.format("%.2f",8.616*Double.parseDouble(editTextTextPersonName.getText().toString()))+" calories have been added", Toast.LENGTH_SHORT).show();

                }else if (spinnerExercise.getSelectedItemPosition()==2) {
                    if (db.getExerciseCal(userId, date) == null) {
                        totalCaloriesBurned = 8.9 * Double.parseDouble(editTextTextPersonName.getText().toString());
                        db.updateExerciseCal(userId, date, String.format("%.2f", totalCaloriesBurned));
                    } else {
                        totalCaloriesBurned = Double.parseDouble(db.getExerciseCal(userId, date)) +  8.9 * Double.parseDouble(editTextTextPersonName.getText().toString());
                        db.updateExerciseCal(userId, date, String.format("%.2f", totalCaloriesBurned));
                    }
                    Toast.makeText(this, String.format("%.2f",8.9*Double.parseDouble(editTextTextPersonName.getText().toString()))+" calories have been added", Toast.LENGTH_SHORT).show();
                }

                startActivity(new Intent(ManuallyInputExerciseActvity.this,Calendar.class));
            }
        });








    }
}