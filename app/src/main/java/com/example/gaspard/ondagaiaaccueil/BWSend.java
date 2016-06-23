package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.widget.ImageView;
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
import com.cloudinary.Cloudinary;
import java.util.Map;
import com.cloudinary.utils.ObjectUtils;
import java.io.File;
import java.util.HashMap;

public class BWSend extends AsyncTask<String,Void,String> {
    Context context;
    BWSend(Context ctx){
        context = ctx;
    }
    @Override
    protected String doInBackground(String... params){
        try {
            String IDme = params[0];
            String IDfriend=params[1];
            String picturepath=params[2];
            String lat=params[3];
            String lon=params[4];
            File file = new File (picturepath);
            Map config = new HashMap();
            config.put("api_secret", "CDUO-h5Y89RWXDHqjnIwYE-j_58");
            config.put("api_key", "212168143373637");
            config.put("cloud_name", "djzvvfdmn");
            Cloudinary cloudinary = new Cloudinary(config);
            Map uploadResult;
            try {
                uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            }
            catch (RuntimeException e) {
                return "Error uploading file: " + e.toString();
            }
            String urlpicture = (String) uploadResult.get("url");
            String send_url = "http://192.168.0.31:8888/send.php";
            URL url = new URL(send_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String login_data = URLEncoder.encode("IDme","UTF-8")+"="+URLEncoder.encode(IDme,"UTF-8") + "&"
                    +URLEncoder.encode("IDfriend","UTF-8")+"="+URLEncoder.encode(IDfriend,"UTF-8") + "&"
                    +URLEncoder.encode("urlpicture","UTF-8")+"="+URLEncoder.encode(urlpicture,"UTF-8")+"&"
                    +URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8") + "&"
                    +URLEncoder.encode("lon","UTF-8")+"="+URLEncoder.encode(lon,"UTF-8");
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

    @Override
    protected void onProgressUpdate(Void... values){
        super.onProgressUpdate(values);
    }


}
