package com.bungeee.android.swapi.ui;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bungeee.android.swapi.ui.list.MainActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent MainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(MainActivityIntent);

    }
}
