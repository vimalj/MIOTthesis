package com.example.yo7a.healthwatcher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    public Button login;
    public Button acc;
    public EditText ed1, ed2;
    private Toast mainToast;
    public static String passStr, usrStr, checkpassStr, usrStrlow;

    //internal db code
    UserDB check = new UserDB(this);
    CheckBox chkRememberMe;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;

    FirebaseAuth firebaseAuth;
    private FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        mFirestore = FirebaseFirestore.getInstance();

        //checking whether user is signed in or not
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Toast.makeText(this, "User signed in", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this, Primary.class);
            i.putExtra("Usr", usrStr);
            startActivity(i);
            finish();

        } else {
            // No user is signed in
            Toast.makeText(this, "Not signed in", Toast.LENGTH_SHORT).show();
        }

        login = findViewById(R.id.login);
        acc = findViewById(R.id.newacc);
        ed1 = findViewById(R.id.edtu1);
        ed2 = findViewById(R.id.edtp1);

        //internal db code
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);


        //internal db code
        if (saveLogin) {
            ed1.setText(loginPreferences.getString("username", ""));
            ed2.setText(loginPreferences.getString("password", ""));
            chkRememberMe.setChecked(true);
        }

        login.setOnClickListener(v -> {
            usrStrlow = ed1.getText().toString();
            passStr = ed2.getText().toString();
            usrStr = usrStrlow.toLowerCase();


            if (usrStr.length() < 3 || usrStr.length() > 20) {
                mainToast = Toast.makeText(getApplicationContext(), "Username length must be between 3-20 characters", Toast.LENGTH_SHORT);
                mainToast.show();
            }

            if (passStr.length() < 3 || passStr.length() > 20) {
                mainToast = Toast.makeText(getApplicationContext(), "Password length must be between 3-20 characters", Toast.LENGTH_SHORT);
                mainToast.show();
            } else if (passStr.isEmpty() || usrStr.isEmpty()) {
                mainToast = Toast.makeText(getApplicationContext(), "Please enter your Username and Password ", Toast.LENGTH_SHORT);
                mainToast.show();
            } else {

                //firebase login

                firebaseAuth.signInWithEmailAndPassword(usrStr, passStr).addOnCompleteListener(Login.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(Login.this, "Not sucessfull", Toast.LENGTH_SHORT).show();
                        } else {
                            String token_id = FirebaseInstanceId.getInstance().getToken();

                            //get current uid
                            String current_id = firebaseAuth.getCurrentUser().getUid();

                            //get current email-id
                            String current_email_id = firebaseAuth.getCurrentUser().getEmail();

                            //This saves token-id of user's current device each time it login's in some device
                            Map<String, Object> tokenMap = new HashMap<>();
                            tokenMap.put("token_id", token_id);

                            mFirestore.collection("patients").document(current_email_id)
                                    .update(tokenMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent i = new Intent(v.getContext(), Primary.class);
                                    i.putExtra("Usr", usrStr);
                                    startActivity(i);
                                    finish();
                                }
                            });

                        }
                    }
                });

            }

        });

        acc.setOnClickListener(v -> {
            Intent i = new Intent(v.getContext(), SignUp.class);
            startActivity(i);
        });
    }

}
