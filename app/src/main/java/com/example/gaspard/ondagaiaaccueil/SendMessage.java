package com.example.gaspard.ondagaiaaccueil;

import android.os.Handler;
import android.os.Message;
import android.widget.EditText;

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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;

/**
 * Created by reda on 02/07/16.
 */

public class SendMessage extends Thread {
    EditText etxt;
    String id_conv;
    Context c;


    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String aResponse = (String) msg.obj;
            etxt.setText("");
        }
    };


    SendMessage(Context c,EditText e, String i){
        this.etxt = e;
        this.id_conv = i;
        this.c=c;
    }

    public void run(){
        String login_url = "http://192.168.8.102:8888/sendchat.php";
        try {
            String id_send = ((GlobalVar)c.getApplicationContext()).getmyID();
            String id_rec = "" + id_conv;
            String message =  etxt.getText().toString();
            URL url = new URL(login_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));


            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            String dat =(String) dateFormat.format(date);


            String post_data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(id_send, "UTF-8") + "&" +  URLEncoder.encode("ID_REC", "UTF-8")+"=" + URLEncoder.encode(id_rec, "UTF-8")
                    +"&" + URLEncoder.encode("MESSAGE", "UTF-8")+"=" + URLEncoder.encode(message, "UTF-8") + "&" + URLEncoder.encode("DAT", "UTF-8") + "=" +URLEncoder.encode(dat, "UTF-8") ;
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Message msg = Message.obtain(); // Creates an new Message instance
            msg.obj = result;
            msg.setTarget(this.handler); // Set the Handler
            msg.sendToTarget(); //Se

        } catch( MalformedURLException e){
            e.printStackTrace();
        } catch ( IOException e){
            e.printStackTrace();

        }
    }
}
