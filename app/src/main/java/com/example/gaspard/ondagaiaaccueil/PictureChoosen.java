package com.example.gaspard.ondagaiaaccueil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Button;
import android.view.Gravity;

public class PictureChoosen extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LinearLayout layout = new LinearLayout(this);

        layout.setGravity(Gravity.CENTER);
        layout.setOrientation(LinearLayout.VERTICAL);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);



        Bundle extras = getIntent().getExtras();
        final String idme = extras.getString("myID");
        final String picturepath = extras.getString("ChoosenPicture");
        //System.out.println(picturepath);
        Bitmap bp= BitmapFactory.decodeFile(picturepath);
        ImageView imgView = new ImageView(this);
        imgView.setImageBitmap(bp);
        layout.addView(imgView,params);

        Button sendbutton = new Button(this);
        sendbutton.setText("Send it to a friend");
        layout.addView(sendbutton, params);

        setContentView(layout);

        sendbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWChooseAContact backgroundWorker = new BWChooseAContact(PictureChoosen.this);
                System.out.println(idme);
                backgroundWorker.execute(idme,picturepath);
            }
        });

    }
}
