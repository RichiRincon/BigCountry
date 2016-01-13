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
 * Created by Ricardo on 04/01/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    static final String FILE_NAME_JSON = "raw/countries_data_base.json";
    static final String JSON_COUNTRIES = "countries";
    static final String JSON_COUNTRIES_NAME = "name";
    static final String JSON_COUNTRIES_CAPITAL = "capital";
    static final String JSON_COUNTRIES_CONTINENT = "continent";
    static final String JSON_COUNTRIES_FLAG_NAME = "flag_name";
    static final String JSON_COUNTRIES_AREA = "area";
    static final String JSON_COUNTRIES_POPULATION = "population";

    static final String COUNTRIES_TABLE_NAME = "Countries";
    static final String COUNTRIES_COLUMN_ID = "id";
    static final String COUNTRIES_COLUMN_NAME = "name";
    static final String COUNTRIES_COLUMN_CAPITAL = "capital";
    static final String COUNTRIES_COLUMN_CONTINENT = "continent";
    static final String COUNTRIES_COLUMN_FLAG_NAME = "flag_name";
    static final String COUNTRIES_COLUMN_AREA = "area";
    static final String COUNTRIES_COLUMN_POPULATION = "population";

    private static Context context;

    //SQL to create a Countries table
    String sqlCreate = "CREATE TABLE " + COUNTRIES_TABLE_NAME + " (" +
            COUNTRIES_COLUMN_ID + " INTEGER NOT NULL PRIMARY KEY, " +
            COUNTRIES_COLUMN_NAME + " TEXT NOT NULL, " +
            COUNTRIES_COLUMN_CAPITAL + " TEXT, " +
            COUNTRIES_COLUMN_CONTINENT + " TEXT, " +
            COUNTRIES_COLUMN_FLAG_NAME + " TEXT NOT NULL, " +
            COUNTRIES_COLUMN_AREA + " REAL NOT NULL, " +
            COUNTRIES_COLUMN_POPULATION + " INTEGER)";

    /**
     *
     * @param context
     * @param DBname
     * @param factory
     * @param version
     */
    public DataBaseHelper(Context context, String DBname, CursorFactory factory, int version) {
        super(context, DBname, factory, version);
        this.context = context;
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creacion de la tabla
        db.execSQL(sqlCreate);

        //Insertamos los paises en la tabla Countries con la informacion del json
        try {
            createDatabaseFromJSON(db, context, FILE_NAME_JSON);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param db
     * @param tableName
     * @return
     */
    public static int numberRowsInTable(SQLiteDatabase db, String tableName) {
        int numRows = -1;
        String queryNumRows = "SELECT COUNT(*) FROM " + tableName;
        Cursor c = db.rawQuery(queryNumRows, null);
        if (c.moveToFirst()) {
            numRows = c.getInt(0);
        }
        return numRows;
    }

    /**
     *
     * @param db
     * @param rowId
     * @return
     */
    public static Country getCountriesRow(SQLiteDatabase db, int rowId) {
        Country country = null;
        String queryGetCountry = "Select * From " + COUNTRIES_TABLE_NAME + " Where " +
                COUNTRIES_COLUMN_ID + "=" + rowId;
        Cursor c = db.rawQuery(queryGetCountry, null);
        //Nos aseguramos de que existe al menos un resultado
        if (c.moveToFirst()) {
            String flag_name = c.getString(4);
            int flag_id = getIconId(context, flag_name);
            country = new Country(c.getString(1), flag_id, c.getDouble(5), c.getInt(6));
        }
        return country;
    }

    /**
     * Deletes a table and all rows in the table.
     * @param db
     * @param tableName
     */
    private void deleteTable(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }

    /**
     *
     * @param db
     * @param context
     * @param fileNameJSON
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

        /* If there are countries in "Countries" table, we empty it. */
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
     *
     * @param db
     * @param previousVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion) {
        //Se elimina la versi�n anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS " + COUNTRIES_TABLE_NAME);

        //Se crea de nuevo la tabla
        onCreate(db);
    }
}
