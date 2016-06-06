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
    public static final String MEASUREMENT_ID = "_id";
    public static final String MEASUREMENT_SENSOR_ID = "sensorID";
    public static final String MEASUREMENT_VALUE = "value";
    public static final String MEASUREMENT_TIME = "time";

    public static final String SENSOR_ID = "sensor_id";
    public static final String SENSOR_TYPE = "type";
    public static final String SENSOR_LOCATION = "location";
    public static final String SENSOR_NAME = "name";
    public static final String SENSOR_VISIBLE = "visible";
    public static final String SENSOR_MIN_VALUE = "minValue";
    public static final String SENSOR_MAX_VALUE = "maxValue";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        //delete(getWritableDatabase());
    }
    //Creates Table
    @Override
    public void onCreate(SQLiteDatabase database) {
        //database.execSQL("DROP TABLE IF EXISTS measurment");
        //getWritableDatabase().delete("measurment", null, null);
        database.execSQL("CREATE TABLE measurment(" + MEASUREMENT_ID + " INTEGER PRIMARY KEY, " + MEASUREMENT_SENSOR_ID + " TEXT, " + MEASUREMENT_VALUE + " REAL, " + MEASUREMENT_TIME + " TEXT);");

        database.execSQL("CREATE TABLE sensor(" + SENSOR_ID + " INTEGER PRIMARY KEY, " + SENSOR_TYPE + " TEXT, " + SENSOR_LOCATION + " TEXT, " + SENSOR_NAME + " TEXT, " + SENSOR_VISIBLE + " REAL, " + SENSOR_MIN_VALUE + " REAL, " + SENSOR_MAX_VALUE + " REAL);");
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
            values.put(MEASUREMENT_ID, queryValues.get("id"));
            values.put(MEASUREMENT_SENSOR_ID, queryValues.get("sensorID"));
            values.put(MEASUREMENT_VALUE, queryValues.get("value"));
            values.put(MEASUREMENT_TIME, queryValues.get("time"));
            database.insert("measurment", null, values);
        }
        database.close();
    }
    public void insertSensor(HashMap<String, String> queryValues) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        Cursor cursor = database.query("sensor", null, queryValues.get("sensorID") + " = sensor_id", null, null, null, null);
        if(cursor.getCount() == 0) {
            values.put(SENSOR_ID, queryValues.get("sensorID"));
            values.put(SENSOR_TYPE, queryValues.get("type"));
            values.put(SENSOR_LOCATION, queryValues.get("location"));
            values.put(SENSOR_NAME, queryValues.get("name"));
            values.put(SENSOR_VISIBLE, 1);
            values.put(SENSOR_MIN_VALUE, 0);
            values.put(SENSOR_MAX_VALUE, 0);
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
        database.update("sensor", values, SENSOR_ID +" =" + id, null);
        database.close(); // Closing database connection
    }

    public Cursor getTemperature(){
        SQLiteDatabase db = this.getReadableDatabase();
        //final String MY_QUERY = "SELECT * FROM measurment order by _id desc limit 1";
        final String MY_QUERY = "SELECT * FROM  sensor join measurment WHERE " + SENSOR_ID + " = " + MEASUREMENT_SENSOR_ID + " AND " + SENSOR_VISIBLE + " > 0 group by " + SENSOR_NAME + " order by " + SENSOR_ID + " asc";
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getSensor(int ID){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT * FROM sensor Where " + SENSOR_ID + " = " + ID;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getSensors(){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT rowid as " + MEASUREMENT_ID + ", " + SENSOR_NAME + " FROM sensor";
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getMinMaxValues(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT " + SENSOR_MIN_VALUE + ", " + SENSOR_MAX_VALUE + " FROM sensor Where " + SENSOR_ID + " =" + id;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor isVisible(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT " + SENSOR_VISIBLE + " FROM sensor Where " + SENSOR_ID + " =" + id;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }


    public Cursor getLastTen(int sensorID){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT * FROM measurment WHERE " + MEASUREMENT_SENSOR_ID + " = " + sensorID + " order by " + MEASUREMENT_ID + " desc LIMIT 10";
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        return cursor;
    }

    public Cursor getLastTen2(long measurementID){
        SQLiteDatabase db = this.getReadableDatabase();
        final String MY_QUERY = "SELECT * FROM measurment WHERE " + MEASUREMENT_ID + " = " + measurementID ;
        Cursor tempCursor = db.rawQuery(MY_QUERY, null);
        tempCursor.moveToNext();
        Long id = tempCursor.getLong(1);
        System.out.println("id   " + tempCursor.getLong(1));
        final String MY_QUERY2 = "SELECT * FROM measurment WHERE " + MEASUREMENT_SENSOR_ID + " = " + id + " order by " + MEASUREMENT_ID + " desc LIMIT 10";
        Cursor cursor = db.rawQuery(MY_QUERY2, null);
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