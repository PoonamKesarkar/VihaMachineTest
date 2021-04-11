package com.vihatest.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
import android.widget.Toast;

import com.vihatest.R;
import com.vihatest.database.DatabaseClient;
import com.vihatest.model.JobPostData;
import com.vihatest.util.AppPreference;
import com.vihatest.viewmodel.RecruiterViewModel;

public class RecruiterDashboardActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    @BindView(R.id.btnPostJob)
    Button btnPostJob;
    @BindView(R.id.d_textview)
    TextView dTextView;
    @BindView(R.id.back_imageview)
    ImageView back_imageview;
    @BindView(R.id.imglogout)
    ImageView imglogout;
    private AppPreference preference;
    int profileType=0;
    RecruiterViewModel recruiterViewModel;
    private ProgressDialog progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_dashboard);
        ButterKnife.bind(this);
        dTextView.setText("Recruiter Dashboard");

        recruiterViewModel = new ViewModelProvider(RecruiterDashboardActivity.this).get(RecruiterViewModel.class);
        preference = new AppPreference(this);
        progressBar = new ProgressDialog(this);
    }

    @OnClick({R.id.btnPostJob,R.id.imglogout,R.id.back_imageview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPostJob:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                View dialogview = getLayoutInflater().inflate(R.layout.job_post_layout, null);
                EditText edtJobTitle = dialogview.findViewById(R.id.edtJobTitle);
                Spinner spinnerProfileType = dialogview.findViewById(R.id.spinnerProfileType);
                EditText edtLocation = dialogview.findViewById(R.id.edtLocation);
                EditText edtSalary = dialogview.findViewById(R.id.edtSalary);
                Button btnClose = dialogview.findViewById(R.id.btnClose);
                Button btnSubmit = dialogview.findViewById(R.id.btnSubmit);

                alertDialog.setView(dialogview);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                        this,android.R.layout.simple_spinner_dropdown_item,
                        getResources().getStringArray(R.array.profile_array)); //Your resource name

                spinnerProfileType.setAdapter(arrayAdapter);
                spinnerProfileType.setOnItemSelectedListener(this);
                final Dialog dialog = alertDialog.create();

                btnClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                           if (validatejobTitlle(edtJobTitle) && validateLocation(edtLocation) && validateSalary(edtSalary) && validateProfileType(spinnerProfileType)) {
                               JobPostData jobPostData=new JobPostData();
                               jobPostData.setUserId(preference.getUserid());
                               jobPostData.setJobtitle(edtJobTitle.getText().toString());
                               jobPostData.setLocation(edtLocation.getText().toString());
                               jobPostData.setSalary(edtSalary.getText().toString());
                               jobPostData.setProfiletype(profileType);
                               addJobDataOnDatabase(jobPostData,dialog);
                           }

                    }
                });

                dialog.show();
                break;

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
                Intent i =new Intent(RecruiterDashboardActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create().show();
    }

    private void addJobDataOnDatabase(JobPostData jobPostData, Dialog dialog) {
        recruiterViewModel.insertJobPostData(jobPostData, DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .recruiterDao());
        recruiterViewModel.isInsert().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                if(aLong>0){
                    Toast.makeText(RecruiterDashboardActivity.this, "Inserted Successfully.", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else if(aLong==0){
                    Toast.makeText(RecruiterDashboardActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        recruiterViewModel.isLoading().observe(this, new Observer<Boolean>() {
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
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         profileType=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean validatejobTitlle(EditText edtJob) {
        String jobTitle = edtJob.getText().toString().trim();
        edtJob.setError(null);
        if (jobTitle.length() <=0) {
            edtJob.setError("Please enter job title");
            edtJob.requestFocus();
            return false;
        } else {
            edtJob.setError(null);
            return true;
        }
    }
    private boolean validateLocation(EditText edtLocation) {
        String location = edtLocation.getText().toString().trim();
        edtLocation.setError(null);
        if (location.length() <=0) {
            edtLocation.setError("Please enter location");
            edtLocation.requestFocus();
            return false;
        } else {
            edtLocation.setError(null);
            return true;
        }
    }
    private boolean validateSalary(EditText edtSalary) {
        String salary = edtSalary.getText().toString().trim();
        edtSalary.setError(null);
        if (salary.length() <=0) {
            edtSalary.setError("Please enter job title");
            edtSalary.requestFocus();
            return false;
        } else {
            edtSalary.setError(null);
            return true;
        }
    }
    private boolean validateProfileType(Spinner spinnerProfileType ) {

        if (spinnerProfileType.getSelectedItemPosition()== 0) {
            Toast.makeText(this, "Please select profile type.", Toast.LENGTH_SHORT).show();
            spinnerProfileType.requestFocus();
            return false;
        } else {
            return true;
        }
    }
}