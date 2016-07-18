package com.example.gaspard.ondagaiaaccueil;

import android.content.Context;
import android.widget.TextView;

import org.json.JSONObject;

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
import java.util.Iterator;
import java.util.LinkedList;

public class MessageListener extends Thread {
    Context context;
    TextView txt;

    MessageListener(Context c, TextView t){
        this.context = c;
        this.txt = t;
    }

    public void run(){
        String login_url = "http://192.168.8.102:8888/test.php";
        try {
            String id=((GlobalVar)context.getApplicationContext()).getmyID();
            while(true) {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("ID", "UTF-8") + "=" + URLEncoder.encode(id, "UTF-8");
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
                try {
                    JSONObject js = new JSONObject(result);
                    JSONObject hs = new JSONObject();
                    int nombre = js.getInt("nombre");
                    JSONObject[] userM = new JSONObject[nombre];
                    for(int i = 0; i < nombre; i++){
                        userM[i] = new JSONObject((js.getJSONObject("messages")).getString("" + i));
                        if(userM[i].getString("readMessage").equals("1")) {
                            try {
                                int temp = Integer.parseInt(hs.getString(userM[i].getString("id_rec"))) + 1;
                                hs.put(userM[i].getString("id_rec"), "" + temp);
                            }
                            catch(Exception e) {
                                hs.put(userM[i].getString("id_rec"), "1");
                            }
                        }
                    }
                    ((GlobalVar)context.getApplicationContext()).setSomeVariable(userM);
                    ((GlobalVar)context.getApplicationContext()).nonLu = hs;
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    JSONObject[] message = ((GlobalVar)context.getApplicationContext()).getSomeVariable();
                    LinkedList<String> l = new LinkedList<String>();
                    LinkedList<String> last = new LinkedList<String>();
                    for (int i = message.length -1; i>0; i--){
                        try {
                            String temp1 = message[i].getString("id_send");
                            String temp2 = message[i].getString("id_rec");
                            String message1 = message[i].getString("message");
                            String res;
                            if(!temp1.equals(id)) res = temp1;
                            else res = temp2;
                            if (!l.contains(res)) {
                                l.add(res);
                                last.add(message1);
                            }
                        }catch (Exception e){}

                    }
                    Iterator<String> it =l.iterator();
                    Iterator<String> it1 =last.iterator();
                    String[] st1 = new String[l.size()];
                    int i = 0;
                    while(it.hasNext()){
                        st1[i] = it.next() + "\n" + it1.next();
                        i++;
                    }
                    ((GlobalVar)context.getApplicationContext()).setordre(st1);


                }catch (Exception e){
                    System.out.println(((GlobalVar)context.getApplicationContext()).getSomeVariable());
                }
                try {
                    Thread.sleep(3000);                 //1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }

            }

        } catch( MalformedURLException e){
            e.printStackTrace();
        } catch ( IOException e){
            e.printStackTrace();

        }
    }



}
