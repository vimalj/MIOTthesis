package com.example.doctorhealthwatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfilePage extends AppCompatActivity {

    public static final String EXTRA_USERNAME = "com.example.doctorhealthwatcher.EXTRA_USERNAME";
    public static final String EXTRA_EMAIL = "com.example.doctorhealthwatcher.EXTRA_EMAIL";
    public static final String EXTRA_HEARTRATE = "com.example.doctorhealthwatcher.EXTRA_HEARTRATE";

    String getExtraUsername="";
    String getExtraEmail="";
    String getExtraHeartrate="";
    String age="";
    String gender="";
    String height="";
    String username="";
    String weight="";

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        TextView nameTV=findViewById(R.id.name);
        TextView usernameTV=findViewById(R.id.username);
        TextView emailTV=findViewById(R.id.email);
        TextView ageTV=findViewById(R.id.age);
        TextView heightTV=findViewById(R.id.height);
        TextView weightTV=findViewById(R.id.weight);
        TextView genderTV=findViewById(R.id.gender);
        Button HR=findViewById(R.id.HR);
        Button BP=findViewById(R.id.BP);


        Intent intent = getIntent();

        getExtraEmail = intent.getStringExtra(EXTRA_EMAIL);
        getExtraUsername = intent.getStringExtra(EXTRA_USERNAME);
        getExtraHeartrate = intent.getStringExtra(EXTRA_HEARTRATE);

//        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();

        Log.v("asdfg",getExtraEmail+" "+getExtraUsername+" "+getExtraHeartrate);
        DocumentReference docRef = db.collection("patients").document(getExtraEmail);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
//                        Log.d("TAG", "DocumentSnapshot data: " + document.getData().get("age"));
                        age= String.valueOf(document.getData().get("age"));
                        gender= (String) document.getData().get("gender");
                        height= String.valueOf(document.getData().get("height"));
                        username= (String) document.getData().get("username");
                        weight= String.valueOf(document.getData().get("weight"));
                        Log.d("TAG", "DocumentSnapshot data: " + age+" "+gender+" "+height+" "+username+" "+weight);
                        ageTV.setText(age);
                        nameTV.setText(getExtraUsername);
                        usernameTV.setText(username);
                        emailTV.setText(getExtraEmail);
                        heightTV.setText(height);
                        weightTV.setText(weight);
                        genderTV.setText(gender);

                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        HR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, GraphHr.class);
                intent.putExtra(GraphHr.EXTRA_USERNAME, getExtraUsername);
                intent.putExtra(GraphHr.EXTRA_EMAIL, getExtraEmail);
                intent.putExtra(GraphHr.EXTRA_HEARTRATE, getExtraHeartrate);
                startActivity(intent);
                finish();
            }
        });
        BP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfilePage.this, GraphBp.class);
                intent.putExtra(GraphBp.EXTRA_USERNAME, getExtraUsername);
                intent.putExtra(GraphBp.EXTRA_EMAIL, getExtraEmail);
                intent.putExtra(GraphBp.EXTRA_BP, getExtraHeartrate);
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent i=new Intent(ProfilePage.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}