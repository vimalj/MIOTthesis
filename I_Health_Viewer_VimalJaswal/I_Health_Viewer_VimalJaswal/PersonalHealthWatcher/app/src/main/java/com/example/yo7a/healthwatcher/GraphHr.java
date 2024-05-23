package com.example.yo7a.healthwatcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.yo7a.healthwatcher.R;
import com.example.yo7a.healthwatcher.util.MPUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphHr extends AppCompatActivity {


    private BarChart chart;
    public String current_email_id;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<String> group;



    //Get current user email
    String getCurrentUserEmailId(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentEmail=user.getEmail();
        return currentEmail;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph_hr);
        current_email_id = getCurrentUserEmailId();

        init();
        FirebaseFirestore.getInstance().collection("patients").document(current_email_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                group = (List<String>) document.get("heartRateList");
                Log.d("GraphHR","Heart Rate List: "+group);

                BarData barData2 = new BarData(MPUtil.getXAxisValues(22), MPUtil.getDataSet(GraphHr.this, group));
                MPUtil.drawChart(GraphHr.this, chart, barData2);


                Log.d("myTag", String.valueOf(group));
            }
        });

        Random r = new Random();
        List<Float> dataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            dataList.add((float)i);
        }

    }

    private void init() {
        chart = (BarChart) findViewById(R.id.chart);

        chart.animateXY(3000, 3000);

    }

    @Override
    public void onBackPressed() {
      Intent i=new Intent(GraphHr.this,Primary.class);
      startActivity(i);
    }
}