package com.vihatest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.vihatest.R;
import com.vihatest.util.AppPreference;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity  {
    @BindView(R.id.layoutApplicant)
    LinearLayout layoutApplicant;
    @BindView(R.id.layoutRecruiter)
    LinearLayout layoutRecruiter;
    private AppPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        preference = new AppPreference(this);
    }

    @OnClick({R.id.layoutApplicant, R.id.layoutRecruiter})
    public void onClick(View view) {
        if (view.getId() == R.id.layoutApplicant) {
            if(preference.getUserid()>0){
                Toast.makeText(this, "First logout as a recruiter", Toast.LENGTH_SHORT).show();
                return;
            }
            if(preference.getApplicantId()>0) {
                Intent mainIntent = new Intent(MainActivity.this, ApplicantDashboardActivity.class);
                startActivity(mainIntent);
            }else{
                Intent mainIntent = new Intent(MainActivity.this, ApplicantLoginActivity.class);
                startActivity(mainIntent);
            }
        }else if(view.getId() == R.id.layoutRecruiter){
            if(preference.getApplicantId()>0){
                Toast.makeText(this, "First logout as a applicant", Toast.LENGTH_SHORT).show();
                return;
            }
            if(preference.getUserid()>0){
                Intent mainIntent = new Intent(MainActivity.this, RecruiterDashboardActivity.class);
                startActivity(mainIntent);
            }else {
                Intent mainIntent = new Intent(MainActivity.this, RecruiterLoginActivity.class);
                startActivity(mainIntent);
            }
        }
    }
}