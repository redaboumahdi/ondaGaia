package com.example.gaspard.ondagaiaaccueil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class AccueilContacts extends AppCompatActivity {

    Button addcontact;
    Button waitingrequest;
    ListView mListView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueilcontact);
        final String []friends=getIntent().getStringArrayExtra("listofcontact");
        mListView = (ListView) findViewById(R.id.listView);

        Bundle extras1 = getIntent().getExtras();
        final String idme=extras1.getString("myID");


        //System.out.println(friends);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AccueilContacts.this,
                android.R.layout.simple_list_item_1, friends);
        mListView.setAdapter(adapter);

        addcontact=(Button)findViewById(R.id.Add);
        addcontact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(AccueilContacts.this, AddAContact.class);
                String name2= "myID";
                String name1= "listofcontact";
                intent.putExtra(name1, friends);
                intent.putExtra(name2, idme);
                startActivity(intent);
            }
        });

        waitingrequest=(Button)findViewById(R.id.WaitingRequest);
        waitingrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                BWWaitingRequest backgroundWorker = new BWWaitingRequest(AccueilContacts.this);
                backgroundWorker.execute(idme);
            }
        });
    }
}
