package com.example.gaspard.ondagaiaaccueil;

import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ArrayAdapter;
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
import android.view.ViewGroup;
import android.content.Context;


public class Contacts extends AppCompatActivity {

    Button addcontact;
    ListView mListView;
    Button Home;

    public void getNumber(ContentResolver cr, ArrayList<String> phoneNumber) {
        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null,null, null);
        while (phones.moveToNext())
        {
            phoneNumber.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        final String myID=((GlobalVar)this.getApplication()).getmyID();
        final String []friends=getIntent().getStringArrayExtra("listofcontact");
        final String []Waitingfriends=getIntent().getStringArrayExtra("waitingrequest");
        final String []WaitingfriendsID=getIntent().getStringArrayExtra("waitingrequestID");
        final String []allphonenumbers=getIntent().getStringArrayExtra("phonenumbers");

        final ArrayList<String>waitingfriends=new ArrayList<>();
        final ArrayList<String>waitingfriendsID=new ArrayList<>();

        for (int i=0;i<Waitingfriends.length;i++){
            waitingfriends.add(Waitingfriends[i]);
            waitingfriendsID.add(WaitingfriendsID[i]);
        }

        ArrayList<String> friends2=new ArrayList<String>();
        for (int i=0;i<friends.length;i++){
            friends2.add(friends[i]);
        }
        Collections.sort(friends2);
        for (int i=0;i<friends.length;i++){
            friends[i]=friends2.get(i);
        }

        ArrayList<String> phoneNumber=new ArrayList<String>();
        getNumber(this.getContentResolver(),phoneNumber);

        final ArrayList<String> phonenumberwaitingfriend= new ArrayList<String>();

        for (int i=0;i<phoneNumber.size();i++){
            for (int j=0;j<allphonenumbers.length;j++){
                if (PhoneNumberUtils.compare(phoneNumber.get(i),allphonenumbers[j])){
                    phonenumberwaitingfriend.add(allphonenumbers[j]);
                }
            }
        }

        for (int i=0;i<phonenumberwaitingfriend.size();i++){
            final int finalI = i;
            Thread thread1=new Thread(){
                public void run(){
                    String result="";
                    try {
                        String getidfromphone_url = "http://192.168.8.102:8888/getidfromphone.php";
                        URL url = new URL(getidfromphone_url);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setDoInput(true);
                        OutputStream outputStream = httpURLConnection.getOutputStream();
                        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                        String getidfromphone_data = URLEncoder.encode("phone", "UTF-8") + "=" + URLEncoder.encode(phonenumberwaitingfriend.get(finalI), "UTF-8")+ "&"
                                + URLEncoder.encode("myID", "UTF-8") + "=" + URLEncoder.encode(myID, "UTF-8");
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

                    StringTokenizer st = new StringTokenizer(result, ";;;");
                    while (st.hasMoreElements()) {
                        String names="";
                        try {
                            org.json.JSONObject JSON = new org.json.JSONObject(st.nextToken());
                            names =((String)JSON.get("first_name")) + " " + ((String)JSON.get("last_name"));
                            waitingfriends.add(names);
                            waitingfriendsID.add((String)JSON.get("idR"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

            };
            thread1.start();
            try {
                thread1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        ArrayList<ArrayList<String>> waitingfriends2=new ArrayList<ArrayList<String>>();
        for (int i=0;i<waitingfriends.size();i++){
            waitingfriends2.add(new ArrayList<String>(Arrays.asList(waitingfriends.get(i), waitingfriendsID.get(i))));
        }


        Collections.sort(waitingfriends2, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                return o1.get(0).compareTo(o2.get(0));
            }
        });

        final String[]WaitingFriends=new String[waitingfriends.size()];
        final String[]WaitingFriendsID=new String[waitingfriends.size()];
        for (int i=0;i<waitingfriends.size();i++){
            WaitingFriends[i]=waitingfriends2.get(i).get(0);
            WaitingFriendsID[i]=waitingfriends2.get(i).get(1);
        }

        String[] AllFriends= new String[WaitingFriends.length+friends.length];
        for (int i=0;i<WaitingFriends.length;i++){
            AllFriends[i]=WaitingFriends[i];
        }
        for (int i=0;i<friends.length;i++){
            AllFriends[WaitingFriends.length+i]=friends[i];
        }

        mListView = (ListView) findViewById(R.id.listView);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Contacts.this,
                android.R.layout.simple_list_item_1, AllFriends){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                if (position < WaitingFriends.length) {
                    view.setBackgroundColor(Color.RED);
                } else {
                    view.setBackgroundColor(Color.BLUE);
                }
                return view;
            }
        };

        mListView.setAdapter(adapter);

        final Context c=this.getApplicationContext();
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                if (position<WaitingFriends.length) {
                    BWAddAWaitingRequest backgroundWorker = new BWAddAWaitingRequest(c);
                    backgroundWorker.execute(myID, WaitingFriendsID[position]);
                }
            }
        });

        addcontact=(Button)findViewById(R.id.Add);
        addcontact.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                Intent intent = new Intent(Contacts.this, AddAContact.class);
                startActivity(intent);
            }
        });

        Home = (Button) findViewById(R.id.home2);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome bw=new BWHome(Contacts.this);
                bw.execute(myID);
            }
        });
    }
}

//CHECK