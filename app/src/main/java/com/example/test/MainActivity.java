package com.example.test;


import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.os.Build;

import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.os.Looper;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import android.os.Bundle;
public class MainActivity extends AppCompatActivity {
    ActivityResultLauncher<String[]> mPermissionResultLauncher;
    private boolean ispermissionlocation = false;
    private boolean ispermissionCamera = false;
    private Button opencamera;
    ScanOptions options = new ScanOptions();
    private String bcode = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("ID Scanner");
        mPermissionResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if (result.get(Manifest.permission.ACCESS_FINE_LOCATION) != null){
                    ispermissionlocation = result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (result.get(Manifest.permission.CAMERA) != null){
                    ispermissionlocation = result.get(Manifest.permission.CAMERA);
                }

            }
        });

        requestPermission();
        opencamera = findViewById(R.id.locationButton);
        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scan_code();
            }
        });
    }
    class bgthread extends Thread{
        @Override
        public void run() {
            super.run();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "scanned_Barcodes").build();
            UserDao userDao = db.userDao();

            userDao.insertAll(new User(bcode));
        }
    }
    private void scan_code(){
        options.setPrompt("Volume button to turn on flash");
        options.setBarcodeImageEnabled(true);
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CapAct.class);
        barlauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barlauncher = registerForActivityResult(new ScanContract(),result -> {
        if(result.getContents() != null){
            if (result.getContents().length() > 10){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Result");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        bcode = result.getContents().toString();
                        new bgthread().start();
                        dialogInterface.dismiss();
                        Intent ips = new Intent(getApplicationContext(),mainact2.class);
                        startActivity(ips);
                    }
                });builder.show();

            }
            else if(result.getContents().length() < 10){
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Result");
                builder.setMessage(result.getContents());
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        Intent io = new Intent(getApplicationContext(),failed_act.class);
                        startActivity(io);
                    }
                });builder.show();

            }

        }

    });
    private void requestPermission(){
        ispermissionlocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        ispermissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        List<String> permissionRequest = new ArrayList<>();
        if(! ispermissionlocation){
            permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(! ispermissionCamera){
            permissionRequest.add(Manifest.permission.CAMERA);
        }
        if(!permissionRequest.isEmpty()){
            mPermissionResultLauncher.launch(permissionRequest.toArray(new String[0]));
        }
    }
}