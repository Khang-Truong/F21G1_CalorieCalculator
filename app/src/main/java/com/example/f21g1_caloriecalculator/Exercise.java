package com.example.f21g1_caloriecalculator;

import java.util.List;

public class Exercise {

    String ExerciseName;
    Integer ExercisePic;

    public Exercise(String exerciseName, Integer exercisePic) {
        ExerciseName = exerciseName;
        ExercisePic = exercisePic;
    }

    public String getExerciseName() {
        return ExerciseName;
    }

    public void setExerciseName(String exerciseName) {
        ExerciseName = exerciseName;
    }

    public Integer getExercisePic() {
        return ExercisePic;
    }

    public void setExercisePic(Integer exercisePic) {
        ExercisePic = exercisePic;
    }
}
