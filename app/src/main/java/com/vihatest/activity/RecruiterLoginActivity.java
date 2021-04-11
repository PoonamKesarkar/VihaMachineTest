package com.vihatest.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vihatest.R;
import com.vihatest.database.DatabaseClient;
import com.vihatest.model.RecruiterData;
import com.vihatest.util.AppPreference;
import com.vihatest.util.CommonMethods;
import com.vihatest.viewmodel.RecruiterViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecruiterLoginActivity extends AppCompatActivity {
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.btn_signUp)
    Button btn_signUp;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.loader_layout)
    View loaderLayout;
    RecruiterViewModel recruiterViewModel;
    private AppPreference preference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_login);
        ButterKnife.bind(this);
        recruiterViewModel = new ViewModelProvider(RecruiterLoginActivity.this).get(RecruiterViewModel.class);
        preference = new AppPreference(this);
    }

    @OnClick({R.id.btn_signUp, R.id.btnLogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signUp:
                Intent mainIntent = new Intent(RecruiterLoginActivity.this, RecruiterRegisterActivity.class);
                startActivity(mainIntent);
                break;
            case R.id.btnLogin:

              //  if (validateEmailAddress() && validatePassword()) {
                    CommonMethods.hideKeyboard(RecruiterLoginActivity.this);
                    recruiterLogin();
               // }
                break;
        }
    }

    private void recruiterLogin() {
        String username = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        recruiterViewModel.loginRecruiterData(username,password, DatabaseClient.getInstance(getApplicationContext()).getAppDatabase()
                .recruiterDao());

        recruiterViewModel.isLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Log.d("onemailLoad", "isLoading aBoolean " + aBoolean);
                if(aBoolean){
                    loaderLayout.setVisibility(View.VISIBLE);
                }else{
                    loaderLayout.setVisibility(View.GONE);
                }

            }
        });

        recruiterViewModel.isLoginData().observe(this, new Observer<RecruiterData>() {
            @Override
            public void onChanged(RecruiterData recruiterData) {
                Log.d("onLogin","recruiterData:"+recruiterData);
                if (recruiterData != null) {
                    preference.setUserId(recruiterData.getId());
                    preference.setFirstName(recruiterData.getFirstname());
                    preference.setLastName(recruiterData.getLastname());
                    preference.setEmail(recruiterData.getEmail());
                    preference.setPhone(recruiterData.getPhone());
                    preference.setProfileType(recruiterData.getProfiletype());
                    preference.setPassword(recruiterData.getPassword());
                    Toast.makeText(RecruiterLoginActivity.this, "LoginSuccessfully", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(RecruiterLoginActivity.this, RecruiterDashboardActivity.class);
                    startActivity(i);
                    finish();
                }else{
                    Toast.makeText(RecruiterLoginActivity.this, "Email and password is not valid", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private boolean validateEmailAddress() {
        String email = edtEmail.getText().toString().trim();
        edtEmail.setError(null);
        if (email.length() > 0) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Please enter proper email");
                edtEmail.requestFocus();
                return false;
            } else {
                edtEmail.setError(null);
                return true;
            }
        } else {
            edtEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = edtPassword.getText().toString();
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        edtPassword.setError(null);
        if (password.length() < 8) {
            Toast.makeText(this, "Email and password is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!matcher.matches()) {
            Toast.makeText(this, "Email and password is not valid", Toast.LENGTH_SHORT).show();
            return false;
        }else {
            return true;
        }
    }

}