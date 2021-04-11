package com.vihatest.activity;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.chaos.view.PinView;
import com.github.tntkhang.gmailsenderlibrary.GMailSender;
import com.github.tntkhang.gmailsenderlibrary.GmailListener;
import com.vihatest.R;
import com.vihatest.database.DatabaseClient;
import com.vihatest.model.ApplicantData;
import com.vihatest.model.RecruiterData;
import com.vihatest.util.AppPreference;
import com.vihatest.util.NetworkManager;
import com.vihatest.viewmodel.ApplicantViewModel;
import com.vihatest.viewmodel.RecruiterViewModel;


import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.vihatest.util.Constant.INTERNET_REQUEST_CODE;

public class EmailVerificationActivity extends AppCompatActivity {

    @BindView(R.id.otp_edittext)
    EditText otpEditText;
    @BindView(R.id.timer_textview)
    TextView timerTextView;
    @BindView(R.id.resend_textview)
    TextView resendTextView;
    @BindView(R.id.loader_layout)
    View loaderLayout;
    @BindView(R.id.submit_button)
    Button submitButton;
    @BindView(R.id.pinView)
    PinView pinView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private CountDownTimer countDownTimer;
    private static final String FORMAT = "%02d:%02d";
    private String email;
    private Bundle bundle;
    private int serverOtp;
    RecruiterViewModel recruiterViewModel;
    ApplicantViewModel applicantViewModel;
    private ProgressDialog progressBar;
    private AppPreference preference;
    private Boolean isRecruiter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        preference = new AppPreference(this);
        if (bundle != null) {
            email = bundle.getString("EMAIL");
            isRecruiter = bundle.getBoolean("IS_RECRIUTER",false);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Email Verification");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressBar = new ProgressDialog(this);
        if(isRecruiter) {
            recruiterViewModel = new ViewModelProvider(EmailVerificationActivity.this).get(RecruiterViewModel.class);
        }else{
            applicantViewModel = new ViewModelProvider(EmailVerificationActivity.this).get(ApplicantViewModel.class);
        }
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerTextView.setText("" + String.format(FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            @Override
            public void onFinish() {
                otpEditText.setEnabled(false);
                submitButton.setVisibility(View.GONE);
                timerTextView.setText("Done");
            }
        };

        pinView.setLineColor(getResources().getColor(R.color.grey));
        pinView.setCursorVisible(true);
        pinView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                pinView.setText(null);
            }
        });

        sendVerificationCode(email);
    }

    private void sendVerificationCode(String email) {
        loaderLayout.setVisibility(View.VISIBLE);
        Random rnd = new Random();
        serverOtp = 100000 + rnd.nextInt(900000);
        String textMessage = "Your verification otp is ->" + serverOtp;

        GMailSender.withAccount("adity5172@gmail.com", "Poonam@7")
                .withTitle("VihaTesApp")
                .withBody(textMessage)
                .withSender(getString(R.string.app_name))
                .toEmailAddress(email) // one or multiple addresses separated by a comma
                .withListenner(new GmailListener() {
                    @Override
                    public void sendSuccess() {
                        loaderLayout.setVisibility(View.GONE);
                        countDownTimer.start();
                        Toast.makeText(EmailVerificationActivity.this, "Success", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void sendFail(String err) {
                        loaderLayout.setVisibility(View.GONE);
                        Toast.makeText(EmailVerificationActivity.this, "Fail: " + err, Toast.LENGTH_SHORT).show();
                    }
                })
                .send();
    }

    @OnClick({R.id.submit_button, R.id.resend_textview})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_button:
                try {
                    loaderLayout.setVisibility(View.GONE);
                    String userotp = pinView.getText().toString().trim();

                    if (userotp.isEmpty() || userotp.length() < 6) {
                        pinView.setError("Enter valid code");
                        pinView.requestFocus();
                        return;
                    } else {
                        //verifying the code entered manually
                        verifyVerificationCode(userotp);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.resend_textview:
                sendVerificationCode(email);
                break;
        }
    }

    private void verifyVerificationCode(String userotp) {
        if (userotp.equals(String.valueOf(serverOtp))) {
            if(isRecruiter) {
                addDatatOnDatatbase();
            }else{
                AddApplicantDataOnDatatbase();
            }
        } else {
            String message = "Otp not match";
            Toast.makeText(EmailVerificationActivity.this, message, Toast.LENGTH_SHORT).show();
        }
    }

    private void AddApplicantDataOnDatatbase() {
        ApplicantData applicantData=new ApplicantData();
        applicantData.setFirstname(bundle.getString("NAME"));
        applicantData.setLastname(bundle.getString("LAST_NAME"));
        applicantData.setEmail(bundle.getString("EMAIL"));
        applicantData.setPhone(bundle.getString("MOBILE"));
        applicantData.setPassword(bundle.getString("PASSWORD"));
        applicantData.setProfiletype(bundle.getInt("PROFILE_TYPE"));
        applicantData.setExperience(bundle.getString("EXPERIANCE"));
        applicantData.setPayment_shift(bundle.getDouble("PAYMENTSHIFT"));
        applicantData.setLocation(bundle.getString("LOCATION"));

        applicantViewModel.insertApplicantData(applicantData, DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .recruiterDao());
        applicantViewModel.isInsert().observe(this, new Observer<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(Long aLong) {
                if(aLong>0){
                    Toast.makeText(EmailVerificationActivity.this, "Inserted Successfully.", Toast.LENGTH_SHORT).show();
                    preference.setApplicantUserId(Math.toIntExact(aLong));
                    preference.setFirstName(applicantData.getFirstname());
                    preference.setLastName(applicantData.getLastname());
                    preference.setEmail(applicantData.getEmail());
                    preference.setPhone(applicantData.getPhone());
                    preference.setProfileType(applicantData.getProfiletype());
                    preference.setPassword(applicantData.getPassword());
                    Intent i= new Intent(EmailVerificationActivity.this,ApplicantDashboardActivity.class);
                    startActivity(i);
                    finish();
                }else if(aLong==0){
                    Toast.makeText(EmailVerificationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

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
    }

    private void addDatatOnDatatbase() {
        RecruiterData recruiterData=new RecruiterData();
        recruiterData.setFirstname(bundle.getString("NAME"));
        recruiterData.setLastname(bundle.getString("LAST_NAME"));
        recruiterData.setEmail(bundle.getString("EMAIL"));
        recruiterData.setPhone(bundle.getString("MOBILE"));
        recruiterData.setPassword(bundle.getString("PASSWORD"));
        recruiterData.setProfiletype(bundle.getInt("PROFILE_TYPE",0));

        recruiterViewModel.insertRecruiterData(recruiterData, DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .recruiterDao());
        recruiterViewModel.isInsert().observe(this, new Observer<Long>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(Long aLong) {
                if(aLong>0){
                    Toast.makeText(EmailVerificationActivity.this, "Inserted Successfully.", Toast.LENGTH_SHORT).show();
                    preference.setUserId(Math.toIntExact(aLong));
                    preference.setFirstName(recruiterData.getFirstname());
                    preference.setLastName(recruiterData.getLastname());
                    preference.setEmail(recruiterData.getEmail());
                    preference.setPhone(recruiterData.getPhone());
                    preference.setProfileType(recruiterData.getProfiletype());
                    preference.setPassword(recruiterData.getPassword());
                    Intent i= new Intent(EmailVerificationActivity.this,RecruiterDashboardActivity.class);
                    startActivity(i);
                    finish();
                }else if(aLong==0){
                    Toast.makeText(EmailVerificationActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
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
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null)
            countDownTimer.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkManager.isConnected(this)) {
            Intent i = new Intent(this, InternetActivity.class);
            startActivityForResult(i, INTERNET_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == INTERNET_REQUEST_CODE) {
            sendVerificationCode(email);
        }
    }

}