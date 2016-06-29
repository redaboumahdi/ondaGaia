package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
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
import org.json.JSONException;
import org.json.simple.JSONArray;

public class BWChooseAContact extends AsyncTask<String,Void,String> {
    Context context;

    BWChooseAContact(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String myID = params[0];
            String picturepath=params[1];
            String orientation=params[2];
            String contact_url = "http://192.168.0.31:8888/chooseacontact.php";
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
            String result = "{"+picturepath+"}"+"{"+orientation+"}";
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
        String picturepath="";
        int I=1;
        while(result.charAt(I)!='}'){
            picturepath+=result.charAt(I);
            I++;
        }
        I++;
        String orientation="";
        I++;
        while(result.charAt(I)!='}'){
            orientation+=result.charAt(I);
            I++;
        }
        I++;
        String s= "";
        String myID="";
        while(result.charAt(I)!='}'){
            s+=result.charAt(I);
            I++;
        }
        s+=result.charAt(I);
        I++;
        try {
            org.json.JSONObject json = new org.json.JSONObject(s);
            myID=(String)json.get("number");
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
        String[] friends=new String[number];
        String[] friendsID=new String[number];
        for (int j=0;j<number;j++) {
            String names = "";
            try {
                org.json.JSONObject name = new org.json.JSONObject(array.get(j).toString());
                names = name.get("first_name") + " " + name.get("last_name");
                friendsID[j]= (String) name.get("idR");
                friends[j] = names;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(context, ChooseAContact.class);
        String name1= "listofcontact";
        String name2="myID";
        String name3="listofID";
        String name4="picture";
        String name5="orientation";
        intent.putExtra(name1, friends);
        intent.putExtra(name2, myID);
        intent.putExtra(name3,friendsID);
        intent.putExtra(name4,picturepath);
        intent.putExtra(name5,orientation);
        context.startActivity(intent);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}


//CHECK