package ch.bfh.happytomatoes.sgreen;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Matthias on 18.04.2016.
 */
public class DBtoMySQLconnection {
    HashMap<String, String> queryValues;
    DBHelper controller;

    public DBtoMySQLconnection(Context context, DBHelper helper) {
        controller = helper;
        System.out.println("111HIIIIIEEEEEEEERRRRR");
    }


        public void getDataFromServer() {

            // Create AsycHttpClient object
            AsyncHttpClient client = new AsyncHttpClient();
            // Http Request Params Object
            RequestParams params = new RequestParams();
            // Make Http call to getData.php

            System.out.println("HIIIIIEEEEEEEERRRRR");
            //client.post("http://213.239.210.108:9000/getMeasurements.php", params, new AsyncHttpResponseHandler() {
                    client.post("http://sgreen.bigu.ch/getMeasurements.php", params, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(String response) {
                            // Update SQLite DB with response sent by getData.php
                            updateSQLiteData(response);
                        }
                    });



/*            client.post("http://213.239.210.108:9000/getSensors.php", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(String response) {
                    // Update SQLite DB with response sent by getSensors.php
                    updateSQLiteSensor(response);
                }
            });*/
        }

    private void updateSQLiteData(String response) {
        ArrayList<HashMap<String, String>> dataSyncList;
        dataSyncList = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if(arr.length() != 0){
                // Loop through each array element, get JSON object which has
                //for (int i = 0; i < arr.length(); i++) {
                    for (int i = 0; i < 100; i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("id", obj.get("ID").toString());
                    // Add userName extracted from Object
                    queryValues.put("sensorID", obj.get("SensorID").toString());
                    queryValues.put("value", obj.get("Value").toString());
                    //System.out.println(obj.get("ID").toString() + "   " + obj.get("Value").toString());
                    queryValues.put("time", obj.get("Time").toString());
                    // Insert User into SQLite DB
                    controller.insertData(queryValues);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void updateSQLiteSensor(String response) {
        ArrayList<HashMap<String, String>> dataSyncList = new ArrayList<HashMap<String, String>>();
        // Create GSON object
        Gson gson = new GsonBuilder().create();
        try {
            // Extract JSON array from the response
            JSONArray arr = new JSONArray(response);
            System.out.println(arr.length());
            // If no of array elements is not zero
            if (arr.length() != 0) {
                // Loop through each array element, get JSON object which has
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("sensorID", obj.get("SensorID").toString());
                    // Add userName extracted from Object
                    queryValues.put("type", obj.get("Type").toString());
                    queryValues.put("location", obj.get("Location").toString());
                    queryValues.put("name", obj.get("Name").toString());
                    // Insert User into SQLite DB
                    controller.insertSensor(queryValues);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
