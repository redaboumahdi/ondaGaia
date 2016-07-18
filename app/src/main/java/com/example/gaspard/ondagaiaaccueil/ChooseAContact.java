package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.content.ClipData.Item;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import android.graphics.Color;
import android.widget.EditText;
import android.text.TextWatcher;
import android.text.Editable;

public class ChooseAContact extends AppCompatActivity {

    ListView mListView3;
    Button Home,Place;
    EditText inputSearch;
    ArrayAdapter<String> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chooseacontact);
        mListView3 = (ListView) findViewById(R.id.listView3);

        final String picturepath=getIntent().getExtras().getString("picturepath");
        final String orientation=getIntent().getExtras().getString("orientation");
        final String [] friends=getIntent().getExtras().getStringArray("listofcontact");
        final String [] listofID=getIntent().getExtras().getStringArray("listofID");


        ArrayList<ArrayList<String>> friends2=new ArrayList<ArrayList<String>>();
        for (int i=0;i<listofID.length;i++){
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

        inputSearch = (EditText) findViewById(R.id.inputSearch);

        final ArrayList<ArrayList<String>>friendsposition=new ArrayList<ArrayList<String>>();
        for (int i=0;i<friends.length;i++) {
            friendsposition.add(new ArrayList<String>(Arrays.asList(friends[i], Integer.toString(i))));
        }

        final ArrayList<String>positionsselected=new ArrayList<>();
        final ArrayList<String>friendsselected=new ArrayList<>();

        adapter = new ArrayAdapter<String>(ChooseAContact.this, R.layout.list_item, R.id.contact_name, friends){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    for (int i=0;i<positionsselected.size();i++){
                        if (Integer.parseInt(positionsselected.get(i))==position){
                            view.setBackgroundColor(Color.BLUE);
                        }
                    }
                    return view;
                }
        };
        mListView3.setAdapter(adapter);

        final ArrayList<String> IDselected=new ArrayList<String>();

        mListView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                String s=(String)arg0.getItemAtPosition(position);
                friendsselected.add(s);
                String ss="";
                for (int i=0;i<friendsposition.size();i++){
                    if (friendsposition.get(i).get(0).equals(s)){
                        ss=friendsposition.get(i).get(1);
                        positionsselected.add(ss);
                    }
                }
                IDselected.add(listofID[Integer.parseInt(ss)]);
            }
        });


        Home = (Button) findViewById(R.id.home5);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ChooseAContact.this,Home.class);
                startActivity(i);
            }
        });



        Place = (Button) findViewById(R.id.place);
        Place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[]selectedID=new String[IDselected.size()];
                for(int i=0;i<IDselected.size();i++){
                    selectedID[i]=IDselected.get(i);
                }
                System.out.println(selectedID[0]);
                Intent i= new Intent(ChooseAContact.this, ChooseAPlace.class);
                String name1= "picture";
                String name3="IDselected";
                String name4="orientation";
                i.putExtra(name1, picturepath);
                i.putExtra(name3,selectedID);
                i.putExtra(name4,orientation);
                startActivity(i);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ChooseAContact.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });
    }
}

//CHECK