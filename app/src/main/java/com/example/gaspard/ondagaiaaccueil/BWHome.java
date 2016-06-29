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

public class BWHome extends AsyncTask<String,Void,String> {
    Context context;

    BWHome(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String myID = params[0];
            String home_url = "http://192.168.0.31:8888/home.php";
            URL url = new URL(home_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String home_data = URLEncoder.encode("myID", "UTF-8") + "=" + URLEncoder.encode(myID, "UTF-8");
            bufferedWriter.write(home_data);
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
        String myID="";
        int I=0;
        System.out.println(result);
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
            s+=result.charAt(I);
            I++;
            while (result.charAt(I)!='}'){
                s+=result.charAt(I);
                I++;
            }
            s+=result.charAt(I);
            I++;
            try {
                array.add(new org.json.JSONObject(s));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            s="";
        }
        String[] url=new String[number];
        String[] orientation = new String[number];
        String[] status=new String[number];
        String[] name=new String[number];
        double[] lat=new double[number];
        double[] lon=new double[number];
        String[] num=new String[number];
        for (int j=0;j<number;j++) {
            String names = "";
            try {
                org.json.JSONObject JSON = new org.json.JSONObject(array.get(j).toString());
                names = JSON.get("first_name") + " " + JSON.get("last_name");
                name[j]=names;
                url[j]=(String)JSON.get("url");
                orientation[j]=(String)JSON.get("orientation");
                status[j]=(String)JSON.get("status");
                num[j]=(String)JSON.get("num");
                lat[j]= Double.parseDouble((String)JSON.get("lat"));
                lon[j] = Double.parseDouble((String)JSON.get("lon"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent intent = new Intent(context, Home.class);
        String name1= "url";
        String name2="myID";
        String name3="status";
        String name4="name";
        String name5="lat";
        String name6="lon";
        String name7="number";
        String name8="num";
        String name9="orientation";
        intent.putExtra(name1, url);
        intent.putExtra(name2, myID);
        intent.putExtra(name3,status);
        intent.putExtra(name4,name);
        intent.putExtra(name5,lat);
        intent.putExtra(name6,lon);
        intent.putExtra(name7,number);
        intent.putExtra(name8,num);
        intent.putExtra(name9,orientation);
        context.startActivity(intent);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}


//CHECK