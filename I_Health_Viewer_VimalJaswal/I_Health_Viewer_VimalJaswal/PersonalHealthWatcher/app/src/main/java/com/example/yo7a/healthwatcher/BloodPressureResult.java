package com.example.yo7a.healthwatcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BloodPressureResult extends AppCompatActivity {

    private String user, Date;
    int SP, DP;
    public String currentUserEmail,bpp;
    //Patient Details Map
    public String nameStr, emailStr, heartRate, gender, token_id,bp;
    public long age, weight, height;
    int HR;

    //ArrayList of bp for bar graph
    ArrayList<String> bpList=new ArrayList<String>();

    //Firebase variables
    final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    Date today = Calendar.getInstance().getTime();

    //Get current user email
    String getCurrentUserEmailId() {
        try {
            final FirebaseUser user = firebaseAuth.getCurrentUser();
            final String emailId = user.getEmail();

            return emailId;
        } catch (Exception e) {
            return "e: " + e.getMessage();
        }
    }

    void getCurrentUserDetailsAndStoreInNotification() {
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    age = (Long) documentSnapshot.get("age");
                    weight = (Long) documentSnapshot.get("weight");
                    height = (Long) documentSnapshot.get("height");
                    nameStr = documentSnapshot.getString("name");
                    emailStr = documentSnapshot.getString("email");
                    gender = documentSnapshot.getString("gender");
                    token_id = documentSnapshot.getString("token_id");
                    Log.d("Age: ", Long.toString(age));
                    Log.d("Age: ", Long.toString(weight));
                    Log.d("Age: ", Long.toString(height));
                    Log.d("Age: ", nameStr);

                    Map<String, Object> patientDetails = new HashMap<>();

                    patientDetails.put("userName", nameStr);
                    patientDetails.put("age", age);
                    patientDetails.put("email", emailStr);
                    patientDetails.put("height", height);
                    patientDetails.put("weight", weight);
                    patientDetails.put("heartRate", SP + " / " + DP);

                    patientDetails.put("gender", gender);
                    patientDetails.put("token_id", token_id);

                    db.collection("doctors").document("andy@gmail.com")
                            .collection("patientNotifications").document(currentUserEmail)
                            .set(patientDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("Success", "DocumentSnapshot successfully written!");
                                    Toast.makeText(getApplicationContext(), "Notification Sent to Doctor", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failed", "Error in sending notification. Please check your internet connection", e);
                                    Toast.makeText(getApplicationContext(), "Error in sending notification. Please check your internet connection",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                    db.collection("patients").document(currentUserEmail)
                            .collection("notifications").document("andy@gmail.com")
                            .set(patientDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Log.d("Success", "DocumentSnapshot successfully written!");
//                                    Toast.makeText(getApplicationContext(), "Notification Sent to Doctor", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("Failed", "Error in sending notification. Please check your internet connection", e);
//                                    Toast.makeText(getApplicationContext(), "Error in sending notification. Please check your internet connection",
//                                            Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    Toast.makeText(BloodPressureResult.this, "Data does not exist.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    public void notifyDoctor(View view) {
//        user = getCurrentUserEmailId();
//        System.out.println("Signed in user is " + email);
        Log.d("DEBUG_TAG", "Email: " + currentUserEmail);
        Log.d("DEBUG_TAG", "Blood Pressure: " + SP + " / " + DP);
        Log.d("DEBUG_TAG", "Date: " + today.toString());
        getCurrentUserDetailsAndStoreInNotification();
    }

    public void callAmbulance(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        //Add ambulance number
        callIntent.setData(Uri.parse("tel:+919326041564"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_pressure_result);
        user = getCurrentUserEmailId();

        currentUserEmail = getCurrentUserEmailId();


        Date = df.format(today);
        TextView TBP = (TextView) this.findViewById(R.id.BPT);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            SP = bundle.getInt("SP");
            DP = bundle.getInt("DP");
            user = bundle.getString("Usr");
            TBP.setText(String.valueOf(SP + " / " + DP));
            bpp = Long.toString(SP);
            bpList.add(bpp);


            Map<String, Object> bpListMap = new HashMap<>();

            bpListMap.put("bpList", bpList);
//            add to firestore

            db.collection("patients").document(currentUserEmail)
                    .update("bpList", FieldValue.arrayUnion(bpp))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            Log.d("Success", "DocumentSnapshot successfully written!");
                            Toast.makeText(getApplicationContext(), "Heart Rate Recorded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Failed", "Error in sending notification. Please check your internet connection", e);
                            Toast.makeText(getApplicationContext(), "Error in recording Heart Rate",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }

        docRef = db.collection("patients").document(currentUserEmail);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(BloodPressureResult.this, Primary.class);
        i.putExtra("Usr", user);
        startActivity(i);
        finish();
    }
}
