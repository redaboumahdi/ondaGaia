package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class LoginSignin extends AppCompatActivity {

    Button Login;
    Button Signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsignin);


        Login=(Button)findViewById(R.id.Login);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginSignin.this,Login.class);
                startActivity(intent);
            }
        });

        Signin=(Button)findViewById(R.id.Signin);
        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginSignin.this,Signin.class);
                startActivity(intent);
            }
        });
    }
}

//CHECK