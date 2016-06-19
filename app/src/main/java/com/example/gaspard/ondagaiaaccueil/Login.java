package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {
    Button buttonconnect;
    TextView idT,passwordT;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        idT = (TextView) findViewById(R.id.IDENTIFICATION1);
        passwordT = (TextView) findViewById(R.id.PASSWORD);
        buttonconnect=(Button)findViewById(R.id.Connect);
        buttonconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnLogin(v);
                //Intent intent=new Intent(Login.this,Accueil.class);
                //startActivity(intent);
            }
        });
    }
    public void OnLogin(View view){
        String ident = idT.getText().toString();
        String password = passwordT.getText().toString();
        BWlogin backgroundWorker = new BWlogin(this);
        backgroundWorker.execute(ident,password);

    }
}