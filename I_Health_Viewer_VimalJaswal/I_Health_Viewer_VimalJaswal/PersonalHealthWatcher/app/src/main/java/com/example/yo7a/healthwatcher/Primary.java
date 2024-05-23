package com.example.yo7a.healthwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Primary extends AppCompatActivity {

    private static final String TAG = "Primary";
    private String user;
    private int p;

    public String current_email_id;
    ImageButton Abt;
    Button graphHR,graphBp;
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    //Get current user email
    String getCurrentUserEmailId(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail=user.getEmail();
        return currentEmail;
    }

    public void logout(View v){
        DocumentReference washingtonRef = db.collection("patients").document(current_email_id);

        washingtonRef
                .update("token_id", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully updated!");
                        FirebaseAuth.getInstance().signOut();
                        Intent i = new Intent(Primary.this, Login.class);
                        startActivity(i);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating document", e);
                    }
                });
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if(requestCode == 1){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
//                    Log.d(TAG, "onRequestPermissionsResult: Camera Permission allowed");
//                }
//            }
//            else{
//                Log.d(TAG, "onRequestPermissionsResult: Camera Permission denied");
//            }
//        }
//        if(requestCode == 2){
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){
//                    Log.d(TAG, "onRequestPermissionsResult: PHONE Permission allowed");
//                }
//            }
//            else{
//                Log.d(TAG, "onRequestPermissionsResult: PHONE Permission denied");
//            }
//        }
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_primary);

        current_email_id = getCurrentUserEmailId();
        Toast.makeText(this, current_email_id, Toast.LENGTH_SHORT).show();
//        getCurrentUserEmailId1();

        Button HeartRate = this.findViewById(R.id.HR);
        Button BloodPressure = this.findViewById(R.id.BP);
        Abt = (ImageButton) findViewById(R.id.About);
        graphHR = (Button) findViewById(R.id.graphHR);
        graphBp = (Button) findViewById(R.id.graphBp);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            user = extras.getString("Usr");
            //The key argument here must match that used in the other activity
        }


        //Every Test Button sends the username + the test number, to go to the wanted test after the instructions activity
        HeartRate.setOnClickListener(v -> {
            p = 1;
            Intent i = new Intent(v.getContext(), StartVitalSigns.class);
            i.putExtra("Usr", user);
            i.putExtra("Page", p);
            startActivity(i);
            finish();
        });

        BloodPressure.setOnClickListener(v -> {
            p = 2;
            Intent i = new Intent(v.getContext(), StartVitalSigns.class);
            i.putExtra("Usr", user);
            i.putExtra("Page", p);
            startActivity(i);
            finish();
        });
        graphHR.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), GraphHr.class);
            startActivity(i);
            finish();
        });

        graphBp.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), GraphBp.class);
            startActivity(i);
            finish();
        });


        //Request permission for phone and camera
//        if (Build.VERSION.SDK_INT <=23) {
//            Log.d(TAG, "onCreate: SDK version less than 23 detected");
//        }
//        else {
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
//            }
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 2);
//            }
//        }


    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {

                    Primary.super.onBackPressed();
                    finish();
                    System.exit(0);
                }).create().show();
    }


}

