package com.example.doctorhealthwatcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseFirestore mFirestore;

    public List<Patient> patientsList;
    public List<String> patientDoc;
    PatientAdapter patientAdapter;
    RecyclerView patientsListView;

    public String current_email_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Get current user email
    String getCurrentUserEmailId(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail=user.getEmail();
        return currentEmail;
    }

    private void logoutUser() {
        DocumentReference washingtonRef = db.collection("doctors").document(current_email_id);
        washingtonRef
                .update("token_id", "")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("", "DocumentSnapshot successfully updated!");
                        FirebaseAuth.getInstance().signOut();
                        Intent i=new Intent(MainActivity.this,First.class);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        current_email_id = getCurrentUserEmailId();

//        getSupportActionBar().setTitle("Doctor Health Watcher");

        mFirestore = FirebaseFirestore.getInstance();

        patientsListView = (RecyclerView) findViewById(R.id.recycler_view);

        patientsList = new ArrayList<>();
        patientDoc = new ArrayList<>();

        patientAdapter = new PatientAdapter(patientsList);

        patientsListView.setHasFixedSize(true);
        patientsListView.setLayoutManager(new LinearLayoutManager(this));
        patientsListView.setAdapter(patientAdapter);

        fillTheRecyclerView();


        patientAdapter.setOnItemClickListener(new PatientAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Patient patient) {
//                Intent intent = new Intent(MainActivity.this, GraphHr.class);
//                intent.putExtra(GraphHr.EXTRA_USERNAME, patient.getUserName());
//                intent.putExtra(GraphHr.EXTRA_EMAIL, patient.getEmail());
//                intent.putExtra(GraphHr.EXTRA_HEARTRATE, patient.getHeartRate());
//                startActivity(intent);
//                finish();
                Intent intent = new Intent(MainActivity.this, ProfilePage.class);
                intent.putExtra(ProfilePage.EXTRA_USERNAME, patient.getUserName());
                intent.putExtra(ProfilePage.EXTRA_EMAIL, patient.getEmail());
                intent.putExtra(ProfilePage.EXTRA_HEARTRATE, patient.getHeartRate());
                startActivity(intent);
                finish();
            }
        });


    }

    protected void fillTheRecyclerView() {
        mFirestore.collection("doctors").document("andy@gmail.com")
                .collection("patientNotifications").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc: querySnapshot.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        Patient patients = doc.getDocument().toObject(Patient.class);
                        patientsList.add(patients);
                        patientDoc.add(doc.getDocument().get("email").toString());

                        patientAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_warning)
                        .setTitle("Delete Notification")
                        .setMessage("Do you want to delete this notification?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "Notification deleted"+patientDoc.get(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                                mFirestore.collection("doctors").document("andy@gmail.com")
                                        .collection("patientNotifications").document(patientDoc.get(viewHolder.getAdapterPosition()))
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("TAG", "DocumentSnapshot successfully deleted!");
                                                recreate();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w("TAG", "Error deleting document", e);
                                            }
                                        });

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                recreate();
                                Toast.makeText(MainActivity.this, "Note deletion canceled", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        }).attachToRecyclerView(patientsListView);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar actions click
        switch (item.getItemId()) {
            case R.id.action_logout:
                logoutUser();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, (arg0, arg1) -> {

                    MainActivity.super.onBackPressed();
                    finish();
                    System.exit(0);
                }).create().show();
    }

}
