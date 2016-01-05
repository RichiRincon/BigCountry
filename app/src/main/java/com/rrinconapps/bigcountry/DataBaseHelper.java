package com.rrinconapps.bigcountry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ricardo on 04/01/2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreate = "CREATE TABLE Countries (" +
            "id INTEGER NOT NULL PRIMARY KEY, " +
            "name TEXT NOT NULL, " +
            "capital TEXT, " +
            "continent TEXT, " +
            "flag_name TEXT NOT NULL, " +
            "flag_id INTEGER NOT NULL, " +
            "area REAL NOT NULL, " +
            "population INTEGER)";

    public DataBaseHelper(Context context, String DBname, CursorFactory factory, int version) {
        super(context, DBname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);

        //Insertamos los países en la tabla Countries
        int id = 1;
        String name = "Afghanistan";
        String capital = "Kabul";
        String continent = "Asia";
        String flag_name = "afghanistan";
        int flag_id = R.drawable.afghanistan;
        double area = 652230;
        int population = 1;
        db.execSQL("INSERT INTO Countries (id, name, capital, continent, flag_name, flag_id, area, population) " +
                "VALUES (" + id + ", '" + name + "', '" + capital + "', '" + continent + "', '" +
                flag_name + "', " + flag_id + ", " + area + ", " + population + ")");

        id += 1;
        name = "Albania";
        capital = "";
        continent = "";
        flag_name = "albania";
        flag_id = R.drawable.albania;
        area = 28748;
        population = 1;
        db.execSQL("INSERT INTO Countries (id, name, capital, continent, flag_name, flag_id, area, population) " +
                "VALUES (" + id + ", '" + name + "', '" + capital + "', '" + continent + "', '" +
                flag_name + "', " + flag_id + ", " + area + ", " + population + ")");

        id += 1;
        name = "Algeria";
        capital = "";
        continent = "";
        flag_name = "algeria";
        flag_id = R.drawable.algeria;
        area = 2381741;
        population = 1;
        db.execSQL("INSERT INTO Countries (id, name, capital, continent, flag_name, flag_id, area, population) " +
                "VALUES (" + id + ", '" + name + "', '" + capital + "', '" + continent + "', '" +
                flag_name + "', " + flag_id + ", " + area + ", " + population + ")");

        id += 1;
        name = "Andorra";
        capital = "";
        continent = "";
        flag_name = "andorra";
        flag_id = R.drawable.andorra;
        area = 468;
        population = 1;
        db.execSQL("INSERT INTO Countries (id, name, capital, continent, flag_name, flag_id, area, population) " +
                "VALUES (" + id + ", '" + name + "', '" + capital + "', '" + continent + "', '" +
                flag_name + "', " + flag_id + ", " + area + ", " + population + ")");

        id += 1;
        name = "Angola";
        capital = "";
        continent = "";
        flag_name = "angola";
        flag_id = R.drawable.angola;
        area = 1246700;
        population = 1;
        db.execSQL("INSERT INTO Countries (id, name, capital, continent, flag_name, flag_id, area, population) " +
                "VALUES (" + id + ", '" + name + "', '" + capital + "', '" + continent + "', '" +
                flag_name + "', " + flag_id + ", " + area + ", " + population + ")");

        id += 1;
        name = "Antarctic";
        capital = "";
        continent = "";
        flag_name = "antarctic";
        flag_id = R.drawable.antarctic;
        area = 14000000;
        population = 1;
        db.execSQL("INSERT INTO Countries (id, name, capital, continent, flag_name, flag_id, area, population) " +
                "VALUES (" + id + ", '" + name + "', '" + capital + "', '" + continent + "', '" +
                flag_name + "', " + flag_id + ", " + area + ", " + population + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int previousVersion, int newVersion) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Countries");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
