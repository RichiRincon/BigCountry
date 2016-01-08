package com.rrinconapps.bigcountry;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Random;

import static com.rrinconapps.bigcountry.DataBaseHelper.*;

enum Answers {
    OPTION_A, OPTION_B
}

/**
 * Created by Ricardo on 19/12/2015.
 */
public class Question {
    private Country optionA;
    private Country optionB;
    private Answers answer;

    /**
     *
     * @param db
     * @param optionA_IM
     * @param optionA_TV
     * @param optionB_IM
     * @param optionB_TV
     */
    public Question(SQLiteDatabase db, ImageButton optionA_IM, TextView optionA_TV,
                    ImageButton optionB_IM, TextView optionB_TV) {

        int numCountries = numberRowsInTable(db, COUNTRIES_TABLE_NAME);
        int[] randomNums = getTwoRandomNumbers(numCountries);

        optionA = getCountriesRow(db, randomNums[0]);
        optionA_TV.setText(optionA.getName());
        optionA_IM.setImageResource(optionA.getFlag_id());

        optionB = getCountriesRow(db, randomNums[1]);
        optionB_TV.setText(optionB.getName());
        optionB_IM.setImageResource(optionB.getFlag_id());

        setAnswer();
    }

    /**
     *
     * @param max
     * @return
     */
    private int[] getTwoRandomNumbers(int max) {
        int[] randomNums = new int[2];
        Random rand = new Random();
        randomNums[0] = rand.nextInt(max) + 1;
        do {
            randomNums[1] = rand.nextInt(max) + 1;
        } while (randomNums[1] == randomNums[0]);
        return randomNums;
    }

    public Country getOptionA() {
        return optionA;
    }

    public Country getOptionB() {
        return optionB;
    }

    public Answers getAnswer() {
        return answer;
    }

    private void setAnswer() {
        if (optionA.isBigger(optionB))
            answer = Answers.OPTION_A;
        else
            answer = Answers.OPTION_B;
    }
}
