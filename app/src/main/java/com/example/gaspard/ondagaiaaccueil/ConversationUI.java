package com.example.gaspard.ondagaiaaccueil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.StringTokenizer;

public class ConversationUI extends Thread {
    private ListView ex;
    private Context context;
    private final Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            String[] aResponse = (String[]) msg.obj;
            ArrayAdapter adapter = new ArrayAdapter<String>(context,R.layout.chat_listview,aResponse);
            ex.setAdapter(adapter);

        }
    };

    ConversationUI(ListView ls, Context c){
        this.ex = ls;
        this.context = c;
    }

    public void run() {
        while (true) {
            try {
                Thread.sleep(2000);                 //1000 milliseconds is one second.
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            GlobalVar u = (GlobalVar) context.getApplicationContext();
            Message msg = Message.obtain(); // Creates an new Message instance
            String[] temp = new String[u.getordre().length];
            for (int i = 0; i < u.ordre.length; i++){
                temp[i] = u.ordre[i];
                try {
                    //parser \n pour chopper le id !!
                    StringTokenizer st = new StringTokenizer(u.ordre[i].toString(),"\n");
                    String var = st.nextElement().toString();
                    if(!u.nonLu.getString(var).equals("0"))
                    temp[i] += "\n" + u.nonLu.getString(var) + " non lus";
                }catch( Exception e){

                }
            }
            msg.obj = temp;
            msg.setTarget(this.handler); // Set the Handler
            msg.sendToTarget(); //Se
        }
    }
}
