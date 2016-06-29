package com.example.gaspard.ondagaiaaccueil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class WaitingRequest extends AppCompatActivity {
    ListView mListView2;
    Button Back;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waitingrequest);

        final String[] waitingfriends = getIntent().getStringArrayExtra("listofcontact");
        final String[] waitingfriendsID = getIntent().getStringArrayExtra("listofID");
        Bundle extras = getIntent().getExtras();
        final String myID = extras.getString("myID");

        ArrayList<ArrayList<String>> waitingfriends2=new ArrayList<ArrayList<String>>();
        for (int i=0;i<waitingfriends.length;i++){
            waitingfriends2.add(new ArrayList<String>(Arrays.asList(waitingfriends[i], waitingfriendsID[i])));
        }

        Collections.sort(waitingfriends2, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });

        for (int i=0;i<waitingfriends.length;i++){
            waitingfriends[i]=waitingfriends2.get(i).get(0);
            waitingfriendsID[i]=waitingfriends2.get(i).get(1);
        }

        mListView2 = (ListView) findViewById(R.id.listView2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WaitingRequest.this,
                android.R.layout.simple_list_item_1, waitingfriends);
        mListView2.setAdapter(adapter);
        mListView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                BWAddAWaitingRequest backgroundWorker = new BWAddAWaitingRequest(WaitingRequest.this);
                backgroundWorker.execute(myID,waitingfriendsID[position]);
            }
        });

        Back = (Button) findViewById(R.id.back);
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWContact backgroundWorker = new BWContact(WaitingRequest.this);
                backgroundWorker.execute(myID);
            }
        });

    }

}


//CHECK