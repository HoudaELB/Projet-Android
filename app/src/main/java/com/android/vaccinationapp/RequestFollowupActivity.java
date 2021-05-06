package com.android.vaccinationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.vaccinationapp.datamodel.Vaccination;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RequestFollowupActivity extends AppCompatActivity {

    private Toolbar appbar;
    private TextView req;
    private TextView certif;
    private TextView textVaccinationState;
    private TextView textFirstDoseDate;
    private TextView textSecondDoseDate;
    private TextView textLocation;


    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_followup);

        appbar = findViewById(R.id.appbar);
        setSupportActionBar(appbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        textVaccinationState = findViewById(R.id.textView6);
        textFirstDoseDate = findViewById(R.id.textView7);
        textSecondDoseDate = findViewById(R.id.textView8);
        textLocation = findViewById(R.id.textView9);

        //String userID = fAuth.getCurrentUser().getUid();

        final Task<QuerySnapshot> querySnapshotTask = db.collection("vaccinations")
                //.whereEqualTo("user", "userID")
                .whereEqualTo("request", "RgJmkgdAhuzr0DLe19KM")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<Vaccination> vaccinationList=new ArrayList<>();

                    //QuerySnapshot querySnapshot = task.getResult();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        System.out.println(document.getId() + " => " + document.getData());

                        vaccinationList.add(document.toObject(Vaccination.class));
                        Vaccination vac = task.getResult().getDocuments().get(0).toObject(Vaccination.class);

                        //Vaccination vac = querySnapshot.toObject(Vaccination.class);

                        String first_dose_date = vac.getFirst_dose_date();
                        String location = vac.getLocation();
                        String second_dose_date = vac.getSecond_dose_date();
                        String vaccination_state = vac.getVaccination_state();

                        textVaccinationState.setText(vaccination_state);
                        textFirstDoseDate.setText(first_dose_date);
                        textSecondDoseDate.setText(second_dose_date);
                        textLocation.setText(location);


                    }
                } else {
                    Toast.makeText(RequestFollowupActivity.this,"Failed to fetch data",Toast.LENGTH_SHORT).show();
                }
            }
        });


        certif = findViewById(R.id.textView11);
        certif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestFollowupActivity.this, DownloadCertificateActivity.class));
            }
        });


        req = findViewById(R.id.textView13);
        req.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RequestFollowupActivity.this, RequestActivity.class));
            }
        });

    }
}