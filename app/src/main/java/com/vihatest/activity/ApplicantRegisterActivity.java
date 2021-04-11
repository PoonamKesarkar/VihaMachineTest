package com.vihatest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.vihatest.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicantRegisterActivity extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {
    @BindView(R.id.edtFirstName)
    EditText edtFirstName;
    @BindView(R.id.edtLastName)
    EditText edtLastName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtMobile)
    EditText edtMobile;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtRetypePassword)
    EditText edtRetypePassword;

    @BindView(R.id.spinnerProfileType)
    Spinner spinnerProfileType;
    @BindView(R.id.edtExperiance)
    EditText edtExperiance;
    @BindView(R.id.edtPaymentShift)
    EditText edtPaymentShift;
    @BindView(R.id.edtLocation)
    EditText edtLocation;

    @BindView(R.id.btn_signUp)
    Button btn_signUp;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    private Double payment=0.0;
    int profileType=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_register);
        ButterKnife.bind(this);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.profile_type_array)); //Your resource name

        spinnerProfileType.setAdapter(arrayAdapter);
        spinnerProfileType.setOnItemSelectedListener(this);
        btn_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString().trim();
                String password = edtPassword.getText().toString().trim();
                String name = edtFirstName.getText().toString().trim();
                String lastname = edtLastName.getText().toString().trim();
                String mobileNo = edtMobile.getText().toString().trim();
                String experiance = edtExperiance.getText().toString().trim();
                String paymentShift = edtPaymentShift.getText().toString().trim();
                if(paymentShift.length()>0){
                    payment=Double.parseDouble(paymentShift);
                }
                String location = edtLocation.getText().toString().trim();
                if (validateName() && validateLastName() && validateMobile() && validateEmailAddress() && validatePassword() && validateCfmPassword()
                        && validateProfileType()&& validateExperience() && validatePaymentShift()&&validateLocation()) {
                    Bundle signup_bundle = new Bundle();
                    signup_bundle.putInt("PROFILE_TYPE", profileType);
                    signup_bundle.putString("PASSWORD", password);
                    signup_bundle.putString("NAME", name);
                    signup_bundle.putString("LAST_NAME", lastname);
                    signup_bundle.putString("EMAIL", email);
                    signup_bundle.putString("MOBILE", mobileNo);
                    signup_bundle.putString("EXPERIANCE", experiance);
                    signup_bundle.putDouble("PAYMENTSHIFT", payment);
                    signup_bundle.putString("LOCATION", location);
                    signup_bundle.putBoolean("IS_RECRIUTER", false);
                    Intent intent = new Intent(ApplicantRegisterActivity.this, EmailVerificationActivity.class);
                    intent.putExtras(signup_bundle);
                    startActivity(intent);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ApplicantRegisterActivity.this, ApplicantLoginActivity.class);
                startActivity(mainIntent);
            }
        });
    }
    private boolean validateMobile() {
        String mobile = edtMobile.getText().toString().trim();
        edtMobile.setError(null);
        if (mobile.length() != 10) {
            edtMobile.setError("Please enter proper mobile number");
            edtMobile.requestFocus();
            return false;
        } else {
            edtMobile.setError(null);
            return true;
        }
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
            edtPassword.setError("Password length should be greater than 8");
            edtPassword.requestFocus();
            return false;
        }else if (!matcher.matches()) {
            edtPassword.setError("Password is a 8 digit combination of upper case, lower case and number");
            edtPassword.requestFocus();
            return false;
        }else {
            edtPassword.setError(null);
            return true;
        }
    }

    private boolean validateCfmPassword() {
        String cfmPassword = edtRetypePassword.getText().toString();
        String password = edtPassword.getText().toString();
        edtRetypePassword.setError(null);
        if (cfmPassword.contains(" ")) {
            edtRetypePassword.setError("Space not allowed in password");
            edtRetypePassword.requestFocus();
            return false;
        }
        if (!password.equals(cfmPassword)) {
            edtRetypePassword.setError("Password does not match");
            edtRetypePassword.requestFocus();
            return false;
        } else {
            edtRetypePassword.setError(null);
            return true;
        }
    }

    private boolean validateName() {
        String name = edtFirstName.getText().toString().trim();
        edtFirstName.setError(null);
        if (name.length() < 2) {
            edtFirstName.setError("Please enter proper first name");
            edtFirstName.requestFocus();
            return false;
        } else {
            edtFirstName.setError(null);
            return true;
        }
    }

    private boolean validateLastName() {
        String lastname = edtLastName.getText().toString().trim();
        edtLastName.setError(null);
        if (lastname.length() < 2) {
            edtLastName.setError("Please enter proper first nam");
            edtLastName.requestFocus();
            return false;
        } else {
            edtLastName.setError(null);
            return true;
        }
    }
    private boolean validateProfileType() {

        if (spinnerProfileType.getSelectedItemPosition()== 0) {
            Toast.makeText(this, "Please select profile type.", Toast.LENGTH_SHORT).show();
            spinnerProfileType.requestFocus();
            return false;
        } else {
            return true;
        }
    }


    private boolean validateExperience() {

        String experience = edtExperiance.getText().toString().trim();
        edtExperiance.setError(null);
        if (experience.length() <= 0) {
            edtExperiance.setError("Please enter experience");
            edtExperiance.requestFocus();
            return false;
        } else {
            edtExperiance.setError(null);
            return true;
        }
    }

    private boolean validateLocation() {

        String experience = edtLocation.getText().toString().trim();
        edtLocation.setError(null);
        if (experience.length() <= 0) {
            edtLocation.setError("Please enter location");
            edtLocation.requestFocus();
            return false;
        } else {
            edtLocation.setError(null);
            return true;
        }
    }
    private boolean validatePaymentShift() {

        String payment = edtPaymentShift.getText().toString().trim();
        edtPaymentShift.setError(null);
        if (payment.length() <= 0) {
            edtPaymentShift.setError("Please enter payment");
            edtPaymentShift.requestFocus();
            return false;
        } else {
            edtPaymentShift.setError(null);
            return true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        profileType=position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}