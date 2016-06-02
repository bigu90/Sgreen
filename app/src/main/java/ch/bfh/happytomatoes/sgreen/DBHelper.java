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
        //getWritableDatabase().delete("measurment", null, null);
        database.execSQL("CREATE TABLE measurment(_id INTEGER PRIMARY KEY, sensorID TEXT, value REAL, time TEXT);");

        database.execSQL("CREATE TABLE sensor(sensor_id INTEGER PRIMARY KEY, type TEXT, location TEXT, name TEXT, visible REAL, minValue REAL, maxValue REAL);");
    }
     /**
     * Inserts Data into SQLite DB
     * @param queryValues
     */
    public void insertData(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = database.query("measurment", null, queryValues.get("id") + " = _id", null, null, null, null);
        if(cursor.getCount() == 0) {
            values.put("_id", queryValues.get("id"));
            values.put("sensorID", queryValues.get("sensorID"));
            values.put("value", queryValues.get("value"));
            values.put("time", queryValues.get("time"));
            database.insert("measurment", null, values);
        }
        database.close();
    }
    public void insertSensor(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = database.query("sensor", null, queryValues.get("sensorID") + " = sensor_id", null, null, null, null);
        if(cursor.getCount() == 0) {
            values.put("sensor_id", queryValues.get("sensorID"));
            values.put("type", queryValues.get("type"));
            values.put("location", queryValues.get("location"));
            values.put("name", queryValues.get("name"));
            values.put("visible", 1);
            values.put("minValue", 0);
            values.put("maxValue", 0);
            database.insert("sensor", null, values);
        }
        database.close();
    }

    public void updateSensor(int id, boolean show, double minValue, double maxValue){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int showSensor = 0;
        if(show){showSensor = 1;}
        values.put("visible", showSensor);
        values.put("minValue", minValue);
        values.put("maxValue", maxValue);

        // uptade Row
        database.update("sensor", values, "sensor_id =" + id, null);
        database.close(); // Closing database connection
    }

    public Cursor getTemperature(){
        SQLiteDatabase db = this.getReadableDatabase();
        //final String MY_QUERY = "SELECT * FROM measurment order by _id desc limit 1";
        final String MY_QUERY = "SELECT * FROM  sensor join measurment WHERE sensor_id = sensorID AND visible > 0 group by name order by sensor_id asc";
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getSensor(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT * FROM sensor Where sensor_id = " + ID;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getSensors(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT rowid as _id, name FROM sensor";
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getMinMaxValues(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT minValue, maxValue FROM sensor Where sensor_id =" + id;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor isVisible(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT visible FROM sensor Where sensor_id =" + id;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }


    public Cursor getLastTen(int sensorID){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT * FROM measurment WHERE sensorID = " + sensorID + " order by _id desc LIMIT 10";
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getLastTen2(long measurementID){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT * FROM measurment WHERE _id = " + measurementID ;
        Cursor tempCursor = db.rawQuery(MY_QUERY, null);
        tempCursor.moveToNext();
        Long id = tempCursor.getLong(1);
        System.out.println("id   " + tempCursor.getLong(1));
        final String MY_QUERY2 = "SELECT * FROM measurment WHERE sensorID = " + id + " order by _id desc LIMIT 10";
        Cursor cursor = db.rawQuery(MY_QUERY2, null);
        System.out.println("CUUURRSSOOOOOOR  " + cursor.getCount());
        return cursor;
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
        db.execSQL("DROP TABLE IF EXISTS " + "sensor");
        onCreate(db);
    }
}