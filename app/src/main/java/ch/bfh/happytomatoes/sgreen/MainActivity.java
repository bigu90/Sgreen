package ch.bfh.happytomatoes.sgreen;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity  {
    private DBtoMySQLconnection dbUpdate;
    //private static String ORDER_BY = "time" + " DESC";
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);
        dbUpdate = new DBtoMySQLconnection(this, dbHelper);

        dbUpdate.getDataFromServer();


        //halllooo rivella

        //jajaaaa

        //ig o!
    }


    @Override
    protected void onResume(){
    super.onResume();

        showEvents(getEvents());
    }

    private Cursor getEvents() {
        // Perform a managed query. The Android system handles closing
        // and re-querying the cursor when needed.
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("measurment", null, null, null, null, null, null);
        System.out.println("ONRESUUUMMMEEE");
        System.out.println(cursor.getColumnCount());
        return cursor;
    }




    private void showEvents(Cursor cursor) {
        // Stuff them all into a big string
        StringBuilder uiText = new StringBuilder("Events in Table:\n");

        while (cursor.moveToNext()) {

            // Could use getColumnIndexOrThrow() to get indexes
            long id = cursor.getLong(0);
         /*   Calendar cal1 = Calendar.getInstance();
            cal1.setTimeInMillis(cursor.getLong(1));*/
            long date = cursor.getLong(1);
            //String dateFormated = new SimpleDateFormat("dd-MM-yyyy").format(new Date(date));
            long jaja = cursor.getLong(2);
            long jaja2 = cursor.getLong(3);

/*            SQLiteDatabase db = dbHelper.getReadableDatabase();
            db.query("measurment", null, null, null, null, null, null);
            db.*/

            uiText.append("id: " +  cursor.getLong(0) + "  SensorID: " + cursor.getString(1) + "  Value: " + cursor.getString(2) + "  Datum: " + cursor.getString(3));
            uiText.append("\n");
        }
        // Display on the screen

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(uiText);
    }
}
