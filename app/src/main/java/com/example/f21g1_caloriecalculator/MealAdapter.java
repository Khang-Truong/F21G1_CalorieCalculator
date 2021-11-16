package com.example.f21g1_caloriecalculator;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class MealAdapter extends BaseAdapter {

    List<String[]> myMealsList;

    public MealAdapter(List<String[]> myMealsList) {
        this.myMealsList = myMealsList;
    }

    @Override
    public int getCount() {
        return myMealsList.size();
    }

    @Override
    public String[] getItem(int i) {
        return myMealsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            convertView = layoutInflater.inflate(R.layout.layout_meal, parent, false);
        }

        TextView textViewMealEnter = convertView.findViewById(R.id.textViewMealEnter);
        TextView textViewCalEnter = convertView.findViewById(R.id.textViewCaloriesEnter);

        textViewMealEnter.setText(myMealsList.get(i)[0]);
        textViewCalEnter.setText(myMealsList.get(i)[1]);

        Log.d("Meal", myMealsList.get(i)[0]);

        return convertView;

    }
}
