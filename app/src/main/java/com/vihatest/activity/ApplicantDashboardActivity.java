package com.vihatest.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vihatest.R;
import com.vihatest.database.DatabaseClient;
import com.vihatest.model.JobPostData;
import com.vihatest.util.AppPreference;
import com.vihatest.viewmodel.ApplicantViewModel;

import java.util.ArrayList;
import java.util.List;

public class ApplicantDashboardActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    @BindView(R.id.d_textview)
    TextView dTextView;
    @BindView(R.id.back_imageview)
    ImageView back_imageview;
    @BindView(R.id.imglogout)
    ImageView imglogout;
    private AppPreference preference;
    @BindView(R.id.recyclerList)
    RecyclerView recyclerList;
    @BindView(R.id.spinnerLocation)
    Spinner spinnerLocation;
    private ArrayList<JobPostData> list;
    private JobAdapter adapter;
    private ProgressDialog progressBar;
    ApplicantViewModel applicantViewModel;
    private String[] locationArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_dashboard);
        ButterKnife.bind(this);
        dTextView.setText("Applicant Dashboard");

        locationArray = getResources().getStringArray(R.array.location_array);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.location_array)); //Your resource name

        spinnerLocation.setAdapter(arrayAdapter);
        spinnerLocation.setOnItemSelectedListener(this);
        list = new ArrayList<>();
        preference = new AppPreference(this);
        progressBar = new ProgressDialog(this);
        applicantViewModel = new ViewModelProvider(ApplicantDashboardActivity.this).get(ApplicantViewModel.class);
        adapter = new JobAdapter(this, list);
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(ApplicantDashboardActivity.this, LinearLayoutManager.VERTICAL, false);
        recyclerList.setLayoutManager(verticalLayoutManagaer);
        recyclerList.setAdapter(adapter);
        getAllJobData(null);

    }
    @OnClick({R.id.imglogout,R.id.back_imageview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imglogout:
                logOut();
                break;

            case R.id.back_imageview:
                onBackPressed();
                break;

        }
    }

    private void logOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log Out?");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                preference.clearPreference();
                Intent i =new Intent(ApplicantDashboardActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }
    private void getAllJobData(String location) {
        Log.d("FliterData","location:"+location);
        applicantViewModel.getJobData(location,DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .recruiterDao());
        applicantViewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d("onemailLoad", "isLoading aBoolean " + aBoolean);
                if(aBoolean){
                    progressBar.setMessage("Loading");
                    progressBar.show();
                }else{
                    progressBar.dismiss();
                }

            }
        });
        applicantViewModel.getAllJobData().observe(this, new Observer<List<JobPostData>>() {
            @Override
            public void onChanged(List<JobPostData> jobPostData) {

                list.clear();
                if(jobPostData!=null) {
                    Log.d("FliterData","jobPostData:"+jobPostData.size());
                    list.addAll(jobPostData);
                   // Log.d("FliterData","jobPostData:"+jobPostData.get(0).getLocation());
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("FliterData","onItemSelected:"+locationArray[position]);
        if(position!=0) {
            getAllJobData(locationArray[position]);
        }else{
            getAllJobData(null);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}