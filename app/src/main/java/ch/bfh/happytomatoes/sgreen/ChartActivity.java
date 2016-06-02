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
    private long sensorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        Toolbar myToolbar = (Toolbar)findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        sensorID = getIntent().getLongExtra("sensorID", 0);

        System.out.println("chart" + sensorID);

        dbHelper = new DBHelper(this);
        setChart();



    }

    private void setChart(){
        LineChart chart = (LineChart) findViewById(R.id.chart);
        chart.setAutoScaleMinMaxEnabled(true);
        chart.setTouchEnabled(false);
        //chart.canScrollVertically(1);
        //chart.setBackgroundColor(getResources().getColor(R.color.chartBackground));


        ArrayList<String> xVals = new ArrayList<String>();
        xVals.add("test1"); xVals.add("test2"); xVals.add("test3"); xVals.add("test4");

        LineData lineData = new LineData(getXaxisData(), getValues());
        chart.setData(lineData);
        chart.invalidate();
    }


    private List<ILineDataSet> getValues(){
        //Cursor cursor = dbHelper.getLastTen(sensorID);
        int counter = 0;
        Cursor cursor = dbHelper.getLastTen2(sensorID);
        System.out.println(cursor.getColumnCount());
        List<Entry> entries = new ArrayList<>();

        while(cursor.moveToNext()){

            entries.add(new Entry(cursor.getFloat(2), counter++));
            System.out.println(cursor.getFloat(2));

        }
        LineDataSet lineDataSet = new LineDataSet(entries, "Test");
        lineDataSet.setDrawCubic(true);

        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(lineDataSet);

        return dataSets;
    }

    private List<String> getXaxisData(){
        StringBuilder xAxisText;
        Cursor cursor = dbHelper.getLastTen(1);
        List<String> entries = new ArrayList<>();

        while(cursor.moveToNext()){
            xAxisText = new StringBuilder(cursor.getString(3));
            xAxisText.delete(0, 11);
            entries.add(xAxisText.toString());

        }

        return entries;
    }

}
