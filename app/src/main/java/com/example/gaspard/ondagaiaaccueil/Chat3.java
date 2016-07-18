package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Chat3 extends AppCompatActivity {
    private ListView ls;
    private Button button4;
    private EditText et;

    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String myID=((GlobalVar)this.getApplication()).getmyID();
        setContentView(R.layout.chat3);
        GlobalVar u = (GlobalVar) getApplication();
        ls = (ListView) findViewById(R.id.exp1);

        final String[][] aaResponse = {new String[0]};
        final ArrayList<String> friendsnames=new ArrayList<>();
        final ArrayList<String> friendsID=new ArrayList<>();
        Thread thread = new Thread(){
            public void run(){
                String result="";
                try {
                    String getidfromphone_url = "http://192.168.8.102:8888/idfriends.php";
                    URL url = new URL(getidfromphone_url);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    OutputStream outputStream = httpURLConnection.getOutputStream();
                    BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                    String getidfromphone_data =URLEncoder.encode("myID", "UTF-8") + "=" + URLEncoder.encode(myID, "UTF-8");
                    bufferedWriter.write(getidfromphone_data);
                    bufferedWriter.flush();
                    bufferedWriter.close();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                    String line = "";
                    while ((line = bufferedReader.readLine()) != null) {
                        result += line;
                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ArrayList<String> idfriends=new ArrayList<>();
                StringTokenizer st = new StringTokenizer(result, ";;;");
                while (st.hasMoreElements()) {
                    String names="";
                    try {
                        org.json.JSONObject JSON = new org.json.JSONObject(st.nextToken());
                        names = ((String) JSON.get("first_name")) + " " + ((String) JSON.get("last_name"));
                        friendsnames.add(names);
                        friendsID.add((String)JSON.get("idR"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final ArrayList<ArrayList<String>> friends=new ArrayList<ArrayList<String>>();
        for (int i=0;i<friendsnames.size();i++){
            friends.add(new ArrayList<String>(Arrays.asList(friendsnames.get(i), friendsID.get(i))));
        }

        Collections.sort(friends, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });

        String[]Friendsnames=new String[friendsnames.size()];
        String[]FriendsID=new String[friendsID.size()];
        for (int i=0;i<friends.size();i++){
            Friendsnames[i]=friends.get(i).get(0);
            FriendsID[i]=friends.get(i).get(1);
        }

        this.adapter = new ArrayAdapter<String>(this,R.layout.contact_listview,Friendsnames);
        ls.setAdapter(adapter);

        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick (AdapterView< ? > adapter, View view, int position, long arg){
                TextView v = (TextView) view.findViewById(R.id.label1);
                String res = v.getText().toString();
                String idres="";
                for (int i=0;i<friendsnames.size();i++){
                    if (friends.get(i).get(0).equals(res)){
                        idres=friends.get(i).get(1);
                    }
                }
                Toast.makeText(getApplicationContext(), "selected Item Name is " + res, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Chat3.this, Chat2.class);
                intent.putExtra("id_conversation",idres);
                startActivity(intent);
            }
        });
        button4 = (Button) findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat3.this, Chat.class);
                startActivity(intent);
            }
        });
        et = (EditText) findViewById(R.id.editText2);
        et.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Chat3.this.adapter.getFilter().filter(cs);
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

        });
    }

}
