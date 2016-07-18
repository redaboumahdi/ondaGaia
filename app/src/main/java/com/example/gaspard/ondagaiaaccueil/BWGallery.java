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


public class BWGallery extends AsyncTask<String,Void,String> {
    Context context;

    BWGallery(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            String myID = params[0];
            String gallery_url = "http://192.168.8.102:8888/gallery.php";
            URL url = new URL(gallery_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String gallery_data = URLEncoder.encode("myID", "UTF-8") + "=" + URLEncoder.encode(myID, "UTF-8");
            bufferedWriter.write(gallery_data);
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
        StringTokenizer st = new StringTokenizer(result, ";;;");

        ArrayList<String> listofcontactt=new ArrayList<String>();
        ArrayList<String> urll=new ArrayList<String>();
        ArrayList<String> orientationn=new ArrayList<String>();
        ArrayList<String> datee=new ArrayList<String>();
        ArrayList<String> numm=new ArrayList<String>();

        while (st.hasMoreElements()) {
            String names="";
            try {
                org.json.JSONObject JSON = new org.json.JSONObject(st.nextToken());
                names =((String)JSON.get("first_name")) + " " + ((String)JSON.get("last_name"));
                listofcontactt.add(names);
                urll.add((String)JSON.get("url"));
                orientationn.add((String)JSON.get("orientation"));
                datee.add((String)JSON.get("date"));
                numm.add((String)JSON.get("num"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String []listofcontact=new String[listofcontactt.size()];
        String []url=new String[urll.size()];
        String []orientation=new String[orientationn.size()];
        String []date=new String[datee.size()];
        String []num=new String[numm.size()];

        for (int i=0;i<listofcontactt.size();i++){
            listofcontact[i]=listofcontactt.get(i);
            url[i]=urll.get(i);
            orientation[i]=orientationn.get(i);
            date[i]=datee.get(i);
            num[i]=numm.get(i);
        }

        Intent intent = new Intent(context, Gallery.class);
        String name1= "listofcontact";
        String name2="num";
        String name3="url";
        String name4="orientation";
        String name5="date";
        intent.putExtra(name1, listofcontact);
        intent.putExtra(name2,num);
        intent.putExtra(name3,url);
        intent.putExtra(name4,orientation);
        intent.putExtra(name5,date);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}