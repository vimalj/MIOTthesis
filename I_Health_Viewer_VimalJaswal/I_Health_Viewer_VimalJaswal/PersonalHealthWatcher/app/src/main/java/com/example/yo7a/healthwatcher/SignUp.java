package com.example.yo7a.healthwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    public Button signUp;
    public EditText ed1, ed2, ed3, ed4, ed5, ed6, ed7, ed8;
    private Toast mainToast;
    public Spinner GenderSpin;
    public String m1 = "Male";
    public String m2 = "Female";
    public String nameStr, weightStr, heightStr, ageStr, passStr, usrStr, usrStrlow, passConStr, emailStr, gender, token_id;
    private int age, weight, height;

    //internal db code
    UserDB Data = new UserDB(this);
    UserDB check = new UserDB(this);
    int c, y = 0;

    int check1 = 0;
    FirebaseAuth firebaseAuth;

    // Access a Cloud Firestore instance from your Activity
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        firebaseAuth = FirebaseAuth.getInstance();

        signUp = findViewById(R.id.signup);
        ed1 = findViewById(R.id.edth);
        ed2 = findViewById(R.id.edtw);
        ed3 = findViewById(R.id.edtn);
        ed4 = findViewById(R.id.edta);
        ed5 = findViewById(R.id.edtu);
        ed6 = findViewById(R.id.edtp);
        ed7 = findViewById(R.id.edtpc);
        ed8 = findViewById(R.id.edte);
        GenderSpin = findViewById(R.id.SGender);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.Gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpin.setAdapter(adapter);


        signUp.setOnClickListener(v -> {

            check1 = 0;
            heightStr = ed1.getText().toString();
            weightStr = ed2.getText().toString();
            nameStr = ed3.getText().toString();
            ageStr = ed4.getText().toString();
            usrStrlow = ed5.getText().toString();
            passStr = ed6.getText().toString();
            passConStr = ed7.getText().toString();
            emailStr = ed8.getText().toString();
            usrStr = usrStrlow.toLowerCase();

            //internal db code
            c = check.checkUser(usrStr); //will check if username exists will return 0 otherwise it will be 1

            if (usrStr.length() < 3 || usrStr.length() > 20) {
                check1 = 1;
                mainToast = Toast.makeText(getApplicationContext(), "Username length must be between 3-20 characters", Toast.LENGTH_SHORT);
                mainToast.show();
            }
            //internal db code
            if (c == y) {
                check1 = 1;
                mainToast = Toast.makeText(getApplicationContext(), "Username already exist", Toast.LENGTH_SHORT);
                mainToast.show();

            }

            if (!(passStr.equals(passConStr))) {
                check1 = 1;
                mainToast = Toast.makeText(getApplicationContext(), "Password don't match !", Toast.LENGTH_SHORT);
                mainToast.show();
            }

            if (passStr.length() < 3 || passStr.length() > 20) {
                check1 = 1;
                mainToast = Toast.makeText(getApplicationContext(), "Password length must be between 3-20 characters", Toast.LENGTH_SHORT);
                mainToast.show();
            }

            if (ageStr.isEmpty() || nameStr.isEmpty() || heightStr.isEmpty() || weightStr.isEmpty() || passStr.isEmpty() || passConStr.isEmpty() || emailStr.isEmpty() || usrStr.isEmpty()) {
                check1 = 1;
                mainToast = Toast.makeText(getApplicationContext(), "Please fill all your data ", Toast.LENGTH_SHORT);
                mainToast.show();
            } else if (check1 == 0) {

                check1 = 0;
                age = Integer.parseInt(ageStr);
                weight = Integer.parseInt(weightStr);
                height = Integer.parseInt(heightStr);
                String text = GenderSpin.getSelectedItem().toString();
                int k = 0;

                if (text.equals(m1)){
                    k = 1;
                    gender = m1;
                } //If gender is male K = 1

                if (text.equals(m2)){
                    k = 2;
                    gender = m2;
                } //If gender is female K = 2

                //internal db code
                user per = new user();
                per.setUsername(usrStr);
                per.setname(nameStr);
                per.setage(age);
                per.setemail(emailStr);
                per.setPass(passStr);
                per.setheight(height);
                per.setweight(weight);
                per.setgender(k);
                Data.addUser(per);

                //firebase code for signup
                firebaseAuth.createUserWithEmailAndPassword(emailStr, passStr).addOnCompleteListener(SignUp.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this.getApplicationContext(),
                                    "SignUp unsuccessful: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        else {

                            token_id = FirebaseInstanceId.getInstance().getToken();

                            //firebase code for adding patient details to firestore
                            Map<String, Object> patients = new HashMap<>();
                            patients.put("username",usrStr);
                            patients.put("name", nameStr);
                            patients.put("age", age);
                            patients.put("email", emailStr);
                            patients.put("height", height);
                            patients.put("weight", weight);
                            patients.put("gender", gender);
                            patients.put("token_id", token_id);

                            db.collection("patients").document(emailStr)
                                    .set(patients)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            android.util.Log.d("Success", "DocumentSnapshot successfully written!");
                                            Intent i = new Intent(v.getContext(), Login.class);
                                            mainToast = Toast.makeText(getApplicationContext(), "Your account has been created", Toast.LENGTH_SHORT);
                                            mainToast.show();
                                            startActivity(i);
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            android.util.Log.d("Failed", "Error writing document", e);
                                        }
                                    });

                        }
                    }
                });

            }

        });
    }
}

