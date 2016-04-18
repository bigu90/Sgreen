package ch.bfh.happytomatoes.sgreen;

import android.app.Service;
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
public class DBService extends Service {
    HashMap<String, String> queryValues;
    DBHelper controller = new DBHelper(this);

    @Override
    public void onCreate() {
        // Create AsycHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        // Http Request Params Object
        RequestParams params = new RequestParams();
        // Make Http call to getData.php
        client.post("http://213.239.210.108:9000/getData.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Update SQLite DB with response sent by getData.php
                updateSQLiteData(response);
            }
        });

        client.post("http://213.239.210.108:9000/getSensors.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // Update SQLite DB with response sent by getSensors.php
                updateSQLiteSensor(response);
            }
        });
    }

    public void updateSQLiteData(String response) {
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
                for (int i = 0; i < arr.length(); i++) {
                    // Get JSON object
                    JSONObject obj = (JSONObject) arr.get(i);
                    // DB QueryValues Object to insert into SQLite
                    queryValues = new HashMap<String, String>();
                    // Add userID extracted from Object
                    queryValues.put("id", obj.get("id").toString());
                    // Add userName extracted from Object
                    queryValues.put("sensorID", obj.get("sensorID").toString());
                    queryValues.put("value", obj.get("value").toString());
                    queryValues.put("time", obj.get("time").toString());
                    // Insert User into SQLite DB
                    controller.insertData(queryValues);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void updateSQLiteSensor(String response) {
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
                    queryValues.put("sensorID", obj.get("sensorID").toString());
                    // Add userName extracted from Object
                    queryValues.put("type", obj.get("type").toString());
                    queryValues.put("location", obj.get("location").toString());
                    queryValues.put("name", obj.get("name").toString());
                    // Insert User into SQLite DB
                    controller.insertSensor(queryValues);
                }
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
