package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG="SignUp Demo";
    EditText editName2,editPass2, editRePass, editAge, editHeight, editWeight;
    RadioGroup radioGender;
    Button btnSignup2;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editName2=findViewById(R.id.editTextName2);
        editPass2=findViewById(R.id.editTextPass2);
        editRePass=findViewById(R.id.editTextRePass);
        radioGender=findViewById(R.id.radioGroupGender);
        editAge=findViewById(R.id.editTextAge);
        editHeight=findViewById(R.id.editTextHeight);
        editWeight=findViewById(R.id.editTextWeight);
        btnSignup2=findViewById(R.id.btnSignUp2);
        DB=new DBHelper(SignUpActivity.this);

        btnSignup2.setOnClickListener((View v)->{
            try{
                String name2=editName2.getText().toString();
                String pas2=editPass2.getText().toString();
                String rePas=editRePass.getText().toString();
                String gender;
                String age=editAge.getText().toString();
                String height=editHeight.getText().toString();
                String weight=editWeight.getText().toString();

                //logic steps: 1st check editText is empty?
                //2nd check password and confirm password are the same?
                //3rd check user name exist?
                //4th if every thing is ok. Then insert data. If insert successful -> show alert
                //after inserting, we will be navigated to recommendation page
                if(name2.isEmpty()||pas2.isEmpty()||rePas.isEmpty()||age.isEmpty()||height.isEmpty()||weight.isEmpty()){
                    Toast.makeText(SignUpActivity.this, "Please enter all the fileds!", Toast.LENGTH_SHORT).show();
                }else{
                    if(pas2.equals(rePas)){
                        Boolean isNameExist=DB.checkNameExist(name2);
                        if(isNameExist==false){
                            if(radioGender.getCheckedRadioButtonId()==R.id.radioButtonMale){
                                gender="male";
                            }else{
                                gender="female";
                            }
                            Boolean insert=DB.insertData(name2,pas2, gender, age,height,weight);
                            if(insert==true){
                                Toast.makeText(SignUpActivity.this, "Register successfully!", Toast.LENGTH_SHORT).show();
//                                Toast.makeText(SignUpActivity.this, "Register successfully! Name: "+name2+
//                                        " Pass: "+pas2+" Gender: "+gender+" Age: "+age+" Height: "+
//                                        height+" Weight: "+weight, Toast.LENGTH_SHORT).show();

                                //create a bundle, put data to it, then navigate to empty page
                                Intent myIntent=new Intent(SignUpActivity.this, MainActivity.class);
//                                Bundle myBundle=new Bundle();
//                                myBundle.putString("NAME",name2);
//                                myBundle.putString("GENDER",gender);
//                                myBundle.putString("AGE",age);
//                                myBundle.putString("HEIGHT",height);
//                                myBundle.putString("WEIGHT",weight);
//                                myIntent.putExtras(myBundle);
                                startActivity(myIntent);
                            }else {
                                Toast.makeText(SignUpActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(SignUpActivity.this, "This username existed!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(SignUpActivity.this, "Confirm password is wrong!", Toast.LENGTH_SHORT).show();
                    }
                }

            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }
        });
    }
}