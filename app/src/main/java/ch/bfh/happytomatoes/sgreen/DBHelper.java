package ch.bfh.happytomatoes.sgreen;

        import java.util.ArrayList;
        import java.util.HashMap;

        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper  extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Sgreen";
    public static final int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);


        //delete(getWritableDatabase());
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL("DROP TABLE IF EXISTS measurment");
        getWritableDatabase().delete("measurment", null, null);
        database.execSQL("CREATE TABLE measurment(_id INTEGER PRIMARY KEY, sensorID TEXT, value TEXT, time TEXT);");

        database.execSQL("CREATE TABLE " + "sensor" + " (" + "sensor_id"
                + " INTEGER PRIMARY KEY, " + "type"
                + " TEXT," + "location" + " TEXT," + "name" + "TEXT" + ");");
    }
    /**
     * Inserts Data into SQLite DB
     * @param queryValues
     */
    public void insertData(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("_id", queryValues.get("id"));
        values.put("sensorID", queryValues.get("sensorID"));
        values.put("value", queryValues.get("value"));
        values.put("time", queryValues.get("time"));
        database.insert("measurment", null, values);
        database.close();
    }
    public void insertSensor(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sensorID", queryValues.get("sensorID"));
        values.put("type", queryValues.get("type"));
        values.put("location", queryValues.get("location"));
        values.put("name", queryValues.get("name"));
        database.insert("sensor", null, values);
        database.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int version_old, int current_version) {
        String query;
        query = "DROP TABLE IF EXISTS users";
        database.execSQL(query);
        onCreate(database);
    }


    public void delete(SQLiteDatabase db) {
        //db.execSQL("Sgreen");
        db.execSQL("DROP TABLE IF EXISTS " + "measurment");
        onCreate(db);
    }
}