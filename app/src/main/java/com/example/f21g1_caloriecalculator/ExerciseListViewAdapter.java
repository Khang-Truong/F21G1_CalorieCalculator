package com.example.f21g1_caloriecalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ExerciseListViewAdapter extends BaseAdapter {

    List<Exercise>  exerciseList;

    public ExerciseListViewAdapter(List<Exercise> exerciseList) {
        this.exerciseList = exerciseList;
    }

    @Override
    public int getCount() {
        return exerciseList.size();
    }

    @Override
    public Exercise getItem(int i) {
        return exerciseList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if(convertView==null){

            LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
            convertView= layoutInflater.inflate(R.layout.layout_exercise, parent,false);
        }

        TextView textViewExerciseName= convertView.findViewById(R.id.textViewExerciseName);
        ImageView imageViewExercise=convertView.findViewById(R.id.imageViewExercise);

        textViewExerciseName.setText(exerciseList.get(i).getExerciseName());
        imageViewExercise.setImageResource(exerciseList.get(i).getExercisePic());


        return convertView;
    }
}
