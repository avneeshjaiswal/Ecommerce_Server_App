package com.example.avneeshjaiswal.ecommerceserver.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.avneeshjaiswal.ecommerceserver.R;

import info.hoang8f.widget.FButton;

public class Splash extends AppCompatActivity {

    FButton login,register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        login = findViewById(R.id.login);
        register = findViewById(R.id.signup);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_login = new Intent(Splash.this,Login.class);
                startActivity(i_login);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i_register = new Intent(Splash.this,Register.class);
                startActivity(i_register);
            }
        });

    }
}
