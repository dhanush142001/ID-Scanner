package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class mainact2 extends AppCompatActivity {
    LottieAnimationView ass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainact2);
        ass = findViewById(R.id.wassup);


        long l = 1100;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mainact2.this, "Success!!", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        },l);
    }
}