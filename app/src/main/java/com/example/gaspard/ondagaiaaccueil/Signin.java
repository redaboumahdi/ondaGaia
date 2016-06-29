package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Signin extends AppCompatActivity {
    Button submit;
    TextView pseudoT, first_nameT, last_nameT,passwordT, passwordbisT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        pseudoT = (TextView) findViewById(R.id.PSEUDO2);
        first_nameT = (TextView) findViewById(R.id.FIRSTNAME);
        last_nameT = (TextView) findViewById(R.id.LASTNAME);
        passwordT = (TextView) findViewById(R.id.PASSWORD);
        passwordbisT = (TextView) findViewById(R.id.PASSWORDBIS);
        submit=(Button)findViewById(R.id.Submit_and_Connect);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnSignin(v);
            }
        });
    }

    public void OnSignin(View view){
        String pseudo = pseudoT.getText().toString();
        String first_name = first_nameT.getText().toString();
        String last_name = last_nameT.getText().toString();
        String password = passwordT.getText().toString();
        String passwordbis = passwordbisT.getText().toString();
        BWSignin backgroundWorker = new BWSignin(this);
        backgroundWorker.execute(pseudo,first_name,last_name,password,passwordbis);

    }
}

//CHECK