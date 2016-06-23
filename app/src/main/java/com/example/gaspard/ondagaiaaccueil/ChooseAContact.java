package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


public class ChooseAContact extends AppCompatActivity {

    ListView mListView3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueilchooseacontact);
        final String []friends=getIntent().getStringArrayExtra("listofcontact");
        mListView3 = (ListView) findViewById(R.id.listView3);

        final String idme=getIntent().getExtras().getString("myID");
        final String []listofID=getIntent().getStringArrayExtra("listofID");
        final String picturepath=getIntent().getExtras().getString("picture");
        System.out.println(idme);

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
                i.putExtra(name1, picturepath);
                i.putExtra(name2, idme);
                i.putExtra(name3,listofID[position]);
                startActivity(i);
            }
        });
    }
}
