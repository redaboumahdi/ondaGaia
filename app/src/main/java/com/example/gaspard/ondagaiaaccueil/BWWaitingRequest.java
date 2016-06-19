package com.example.gaspard.ondagaiaaccueil;

/**
 * Created by reda on 01/06/16.
 */
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.widget.Toast;
import org.json.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;



public class BWWaitingRequest extends AsyncTask<String,Void,String> {
    Context context;

    BWWaitingRequest(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String myID = params[0];
            String contact_url = "http://10.0.2.2:8888/waitingrequest.php";
            URL url = new URL(contact_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String login_data = URLEncoder.encode("myID", "UTF-8") + "=" + URLEncoder.encode(myID, "UTF-8");
            bufferedWriter.write(login_data);
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
        String s= "";
        String idme="";
        int I=0;
        while(result.charAt(I)!='}'){
            s+=result.charAt(I);
            I++;
        }
        s+=result.charAt(I);
        I++;
        try {
            org.json.JSONObject json = new org.json.JSONObject(s);
            idme=(String)json.get("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        s="";
        int number = 0;
        JSONArray array = new JSONArray();
        while(result.charAt(I)!='}'){
            s+=result.charAt(I);
            I++;
        }
        s+=result.charAt(I);
        I++;
        try {
            org.json.JSONObject json = new org.json.JSONObject(s);
            number=(Integer)json.get("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        s="";
        while(I<result.length()){
            if(result.charAt(I)!='}'){
                s+=result.charAt(I);
                I++;
            }
            else{
                s+=result.charAt(I);
                try {
                    array.add(new org.json.JSONObject(s));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                s="";
                I++;
            }
        }
        String[] waitingfriends=new String[number];
        String[] waitingfriendsID=new String[number];
        for (int j=0;j<number;j++) {
            String names = "";
            try {
                org.json.JSONObject name = new org.json.JSONObject(array.get(j).toString());
                names = name.get("first_name") + " " + name.get("last_name");
                waitingfriendsID[j]= (String) name.get("numR");
                waitingfriends[j] = names;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(context, WaitingRequest.class);
        String name1= "listofcontact";
        String name2="myID";
        String name3="listofID";
        intent.putExtra(name1, waitingfriends);
        intent.putExtra(name2, idme);
        intent.putExtra(name3,waitingfriendsID);
        context.startActivity(intent);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}


