package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Login extends AppCompatActivity {
    Button connect;
    TextView idT,passwordT;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        idT = (TextView) findViewById(R.id.PSEUDO1);
        passwordT = (TextView) findViewById(R.id.PASSWORD);
        connect=(Button)findViewById(R.id.Connect);
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnLogin(v);
            }
        });
    }
    public void OnLogin(View view){
        String ident = idT.getText().toString();
        String password = passwordT.getText().toString();
        BWLogin backgroundWorker = new BWLogin(this);
        backgroundWorker.execute(ident,password);

    }
}

//CHECK