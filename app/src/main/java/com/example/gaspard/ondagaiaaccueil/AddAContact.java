package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddAContact extends AppCompatActivity {

    TextView contact;
    Button add;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueiladdacontact);

        final String []friends=getIntent().getStringArrayExtra("listofcontact");

        Bundle extras = getIntent().getExtras();
        final String idme=extras.getString("myID");

        System.out.println(idme);

        contact = (TextView) findViewById(R.id.IDContact);
        add=(Button)findViewById(R.id.AddContact);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddingContact(v);
            }
        });

    }
    public void AddingContact(View view){
        String IDcontact = contact.getText().toString();
        BWAddContact backgroundWorker = new BWAddContact(this);
        Bundle extras = getIntent().getExtras();
        final String idme=extras.getString("myID");
        backgroundWorker.execute(idme,IDcontact);

    }

}
