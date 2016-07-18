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

public class BWAddContact extends AsyncTask<String,Void,String> {
    Context context;
    BWAddContact(Context ctx){
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params){
        try {
            String idS = params[0];
            String pseudoR=params[1];
            String contact_url = "http://192.168.8.102:8888/addacontact.php";
            URL url = new URL(contact_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String addacontact_data = URLEncoder.encode("idS","UTF-8")+"="+URLEncoder.encode(idS,"UTF-8") + "&"
                    +URLEncoder.encode("pseudoR","UTF-8")+"="+URLEncoder.encode(pseudoR,"UTF-8");
            bufferedWriter.write(addacontact_data);
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
    protected void onPostExecute(String result) {
        BWContact bw=new BWContact(this.context);
        bw.execute(((GlobalVar)this.context).getmyID());
    }

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }

}

//CHECK