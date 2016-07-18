package com.example.gaspard.ondagaiaaccueil;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.StringTokenizer;

public class Chat extends AppCompatActivity {
    TextView txt;
    ListView ex;
    Button but3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        MessageListener m = new MessageListener(Chat.this,txt);
        m.start();
        ex = (ListView) findViewById(R.id.exp);
        ConversationUI c = new ConversationUI(ex, this);
        c.start();
        but3 = (Button) findViewById(R.id.button3);
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chat.this, Chat3.class);
                startActivity(intent);


            }
        });

        ex.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                      @Override
                                      public void onItemClick (AdapterView< ? > adapter, View view, int position, long arg){
              TextView v = (TextView) view.findViewById(R.id.label);
              String id_conv = v.getText().toString();
                                          StringTokenizer stk = new StringTokenizer(id_conv);
                                          String res = stk.nextToken();
              Intent intent = new Intent(Chat.this, Chat2.class);
              intent.putExtra("id_conversation",res);
              startActivity(intent);
          }
      }
        );
    }

}
