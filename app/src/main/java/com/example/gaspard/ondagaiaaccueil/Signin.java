package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Signin extends AppCompatActivity {
    Button buttonsubmit;
    TextView idT, first_nameT, last_nameT,passwordT, passwordbisT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        idT = (TextView) findViewById(R.id.IDENTIFICATION2);
        first_nameT = (TextView) findViewById(R.id.FIRSTNAME);
        last_nameT = (TextView) findViewById(R.id.LASTNAME);
        passwordT = (TextView) findViewById(R.id.PASSWORD);
        passwordbisT = (TextView) findViewById(R.id.PASSWORDBIS);
        buttonsubmit=(Button)findViewById(R.id.Submit_and_Connect);
        buttonsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSignin(v);
                //Intent intent=new Intent(Signin.this,Accueil.class);
                //startActivity(intent);
            }
        });
    }

    public void OnSignin(View view){

        String ident = idT.getText().toString();
        String first_name = first_nameT.getText().toString();
        String last_name = last_nameT.getText().toString();
        String password = passwordT.getText().toString();
        String passwordbis = passwordbisT.getText().toString();
        BWsignin backgroundWorker = new BWsignin(this);
        backgroundWorker.execute(ident,first_name,last_name,password,passwordbis);

    }
}
