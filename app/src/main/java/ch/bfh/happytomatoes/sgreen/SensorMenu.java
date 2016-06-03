package ch.bfh.happytomatoes.sgreen;

import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.HashMap;

/**
 * Created by Matthias on 02.06.2016.
 */
public class SensorMenu extends AppCompatActivity {
    private DBHelper dbHelper;
    private CheckBox box;
    private EditText minValue;
    private EditText maxValue;
    private int sensorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensormenu);

        sensorID = (int) getIntent().getIntExtra("sensorID", 0);
        dbHelper = new DBHelper(this);

        box = (CheckBox) findViewById(R.id.checkBox);
        box.setChecked(isVisible());
        minValue = (EditText) findViewById((R.id.minValue));
        maxValue = (EditText) findViewById((R.id.maxValue));
        setValues();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(" Sgreeeeen");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        dbHelper = new DBHelper(this);
    }

    private boolean isVisible() {
        final Cursor cursor = dbHelper.isVisible(sensorID);
        cursor.moveToNext();
        if (cursor.getLong(0) == 0) {
            return false;
        }
        return true;
    }

    private void setValues(){
        final Cursor cursor = dbHelper.getMinMaxValues(sensorID);
        cursor.moveToNext();
        minValue.setText(cursor.getString(0));
        maxValue.setText(cursor.getString(1));
    }

    public void setData(View view) {
        double min = 0.0;
        double max = 0.0;
        if (minValue.getText().length() != 0) {
            min = Double.parseDouble(minValue.getText().toString());
        }
        if (maxValue.getText().length() != 0) {
            max = Double.parseDouble(maxValue.getText().toString());
        }

        dbHelper.updateSensor(sensorID, box.isChecked(), min, max);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}