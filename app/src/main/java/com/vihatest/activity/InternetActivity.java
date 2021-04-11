package com.vihatest.activity;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.vihatest.R;
import com.vihatest.util.NetworkManager;

public class InternetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.try_again_imageview})
    public void onClick(View view) {
        if (view.getId() == R.id.try_again_imageview) {
            if (NetworkManager.isConnected(this)) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        }
    }
}