package com.example.f21g1_caloriecalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG="Calorie Demo";
    EditText editName, editPass;
    Button btnLogIn, btnSignUp;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName=findViewById(R.id.editTextName);
        editPass=findViewById(R.id.editTextPass);
        btnLogIn=findViewById(R.id.btnLogIn);
        btnSignUp=findViewById(R.id.btnSignUp);
        DB=new DBHelper(MainActivity.this);

        btnLogIn.setOnClickListener((View v)->{
            String name=editName.getText().toString();
            String pas=editPass.getText().toString();

            //logic steps: 1st check editText is empty?
            //2nd check account is already?
            try{
                if(name.isEmpty()||pas.isEmpty()){
                    Toast.makeText(MainActivity.this, "If you have account already, please enter!", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean isAccountExist=DB.checkUserNamePassword(name,pas);
                    if(isAccountExist==true){
                        Toast.makeText(MainActivity.this, "Welcome back "+name, Toast.LENGTH_SHORT).show();
                        Intent intent= new Intent(MainActivity.this, Calendar.class);
                        startActivity(intent);
                        //chuyen qua homepage
                    }else{
                        Toast.makeText(MainActivity.this, "Account is invalid.", Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception e){
                Log.e(TAG, e.getMessage());
            }

        });

        btnSignUp.setOnClickListener((View v)->{
            startActivity(new Intent(MainActivity.this,SignUpActivity.class));
        });
    }
}