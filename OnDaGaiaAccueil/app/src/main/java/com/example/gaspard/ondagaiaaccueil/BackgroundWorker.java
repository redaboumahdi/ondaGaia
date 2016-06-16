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


public class BackgroundWorker extends AsyncTask<String,Void,String> {
    Context context;
    BackgroundWorker(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        String type = params[0];
        if (type.equals("signin")) {
            try {
                String PASSWORD = params[4];
                String PASSWORDBIS = params[5];
                if (PASSWORD.equals((PASSWORDBIS))) {
                    String IDENTIFICATION = params[1];
                    String FIRST_NAME = params[2];
                    String LAST_NAME = params[3];
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
                    //Log.i("DIDIlo",result);
                    return result;
                }
            }catch (MalformedURLException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
        else if(type.equals("login")){
            try {
                String IDENTIFICATION = params[1];
                String PASSWORD=params[2];
                String login_url = "http://10.0.2.2:8888/login.php";
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
        }
        return null;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result){
        if(result.contains("success")) {
            Intent intent = new Intent(context,Accueil.class);
            context.startActivity(intent);
        }
        else{
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }


}