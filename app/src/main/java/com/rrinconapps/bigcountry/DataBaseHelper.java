package com.rrinconapps.bigcountry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.rrinconapps.bigcountry.ResourcesAccess.*;

/**
 * Helper for creation and treatment of data base in the app.
 *
 * @author Ricardo Rincon
 * @since 2016-01-04
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // Name of the json file holding countries' information
    static final String FILE_NAME_JSON = "raw/countries_data_base.json";
    // Names of different fields for each country in the json file
    static final String JSON_COUNTRIES = "countries";
    static final String JSON_COUNTRIES_NAME = "name";
    static final String JSON_COUNTRIES_CAPITAL = "capital";
    static final String JSON_COUNTRIES_CONTINENT = "continent";
    static final String JSON_COUNTRIES_FLAG_NAME = "flag_name";
    static final String JSON_COUNTRIES_AREA = "area";
    static final String JSON_COUNTRIES_POPULATION = "population";

    // Countries' table name in data base
    static final String COUNTRIES_TABLE_NAME = "Countries";
    // Name of columns in countries' table of data base
    static final String COUNTRIES_COLUMN_ID = "id";
    static final String COUNTRIES_COLUMN_NAME = "name";
    static final String COUNTRIES_COLUMN_CAPITAL = "capital";
    static final String COUNTRIES_COLUMN_CONTINENT = "continent";
    static final String COUNTRIES_COLUMN_FLAG_NAME = "flag_name";
    static final String COUNTRIES_COLUMN_AREA = "area";
    static final String COUNTRIES_COLUMN_POPULATION = "population";

    private static Context context;

    // SQL to create a Countries table
    String sqlCreate = "CREATE TABLE " + COUNTRIES_TABLE_NAME + " (" +
            COUNTRIES_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COUNTRIES_COLUMN_NAME + " TEXT NOT NULL, " +
            COUNTRIES_COLUMN_CAPITAL + " TEXT, " +
            COUNTRIES_COLUMN_CONTINENT + " TEXT, " +
            COUNTRIES_COLUMN_FLAG_NAME + " TEXT NOT NULL, " +
            COUNTRIES_COLUMN_AREA + " REAL NOT NULL, " +
            COUNTRIES_COLUMN_POPULATION + " INTEGER)";

    /**
     * Creates the helper for data base creation.
     *
     * @param context App context
     * @param DBname Name of the data base
     * @param factory CursorFactory
     * @param version Data base version
     */
    public DataBaseHelper(Context context, String DBname, CursorFactory factory, int version) {
        super(context, DBname, factory, version);
        this.context = context;
    }

    /**
     * Method executes when creating data base.
     *
     * @param db Data base
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Executes SQL to create the countries table
        db.execSQL(sqlCreate);

        // Insert countries' information from the json file in the table Countries
        try {
            createDatabaseFromJSON(db, context, FILE_NAME_JSON);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes SQL query to know the number of rows in a table of a data base.
     *
     * @param db Data base
     * @param tableName Name of the table we are interested in
     * @return the number of rows in the table
     */
    public static int numberRowsInTable(SQLiteDatabase db, String tableName) {
        int numRows = -1;
        String queryNumRows = "SELECT COUNT(*) FROM " + tableName;
        Cursor c = db.rawQuery(queryNumRows, null);
        if (c.moveToFirst()) {
            numRows = c.getInt(0);
        }
        c.close();
        return numRows;
    }

    /**
     * Gets the country in a given row.
     *
     * @param db Data base
     * @param rowId Row id
     * @return a Country object with the information stored in the indicated row
     */
    public static Country getCountriesRow(SQLiteDatabase db, int rowId) {
        Country country = null;
        String queryGetCountry = "Select * From " + COUNTRIES_TABLE_NAME + " Where " +
                COUNTRIES_COLUMN_ID + "=" + rowId;

        Cursor c = db.rawQuery(queryGetCountry, null);
        // Checks whether there exists a result of the query at least
        if (c.moveToFirst()) {
            String flag_name = c.getString(4);
            int flag_id = getIconId(context, flag_name);
            country = new Country(c.getString(1), c.getString(2), c.getString(3), flag_id,
                    c.getDouble(5), c.getInt(6));
        }

        c.close();
        return country;
    }

    /**
     * Deletes a table and all rows in the table.
     *
     * @param db Data base
     * @param tableName Name of the table we want to delete
     */
    private void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    /**
     * Reads countries' information from the json file to fill the table Countries in.
     *
     * @param db Data base
     * @param context App context
     * @param fileNameJSON Name of the json file
     * @throws IllegalArgumentException
     * @throws JSONException
     * @throws IOException
     */
    private void createDatabaseFromJSON(SQLiteDatabase db, Context context, String fileNameJSON)
            throws IllegalArgumentException, JSONException, IOException
    {
        String textFromJSON = null;

        if((context == null) && (fileNameJSON == null))
        {
            throw new IllegalArgumentException();
        }

        /* If there are already countries in "Countries" table, we empty it. */
        if(numberRowsInTable(db, COUNTRIES_TABLE_NAME) > 0)
        {
            deleteTable(db, COUNTRIES_TABLE_NAME);
            db.execSQL(sqlCreate);
        }

        /* Get text from JSON. */
        /* Internal JSON. */
        if(context != null)
        {
            InputStream inputStream = context.getResources().openRawResource(R.raw.countries_data_base);
            byte [] buffer = new byte[inputStream.available()];
            while(inputStream.read(buffer) != -1);
            textFromJSON = new String(buffer);
        }
        /* External JSON. */
        else if(fileNameJSON != null)
        {
            File file = new File(fileNameJSON);
            FileInputStream fileInputStream = new FileInputStream(file);
            byte [] buffer = new byte[fileInputStream.available()];
            while(fileInputStream.read(buffer) != -1);
            textFromJSON = new String(buffer);
        }

        /* Create array of countries from the text previously gotten. */
        JSONObject jsonBase = new JSONObject(textFromJSON);
        JSONArray jsonCountryArray = jsonBase.getJSONArray(JSON_COUNTRIES);

        for(int i = 0; i < jsonCountryArray.length(); i++)
        {
            JSONObject jsonObject = jsonCountryArray.getJSONObject(i);

            /* Read every country value and create a new row (new country) in "countries" table with
            these values. */
            ContentValues newRegister = new ContentValues();
            newRegister.put(COUNTRIES_COLUMN_ID, i+1);
            newRegister.put(COUNTRIES_COLUMN_NAME, jsonObject.getString(JSON_COUNTRIES_NAME));
            newRegister.put(COUNTRIES_COLUMN_CAPITAL, jsonObject.getString(JSON_COUNTRIES_CAPITAL));
            newRegister.put(COUNTRIES_COLUMN_CONTINENT, jsonObject.getString(JSON_COUNTRIES_CONTINENT));
            newRegister.put(COUNTRIES_COLUMN_FLAG_NAME, jsonObject.getString(JSON_COUNTRIES_FLAG_NAME));
            newRegister.put(COUNTRIES_COLUMN_AREA, jsonObject.getDouble(JSON_COUNTRIES_AREA));
            newRegister.put(COUNTRIES_COLUMN_POPULATION, jsonObject.getInt(JSON_COUNTRIES_POPULATION));

            db.insert(COUNTRIES_TABLE_NAME, null, newRegister);
        }
    }

    /**
     * Method executes when upgrading data base.
     *
     * @param db Data base
     * @param previousVersion of data base
     * @param newVersion of data base
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion) {
        // We delete previous version of the table
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRIES_TABLE_NAME);

        // We create it again
        onCreate(db);
    }
}
