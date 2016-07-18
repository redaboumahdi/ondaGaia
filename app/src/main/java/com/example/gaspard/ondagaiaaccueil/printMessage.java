package com.example.gaspard.ondagaiaaccueil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.Objects;

/**
 * Created by reda on 02/07/16.
 */
public class printMessage extends Thread {
    Context context;
    TextView txt;
    String id_conv;
    int flag = 0;
    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            String aResponse = (String) msg.obj;

            if ((null != aResponse)) {

                // ALERT MESSAGE
                boolean b = txt.getText().toString().equals(aResponse);
                txt.setText(aResponse);
                final int scrollAmount = txt.getLayout().getLineTop(txt.getLineCount()) - txt.getHeight();
                // if there is no need to scroll, scrollAmount will be <=0
                if(!b){
                    if (scrollAmount > 0)
                        txt.scrollTo(0, scrollAmount);
                    else
                        txt.scrollTo(0, 0);

                }
            }


        }
    };

    printMessage(Context c, TextView t, String ic){
        context = c;
        txt = t;
        id_conv = ic;

    }

    public void run(){
        while(true) {
            JSONObject[] temp = ((GlobalVar)context.getApplicationContext()).getSomeVariable();
            String result = "";
            for (int i = 0; i < temp.length; i++) {
                JSONObject js = temp[i];
                try {
                    String ids = js.getString("id_send");
                    String idr = js.getString("id_rec");
                    if (ids.equals(id_conv) || idr.equals(id_conv)) {
                        result += "sender :" + ids + " message : ";
                        result += js.getString("message");
                        result += "\n";
                    }
                } catch (Exception e) {
                }
            }
            Message msg = Message.obtain(); // Creates an new Message instance
            msg.obj = result;
            msg.setTarget(this.handler); // Set the Handler
            msg.sendToTarget(); //Send the message
            try {
                Thread.sleep(1000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            if(flag == 1) return;
        }

    }


}

