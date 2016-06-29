package com.example.gaspard.ondagaiaaccueil;

import android.os.AsyncTask;
import android.content.Context;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BWPutAccepted extends AsyncTask<String,Void,String> {
    Context context;
    BWPutAccepted(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        try {
            String num = params[0];
            String putaccepted_url = "http://192.168.0.31:8888/putaccepted.php";
            URL url = new URL(putaccepted_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String putaccepted_data = URLEncoder.encode("num","UTF-8")+"="+URLEncoder.encode(num,"UTF-8");
            bufferedWriter.write(putaccepted_data);
            bufferedWriter.flush();
            bufferedWriter.close();
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
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }


}

//CHECK