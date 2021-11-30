package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    ListView ListViewExercise;
    List<Exercise> ExerciseList= new ArrayList<>();
    DBHelper db;
    double totalCaloriesBurned=0;
    int userId;
    String name;
    String password;
    String date;
    Button buttonManuallyAddBurningRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);
        name = sharedPreferences.getString("Name", null);
        password = sharedPreferences.getString("Password", null);
        userId = sharedPreferences.getInt("UserId", -1);
        Log.d("Deeeebag",userId + "!");
        db = new DBHelper(ExerciseActivity.this);
        date=sharedPreferences.getString("Date",null);
       


        ListViewExercise=findViewById(R.id.ListViewExercise);
        buttonManuallyAddBurningRecord=findViewById(R.id.buttonManuallyAddBurningRecord);

        //add All Exercise Name and image in the list
        addExerciseData();


        //inflate data in ListView
        ExerciseListViewAdapter exerciseList= new ExerciseListViewAdapter(ExerciseList);
        ListViewExercise.setAdapter(exerciseList);




        //set click listener in each element in the listView
        ListViewExercise.setOnItemClickListener((AdapterView<?> adapterView, View view, int i, long l)-> {

            if(i==0){
                startActivity(new Intent(ExerciseActivity.this,Calendar.class));
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=Cw-Wt4xKD2s&ab_channel=PamelaReif")));

                // update the ExerciseCal in this record
                // (getExerciseCal(userId,date)==null) means that haven't had ExerciseCal in this record in Calendar Database. We need to update it.
                if(db.getExerciseCal(userId,date)==null){
                    db.updateExerciseCal(userId,date,"75");
                    Log.d("Try",db.getExerciseCal(userId,date)+"1");
                }else{
                    totalCaloriesBurned=Double.parseDouble(db.getExerciseCal(userId,date))+75;
                    db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                }
                Toast.makeText(this, "75 calories has been added into your daily calorie's burning", Toast.LENGTH_SHORT).show();
            }
            if(i==1){
                startActivity(new Intent(ExerciseActivity.this,Calendar.class));
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=QPKXw8XEQiA&ab_channel=PamelaReif")));

                if(db.getExerciseCal(userId,date)==null){
                    db.updateExerciseCal(userId,date,String.valueOf(60));
                }else{
                    totalCaloriesBurned=Double.parseDouble(db.getExerciseCal(userId,date))+60;
                    db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                }
                Toast.makeText(this, "60 calories has been added into your daily calorie's burning", Toast.LENGTH_SHORT).show();
            }
            if(i==2){
                startActivity(new Intent(ExerciseActivity.this,Calendar.class));
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=zr08J6wB53Y&ab_channel=PamelaReif")));
                if(db.getExerciseCal(userId,date)==null){
                    db.updateExerciseCal(userId,date,String.valueOf(50));
                }else{
                    totalCaloriesBurned=Double.parseDouble(db.getExerciseCal(userId,date))+50;
                    db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                }
                Toast.makeText(this, "50 calories has been added into your daily calorie's burning", Toast.LENGTH_SHORT).show();
            }
            if(i==3){
                startActivity(new Intent(ExerciseActivity.this,Calendar.class));
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=Z-8sNHcncpM&ab_channel=PamelaReif")));
                if(db.getExerciseCal(userId,date)==null){
                    db.updateExerciseCal(userId,date,String.valueOf(40));
                }else{
                    totalCaloriesBurned=Double.parseDouble(db.getExerciseCal(userId,date))+40;
                    db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                }
                Toast.makeText(this, "40 calories has been added into your daily calorie's burning", Toast.LENGTH_SHORT).show();
            }
            if(i==4){
                startActivity(new Intent(ExerciseActivity.this,Calendar.class));
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/watch?v=jC5xlKIQgR8&ab_channel=PamelaReif")));
                if(db.getExerciseCal(userId,date)==null){
                    db.updateExerciseCal(userId,date,String.valueOf(30));
                }else{
                    totalCaloriesBurned=Double.parseDouble(db.getExerciseCal(userId,date))+30;
                    db.updateExerciseCal(userId,date,String.format("%.2f",totalCaloriesBurned));
                }
                Toast.makeText(this, "30 calories has been added into your daily calorie's burning", Toast.LENGTH_SHORT).show();
            }

        });


        //button to connect ManuallyInputExerciseActvity page
        buttonManuallyAddBurningRecord.setOnClickListener((View view)-> {
            startActivity(new Intent(ExerciseActivity.this,ManuallyInputExerciseActvity.class));
        });





    }


    private void addExerciseData(){
        ExerciseList.add(new Exercise("15 Min Exercise (Burn 75 calories)",R.drawable.exercisein15min));
        ExerciseList.add(new Exercise("12 Min Exercise (Burn 60 calories)",R.drawable.exercisein12min));
        ExerciseList.add(new Exercise("10 Min Exercise (Burn 50 calories)",R.drawable.exercisein10min));
        ExerciseList.add(new Exercise("8 Min Exercise (Burn 40 calories)",R.drawable.exercisein8min));
        ExerciseList.add(new Exercise("6 Min Exercise (Burn 30 calories)",R.drawable.exercisein6min));


    }
}