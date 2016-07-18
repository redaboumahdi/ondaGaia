package com.example.gaspard.ondagaiaaccueil;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import android.graphics.Bitmap;

import org.json.JSONObject;

public class GlobalVar extends Application {

    private String myID;

    private JSONObject[] someVariable;
    public String[] ordre;
    public JSONObject nonLu;

    public String getmyID(){
        return this.myID;
    }

    public void setmyID(String s){
        this.myID=s;
    }

    public JSONObject[] getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(JSONObject[] someVariable) {
        this.someVariable = someVariable;
    }

    public void setordre(String[]s){
        this.ordre=s;
    }

    public String[] getordre(){
        return this.ordre;
    }
}
