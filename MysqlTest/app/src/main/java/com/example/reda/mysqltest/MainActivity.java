package com.example.reda.mysqltest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;

import java.net.*;
import java.io.*;

public class MainActivity extends AppCompatActivity {
    TextView usernameT, passwordT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameT = (TextView) findViewById(R.id.identi);
        passwordT = (TextView) findViewById(R.id.password);



    }
    public void OnLogin(View view){

        String username = usernameT.getText().toString();
        String password = passwordT.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type,username,password);

    }
}
