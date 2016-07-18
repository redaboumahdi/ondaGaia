package com.example.gaspard.ondagaiaaccueil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.widget.Button;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.content.Intent;

public class PictureChoosen extends AppCompatActivity {

    Button Home,Send;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String myID = ((GlobalVar)this.getApplication()).getmyID();
        Bundle extras = getIntent().getExtras();
        final String picturepath = extras.getString("ChoosenPicture");
        Bitmap bp= BitmapFactory.decodeFile(picturepath);
        int orientation=0;
        try {
            ExifInterface exif = new ExifInterface(picturepath);
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            bp = Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);
        }
        catch (Exception e) {
        }

        setContentView(R.layout.picturechoosen);
        ImageView img = (ImageView)findViewById(R.id.imgView2);
        ImageView imgView=new ImageView(this);
        imgView.setImageBitmap(bp);
        img.setImageDrawable(imgView.getDrawable());

        Send = (Button) findViewById(R.id.SendItToAFriend);
        final String Orientation = Integer.toString(orientation);
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWChooseAContact bw=new BWChooseAContact(PictureChoosen.this);
                bw.execute(myID,picturepath,Orientation);
            }
        });

        Home = (Button) findViewById(R.id.home6);
        Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BWHome bw=new BWHome(PictureChoosen.this);
                bw.execute(myID);
            }
        });

    }
}

//CHECK