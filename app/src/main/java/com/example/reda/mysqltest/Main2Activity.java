package com.example.reda.mysqltest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {
    private static Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        OnClickButtonListener();
    }

    public void OnClickButtonListener(){
        button = (Button) findViewById(R.id.button4);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent("com.example.reda.mysqltest.Chat");
                startActivity(intent);
            }

        });



    }


}
