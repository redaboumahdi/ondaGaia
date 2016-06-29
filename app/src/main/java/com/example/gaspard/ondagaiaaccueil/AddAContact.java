package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddAContact extends AppCompatActivity {

    TextView contact;
    Button add;
    Button Back;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addacontact);

        Bundle extras = getIntent().getExtras();
        final String myID = extras.getString("myID");

        contact = (TextView) findViewById(R.id.PseudoContact);
        add=(Button)findViewById(R.id.AddContact);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddingContact(v);
            }
        });

        Back = (Button) findViewById(R.id.back2);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWContact backgroundWorker = new BWContact(AddAContact.this);
                backgroundWorker.execute(myID);
            }
        });

    }
    public void AddingContact(View view){
        String pseudocontact = contact.getText().toString();
        BWAddContact backgroundWorker = new BWAddContact(this);
        Bundle extras = getIntent().getExtras();
        final String myID=extras.getString("myID");
        backgroundWorker.execute(myID,pseudocontact);

    }

}

//CHECK