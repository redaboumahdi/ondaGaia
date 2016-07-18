package com.example.gaspard.ondagaiaaccueil;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import java.util.StringTokenizer;

public class BWContact extends AsyncTask<String,Void,String> {
    Context context;

    BWContact(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String myID = params[0];
            String contact_url = "http://192.168.8.102:8888/listcontacts.php";
            URL url = new URL(contact_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String contact_data = URLEncoder.encode("myID", "UTF-8") + "=" + URLEncoder.encode(myID, "UTF-8");
            bufferedWriter.write(contact_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String result = "";
            String line = "";
            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        StringTokenizer st= new StringTokenizer(result,"!!!");

        ArrayList<String> listofcontactt=new ArrayList<String>();
        ArrayList<String> allphonenumbers = new ArrayList<String>();
        final ArrayList<String> waitingfriendss = new ArrayList<String>();
        final ArrayList<String> waitingfriendssID = new ArrayList<String>();

        String[] phonenumbers = new String[0];
        String[] waitingfriends = new String[0];
        String[] waitingfriendsID = new String[0];
        String[] listofcontact = new String[0];

        String st1 = st.nextToken();
        if(!st1.equals("empty")) {
            StringTokenizer stt1 = new StringTokenizer(st1, ";;;");
            while (stt1.hasMoreElements()) {
                String names = "";
                try {
                    org.json.JSONObject JSON = new org.json.JSONObject(stt1.nextToken());
                    names = ((String) JSON.get("first_name")) + " " + ((String) JSON.get("last_name"));
                    listofcontactt.add(names);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            listofcontact = new String[listofcontactt.size()];

            for (int i = 0; i < listofcontactt.size(); i++) {
                listofcontact[i] = listofcontactt.get(i);
            }
        }

        String st2 = st.nextToken();
        if (!st2.equals("empty")) {
            StringTokenizer stt2 = new StringTokenizer(st2, ";;;");

            while (stt2.hasMoreElements()) {
                String names = "";
                try {
                    org.json.JSONObject JSON = new org.json.JSONObject(stt2.nextToken());
                    names = ((String) JSON.get("first_name")) + " " + ((String) JSON.get("last_name"));
                    waitingfriendss.add(names);
                    waitingfriendssID.add((String) JSON.get("idR"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            waitingfriends = new String[waitingfriendss.size()];
            waitingfriendsID = new String[waitingfriendssID.size()];

            for (int i = 0; i < waitingfriendss.size(); i++) {
                waitingfriends[i] = waitingfriendss.get(i);
                waitingfriendsID[i] = waitingfriendssID.get(i);
            }
        }
        String st3 = st.nextToken();
        if (!st3.equals("empty")) {
            StringTokenizer stt3 = new StringTokenizer(st3, ";;;");

            while (stt3.hasMoreElements()) {
                try {
                    String s=stt3.nextToken();
                    org.json.JSONObject JSON = new org.json.JSONObject(s);
                    allphonenumbers.add((String) JSON.get("phonenumber"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            phonenumbers = new String[allphonenumbers.size()];

            for (int i = 0; i < allphonenumbers.size(); i++) {
                phonenumbers[i] = allphonenumbers.get(i);
            }
        }

        Intent intent = new Intent(context, Contacts.class);
        intent.putExtra("listofcontact", listofcontact);
        intent.putExtra("waitingrequest",waitingfriends);
        intent.putExtra("waitingrequestID",waitingfriendsID);
        intent.putExtra("phonenumbers",phonenumbers);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}


//CHECK