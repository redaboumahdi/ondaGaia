package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;


public class ChooseAContact extends AppCompatActivity {

    ListView mListView3;
    Button Home;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseacontact);
        final String []friends=getIntent().getStringArrayExtra("listofcontact");
        mListView3 = (ListView) findViewById(R.id.listView3);

        final String myID=getIntent().getExtras().getString("myID");
        final String []listofID=getIntent().getStringArrayExtra("listofID");
        final String picturepath=getIntent().getExtras().getString("picture");
        final String orientation=getIntent().getExtras().getString("orientation");

        ArrayList<ArrayList<String>> friends2=new ArrayList<ArrayList<String>>();
        for (int i=0;i<friends.length;i++){
            friends2.add(new ArrayList<String>(Arrays.asList(friends[i], listofID[i])));
        }

        Collections.sort(friends2, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });

        for (int i=0;i<friends.length;i++){
            friends[i]=friends2.get(i).get(0);
            listofID[i]=friends2.get(i).get(1);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChooseAContact.this,
                android.R.layout.simple_list_item_1, friends);
        mListView3.setAdapter(adapter);
        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                Intent i= new Intent(ChooseAContact.this, ChooseAPlace.class);
                String name1= "picture";
                String name2="myID";
                String name3="IDfriend";
                String name4="orientation";
                i.putExtra(name1, picturepath);
                i.putExtra(name2, myID);
                i.putExtra(name3,listofID[position]);
                i.putExtra(name4,orientation);
                startActivity(i);
            }
        });

        Home = (Button) findViewById(R.id.home5);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome backgroundWorker = new BWHome(ChooseAContact.this);
                backgroundWorker.execute(myID);
            }
        });
    }
}

//CHECK