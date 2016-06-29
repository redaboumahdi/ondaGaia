package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Contacts extends AppCompatActivity {

    Button addcontact;
    Button waitingrequest;
    ListView mListView;
    Button Home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);
        final String []friends=getIntent().getStringArrayExtra("listofcontact");
        ArrayList<String> friends2=new ArrayList<String>();
        for (int i=0;i<friends.length;i++){
            friends2.add(friends[i]);
        }
        Collections.sort(friends2);
        for (int i=0;i<friends.length;i++){
            friends[i]=friends2.get(i);
        }
        mListView = (ListView) findViewById(R.id.listView);

        Bundle extras1 = getIntent().getExtras();
        final String myID=extras1.getString("myID");
        System.out.println(myID);

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