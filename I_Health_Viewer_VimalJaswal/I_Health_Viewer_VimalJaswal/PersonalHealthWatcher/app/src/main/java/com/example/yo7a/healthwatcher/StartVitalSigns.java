package com.example.yo7a.healthwatcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StartVitalSigns extends AppCompatActivity {
    private String user;
    private int p;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int PHONE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vital_signs);

                checkPermission(
                Manifest.permission.CALL_PHONE,
                PHONE_PERMISSION_CODE,
                        Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE

                );

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("Usr");
            p = extras.getInt("Page");
        }

        Button VS = this.findViewById(R.id.StartVS);

        VS.setOnClickListener(v -> {

            //switch is to decide which activity must be opened
            switch (p) {

                case 1: {
                    Intent i = new Intent(v.getContext(), HeartRateProcess.class);
                    i.putExtra("Usr", user);
                    startActivity(i);
                    finish();
                }
                break;

                case 2: {
                    Intent i = new Intent(v.getContext(), BloodPressureProcess.class);
                    i.putExtra("Usr", user);
                    startActivity(i);
                    finish();
                }
                break;
            }

        });
    }


    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode,String permission1, int requestCode1)
    {
        if (ContextCompat.checkSelfPermission(StartVitalSigns.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(StartVitalSigns.this,
                    new String[] { permission },
                    requestCode);


        }
        else {
            Toast.makeText(StartVitalSigns.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();

            if (ContextCompat.checkSelfPermission(StartVitalSigns.this, permission1)
                    == PackageManager.PERMISSION_DENIED) {

                // Requesting the permission
                ActivityCompat.requestPermissions(StartVitalSigns.this,
                        new String[] { permission1 },
                        requestCode1);


            }

//            checkPermission(
//                    Manifest.permission.CAMERA,
//                    CAMERA_PERMISSION_CODE
//            );
        }


    }

        public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(StartVitalSigns.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(StartVitalSigns.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == PHONE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(StartVitalSigns.this,
                        "Phone Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();

                checkPermission(
                        Manifest.permission.CALL_PHONE,
                        PHONE_PERMISSION_CODE,
                        Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE

                );
            }
            else {
                Toast.makeText(StartVitalSigns.this,
                        "Phone Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(StartVitalSigns.this, Primary.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }


}
