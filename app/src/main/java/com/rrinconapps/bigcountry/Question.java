package com.rrinconapps.bigcountry;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Ricardo on 19/12/2015.
 */
public class Question {
    private Country _optionA;
    private Country _optionB;
    private Country _answer;

    public Question(SQLiteDatabase db, ImageButton optionA_IM, TextView optionA_TV,
                    ImageButton optionB_IM, TextView optionB_TV) {

        int numCountries = 0;
        String queryNumCountries = "SELECT COUNT(*) FROM Countries";
        Cursor c = db.rawQuery(queryNumCountries, null);
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            numCountries = c.getInt(0);
        }
        Log.e("Log_tag", "Number of countries in data base : " + numCountries);
        Random rand = new Random();

        int randomNumA = rand.nextInt(numCountries);
        String queryCountryA = "Select * From Countries Where id=" + randomNumA;
        c = db.rawQuery(queryCountryA, null);
        if (c.moveToFirst()) {
            _optionA = new Country(c.getString(1), c.getInt(5), c.getDouble(6), c.getInt(7));
        }
        optionA_TV.setText(_optionA.get_name());
        optionA_IM.setImageResource(_optionA.get_flag_id());

        int randomNumB = -1;
        do {
            randomNumB = rand.nextInt(numCountries);
        } while (randomNumB == randomNumA);
        String queryCountryB = "Select * From Countries Where id=" + randomNumB;
        c = db.rawQuery(queryCountryB, null);
        if (c.moveToFirst()) {
            _optionB = new Country(c.getString(1), c.getInt(5), c.getDouble(6), c.getInt(7));
        }
        optionB_TV.setText(_optionB.get_name());
        optionB_IM.setImageResource(_optionB.get_flag_id());

        if (_optionA.isBigger(_optionB))
            _answer = _optionA;
        else
            _answer = _optionB;
    }

    public Country get_optionA() {
        return _optionA;
    }

    public Country get_optionB() {
        return _optionB;
    }

    public Country get_answer() {
        return _answer;
    }
}
