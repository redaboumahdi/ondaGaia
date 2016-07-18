package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Chat2 extends AppCompatActivity {
    private TextView txt;
    private Button button;
    private EditText etxt;
    private Intent i;
    private printMessage pm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat2);
        i = getIntent();
        Toast.makeText(getApplicationContext(), "selected Item Name is " + i.getStringExtra("id_conversation"), Toast.LENGTH_LONG).show();
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendMessage s = new SendMessage(Chat2.this,etxt, i.getStringExtra("id_conversation"));
                s.start();
            }
        });

        etxt = (EditText) findViewById(R.id.editText);
        txt = (TextView) findViewById(R.id.textView);
        txt.setMovementMethod(new ScrollingMovementMethod());


        pm = new printMessage(Chat2.this, txt, i.getStringExtra("id_conversation"));
        pm.start();
    }
    @Override
    public void onBackPressed() {
        pm.flag = 1;
        super.onBackPressed();
    }

}

