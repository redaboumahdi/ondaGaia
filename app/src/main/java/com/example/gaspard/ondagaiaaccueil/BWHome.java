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
        StringTokenizer st = new StringTokenizer(result, ";;;");
        String myID="";
        try {
            org.json.JSONObject json = new org.json.JSONObject(st.nextToken());
            myID=(String)json.get("number");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ArrayList<String> urll=new ArrayList<String>();
        ArrayList<String> radiuss=new ArrayList<String>();
        ArrayList<String> statuss=new ArrayList<String>();
        ArrayList<String> orientationn=new ArrayList<String>();
        ArrayList<String> namee=new ArrayList<String>();
        ArrayList<Double> latt=new ArrayList<Double>();
        ArrayList<Double> lonn=new ArrayList<Double>();
        ArrayList<String> numm=new ArrayList<String>();

        while (st.hasMoreElements()) {
            String names="";
            try {
                org.json.JSONObject JSON = new org.json.JSONObject(st.nextToken());
                names =((String)JSON.get("first_name")) + " " + ((String)JSON.get("last_name"));
                namee.add(names);
                urll.add((String)JSON.get("url"));
                orientationn.add((String)JSON.get("orientation"));
                radiuss.add((String)JSON.get("radius"));
                statuss.add((String)JSON.get("status"));
                numm.add((String)JSON.get("num"));
                latt.add(Double.parseDouble((String)JSON.get("lat")));
                lonn.add(Double.parseDouble((String)JSON.get("lon")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String []url=new String[urll.size()];
        String []radius=new String[radiuss.size()];
        String []status=new String[statuss.size()];
        String []orientation=new String[orientationn.size()];
        String []name=new String[namee.size()];
        double []lat=new double[latt.size()];
        double []lon=new double[lonn.size()];
        String []num=new String[numm.size()];

        for (int i=0;i<urll.size();i++){
            url[i]=urll.get(i);
            radius[i]=radiuss.get(i);
            status[i]=statuss.get(i);
            orientation[i]=orientationn.get(i);
            name[i]=namee.get(i);
            lat[i]=latt.get(i);
            lon[i]=lonn.get(i);
            num[i]=numm.get(i);
        }

        Intent intent = new Intent(context, Home.class);
        String name1= "url";
        String name2="myID";
        String name3="status";
        String name4="name";
        String name5="lat";
        String name6="lon";
        String name8="num";
        String name9="orientation";
        String name10="radius";
        intent.putExtra(name1, url);
        intent.putExtra(name2, myID);
        intent.putExtra(name3,status);
        intent.putExtra(name4,name);
        intent.putExtra(name5,lat);
        intent.putExtra(name6,lon);
        intent.putExtra(name8,num);
        intent.putExtra(name9,orientation);
        intent.putExtra(name10,radius);
        context.startActivity(intent);
    }


    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}


//CHECK