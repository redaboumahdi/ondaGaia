package com.example.gaspard.ondagaiaaccueil;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.lang.reflect.Array;

public class WaitingRequest extends AppCompatActivity {
    ListView mListView2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueilwaitingrequest);

        final String[] waitingfriends = getIntent().getStringArrayExtra("listofcontact");
        final String[] waitingfriendsID = getIntent().getStringArrayExtra("listofID");
        Bundle extras = getIntent().getExtras();
        final String idme = extras.getString("myID");

        mListView2 = (ListView) findViewById(R.id.listView2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(WaitingRequest.this,
                android.R.layout.simple_list_item_1, waitingfriends);
        mListView2.setAdapter(adapter);
        mListView2.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView2.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //System.out.println("caca");
                BWAddAWaitingRequest backgroundWorker = new BWAddAWaitingRequest(WaitingRequest.this);
                backgroundWorker.execute(idme,waitingfriendsID[position]);
            }
        });

    }

}
