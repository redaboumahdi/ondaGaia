package com.example.gaspard.ondagaiaaccueil;

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
import org.json.JSONObject;

public class BWSignin extends AsyncTask<String,Void,String> {
    Context context;
    BWSignin(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        try {
            String password = params[3];
            String passwordbis = params[4];
            if (password.equals((passwordbis))) {
                String pseudo = params[0];
                String first_name = params[1];
                String last_name = params[2];
                String signin_url = "http://192.168.0.31:8888/signin.php";
                URL url = new URL(signin_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("pseudo", "UTF-8") + "=" + URLEncoder.encode(pseudo, "UTF-8") + "&"
                        + URLEncoder.encode("first_name", "UTF-8") + "=" + URLEncoder.encode(first_name, "UTF-8") + "&"
                        + URLEncoder.encode("last_name", "UTF-8") + "=" + URLEncoder.encode(last_name, "UTF-8") + "&"
                        + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
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
        if(!result.equals("This ID already exists!")) {
            try {
                JSONObject json = new JSONObject(result);
                String myID =json.get("numero").toString();
                BWHome backgroundWorker = new BWHome(context);
                backgroundWorker.execute(myID);
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

//CHECK