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


public class BWsignin extends AsyncTask<String,Void,String> {
    Context context;
    BWsignin(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        try {
            String PASSWORD = params[3];
            String PASSWORDBIS = params[4];
            if (PASSWORD.equals((PASSWORDBIS))) {
                String IDENTIFICATION = params[0];
                String FIRST_NAME = params[1];
                String LAST_NAME = params[2];
                System.out.println(IDENTIFICATION);
                String signin_url = "http://10.0.2.2:8888/signin.php";
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("IDENTIFICATION", "UTF-8") + "=" + URLEncoder.encode(IDENTIFICATION, "UTF-8") + "&"
                        + URLEncoder.encode("FIRST_NAME", "UTF-8") + "=" + URLEncoder.encode(FIRST_NAME, "UTF-8") + "&"
                        + URLEncoder.encode("LAST_NAME", "UTF-8") + "=" + URLEncoder.encode(LAST_NAME, "UTF-8") + "&"
                        + URLEncoder.encode("PASSWORD", "UTF-8") + "=" + URLEncoder.encode(PASSWORD, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
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
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
        if(!result.equals("This ID already exists!")) {
            Intent intent = new Intent(context,Accueil.class);
            try {
                JSONObject json = new JSONObject(result);
                System.out.println(json.toString());
                String idme =json.get("numero").toString();
                String name= "myID";
                intent.putExtra(name, idme);
                Log.i("didilo",idme);
                context.startActivity(intent);
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