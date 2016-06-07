package ch.bfh.happytomatoes.sgreen;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matthias on 29.04.2016.
 */
public class ChartActivity extends AppCompatActivity {
    private DBHelper dbHelper;
    private long measurementID;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        measurementID = getIntent().getLongExtra("sensorID", 0);

        dbHelper = new DBHelper(this);
        setChart();
    }


    private void setChart(){
        chart = (LineChart) findViewById(R.id.chart);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setTouchEnabled(false);
        chart.setDescription("Temperature");

        LineData lineData = new LineData(getXaxisData(), getValues());
        chart.setData(lineData);
        chart.invalidate();
    }


    private List<ILineDataSet> getValues(){
        int counter = 0;
        Cursor cursor = dbHelper.getLastTenEntrys(measurementID);
        System.out.println(cursor.getColumnCount());
        List<Entry> entries = new ArrayList<>();

        cursor.moveToLast();
        entries.add(new Entry(cursor.getFloat(2), counter++));
        while(cursor.moveToPrevious()){

            entries.add(new Entry(cursor.getFloat(2), counter++));
        }

        LineDataSet lineDataSet = new LineDataSet(entries, "Verlauf");
        lineDataSet.setDrawCubic(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);

        return dataSets;
    }


    private List<String> getXaxisData(){
        StringBuilder xAxisText;
        Cursor cursor = dbHelper.getLastTenEntrys((int)measurementID);
        System.out.println("cursor :" + cursor.getCount());
        List<String> entries = new ArrayList<>();
        cursor.moveToLast();
        xAxisText = new StringBuilder(cursor.getString(3));
        xAxisText.delete(0, 11);
        entries.add(xAxisText.toString());

        while(cursor.moveToPrevious()){
            xAxisText = new StringBuilder(cursor.getString(3));
            xAxisText.delete(0, 11);
            entries.add(xAxisText.toString());
        }


        return entries;
    }
}
