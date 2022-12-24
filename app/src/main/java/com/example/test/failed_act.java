package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class failed_act extends AppCompatActivity {
    LottieAnimationView assin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_failed);
        assin = findViewById(R.id.faiii);
        long l = 1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(failed_act.this, "Try Again", Toast.LENGTH_SHORT).show();
                Intent in = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(in);

            }
        },l);;

    }
}