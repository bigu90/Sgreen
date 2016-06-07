package ch.bfh.happytomatoes.sgreen;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.HashMap;

/**
 * Created by Dominik on 22.04.2016.
 */
public class DbUpdater extends BroadcastReceiver {

    HashMap<String, String> queryValues;
    DBHelper controller;
    Boolean issueNotification;
    private DBtoMySQLconnection dbUpdate;
    private DBHelper dbHelper;



    @Override
    public void onReceive(Context context, Intent intent) {
        updateDatabase(context);
}

    private void updateDatabase(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        dbUpdate = new DBtoMySQLconnection(context, dbHelper);
        dbUpdate.getDataFromServer();
    }
}
