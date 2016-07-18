package com.example.gaspard.ondagaiaaccueil;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Signin extends AppCompatActivity {
    Button submit;
    TextView pseudoT, first_nameT, last_nameT,passwordT, passwordbisT,phoneT,mailT;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        String result="";
        try {
            result = getIntent().getExtras().getString("result");
        }
        catch (NullPointerException e){

        }
        if (result.equals("This ID already exists!")){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage(result);
            alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.cancel();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        pseudoT = (TextView) findViewById(R.id.PSEUDO2);
        first_nameT = (TextView) findViewById(R.id.FIRSTNAME);
        last_nameT = (TextView) findViewById(R.id.LASTNAME);
        passwordT = (TextView) findViewById(R.id.PASSWORD);
        phoneT = (TextView) findViewById(R.id.PHONE);
        mailT = (TextView) findViewById(R.id.MAIL);
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
        String phone = phoneT.getText().toString();
        String mail = mailT.getText().toString();
        BWSignin backgroundWorker = new BWSignin(this.getApplicationContext());
        backgroundWorker.execute(pseudo,first_name,last_name,password,passwordbis,phone,mail);

    }
}

//CHECK