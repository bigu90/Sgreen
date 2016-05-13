package ch.bfh.happytomatoes.sgreen;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private DBtoMySQLconnection dbUpdate;
    //private static String ORDER_BY = "time" + " DESC";
    private DBHelper dbHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Sgreeeeen");
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);



        dbHelper = new DBHelper(this);
        dbUpdate = new DBtoMySQLconnection(this, dbHelper);
        dbUpdate.getDataFromServer();

        // use this to start and trigger a service
        Intent i= new Intent(getBaseContext(), DbUpdateService.class);
        startService(i);


        //halllooo rivella

        //jajaaaa

        //ig o!
    }

    protected void onStart(Bundle savedInstanceState) {

    }

    @Override
    protected void onResume(){
    super.onResume();

        showEvents();
    }

    private Cursor getEvents() {
        // Perform a managed query. The Android system handles closing
        // and re-querying the cursor when needed.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        //System.out.println("GETTEEEEEVVVVEEEENNNTT");

        Cursor cursor = db.query("measurment", null, null, null, null, null, null);
        if(cursor.getCount() == 0){
            onResume();
        }
        return cursor;
    }


    private void showEvents() {
        Cursor cursor = dbHelper.getTemperature();
        String[] fromFields = new String[] {"name", "value", "time"};
        int[] to = new int[] {R.id.sensor_name, R.id.temp_text, R.id.sensor_informations};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.list_field, cursor, fromFields, to, 0);
        ListView list = (ListView) findViewById(R.id.listview_sensors);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                //String value = (String)adapter.getItemAtPosition(position);
                int id = (int) adapter.getItemIdAtPosition(position);
                adapter.getId();

                Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                System.out.println("measurement id  " + adapter.getItemIdAtPosition(position));

                intent.putExtra("sensorID", adapter.getItemIdAtPosition(position));
                startActivity(intent);
            }
        });

    }



    private void showEvent() {
        // Stuff them all into a big string
        StringBuilder infoText;


        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = dbHelper.getTemperature();

        cursor.moveToNext();

            double ID = cursor.getDouble(0);
            double SensorID = cursor.getDouble(1);
            double value = cursor.getDouble(2);
            String date = cursor.getString(3);

        Cursor cursor2 = dbHelper.getSensor((int)SensorID);
        cursor2.moveToNext();

        String type = cursor2.getString(1);
        String location = cursor2.getString(2);
        String name = cursor2.getString(3);

            System.out.println("HIIIIIIEEERRRRR" + date);
            infoText = new StringBuilder("Sensor Type: " + type + "\n");
            infoText.append("Sensor Location: " + location + "\n");

        // Display on the screen

        TextView sensorName = (TextView) findViewById(R.id.sensor_name);
        sensorName.setText(name);

        TextView temp = (TextView) findViewById(R.id.temp_text);
        temp.setText(value + "°C");

        TextView info = (TextView) findViewById(R.id.sensor_informations);
        info.setText(infoText);

        }






    public void refresh(View view){
        onResume();
    }

    public void newActivity(View view){
        Intent intent = new Intent(this, ChartActivity.class);
        intent.putExtra("sensorID", 1);
        startActivity(intent);
    }
}


