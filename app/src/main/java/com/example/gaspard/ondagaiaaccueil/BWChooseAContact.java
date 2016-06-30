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
import java.util.ArrayList;
import java.util.StringTokenizer;
import org.json.JSONException;


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
            String result = picturepath+";;;"+orientation+";;;";
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
        String myID="";
        String picturepath="";
        String orientation="";
        picturepath=st.nextToken();
        orientation=st.nextToken();
        try {
            org.json.JSONObject json = new org.json.JSONObject(st.nextToken());
            myID=(String)json.get("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> listofcontactt=new ArrayList<String>();
        ArrayList<String> listofIDD=new ArrayList<String>();

        while (st.hasMoreElements()) {
            String names="";
            try {
                org.json.JSONObject JSON = new org.json.JSONObject(st.nextToken());
                names =((String)JSON.get("first_name")) + " " + ((String)JSON.get("last_name"));
                listofcontactt.add(names);
                listofIDD.add((String)JSON.get("idR"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String []listofcontact=new String[listofcontactt.size()];
        String []listofID=new String[listofIDD.size()];

        for (int i=0;i<listofcontactt.size();i++){
            listofcontact[i]=listofcontactt.get(i);
            listofID[i]=listofIDD.get(i);
        }

        Intent intent = new Intent(context, ChooseAContact.class);
        String name1= "listofcontact";
        String name2="myID";
        String name3="listofID";
        String name4="picture";
        String name5="orientation";
        intent.putExtra(name1, listofcontact);
        intent.putExtra(name2, myID);
        intent.putExtra(name3,listofID);
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