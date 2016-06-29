package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class Contacts extends AppCompatActivity {

    Button addcontact;
    Button waitingrequest;
    ListView mListView;
    Button Home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        final String []friends=getIntent().getStringArrayExtra("listofcontact");
        mListView = (ListView) findViewById(R.id.listView);

        Bundle extras1 = getIntent().getExtras();
        final String myID=extras1.getString("myID");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Contacts.this,
                android.R.layout.simple_list_item_1, friends);
        mListView.setAdapter(adapter);

        addcontact=(Button)findViewById(R.id.Add);
        addcontact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(Contacts.this, AddAContact.class);
                String name= "myID";
                intent.putExtra(name, myID);
                startActivity(intent);
            }
        });

        waitingrequest=(Button)findViewById(R.id.WaitingRequest);
        waitingrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                BWWaitingRequest backgroundWorker = new BWWaitingRequest(Contacts.this);
                backgroundWorker.execute(myID);
            }
        });


        Home = (Button) findViewById(R.id.home2);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome backgroundWorker = new BWHome(Contacts.this);
                backgroundWorker.execute(myID);
            }
        });
    }
}

//CHECK