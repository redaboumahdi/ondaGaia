package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.AsyncTask;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import org.json.simple.JSONArray;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;
import org.json.JSONException;
import java.io.Reader;
import org.json.JSONObject;
import java.nio.charset.Charset;


public class BWlogin extends AsyncTask<String,Void,String> {
    Context context;
    BWlogin(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        try {
            String IDENTIFICATION = params[0];
            String PASSWORD=params[1];
            System.out.println(IDENTIFICATION);
            System.out.println(PASSWORD);
            String login_url = "http://192.168.0.31:8888/login.php";
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String login_data = URLEncoder.encode("IDENTIFICATION","UTF-8")+"="+URLEncoder.encode(IDENTIFICATION,"UTF-8") + "&"
                    +URLEncoder.encode("PASSWORD","UTF-8")+"="+URLEncoder.encode(PASSWORD,"UTF-8");
            bufferedWriter.write(login_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
            String result="";
            String line="";
            while ((line = bufferedReader.readLine())!= null){
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            //Log.i("DIDIlo",result);
            return result;
        } catch( MalformedURLException e){
            e.printStackTrace();
        } catch ( IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result){
        System.out.println(result);
        if(!result.equals("Your ID and/or your password are incorrect!")){
            try {
                JSONObject json = new JSONObject(result);
                System.out.println(json.toString());
                String idme =json.get("numero").toString();
                BWAccueil backgroundWorker = new BWAccueil(context);
                backgroundWorker.execute(idme);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }


}