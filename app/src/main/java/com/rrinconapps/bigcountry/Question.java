package com.rrinconapps.bigcountry;

import android.database.sqlite.SQLiteDatabase;

import java.util.Random;

import static com.rrinconapps.bigcountry.DataBaseHelper.COUNTRIES_TABLE_NAME;
import static com.rrinconapps.bigcountry.DataBaseHelper.getCountriesRow;
import static com.rrinconapps.bigcountry.DataBaseHelper.numberRowsInTable;

/**
 * Possible answers for a question.
 */
enum Answers {
    OPTION_A, OPTION_B
}

/**
 * Models a question with two options answer.
 *
 * @author Ricardo Rincon
 * @since 2015-12-19
 */
public class Question {
    private String wording;
    private Country optionA;
    private Country optionB;
    private Answers answer;

    /**
     * Construct a question with data of a data base.
     *
     * @param db Date base with countries information.
     */
    public Question(SQLiteDatabase db) {

        // Gets the number of countries in data base
        int numCountries = numberRowsInTable(db, COUNTRIES_TABLE_NAME);
        // Gets two random number in that range
        int[] randomNums = getTwoRandomNumbers(numCountries);

        // Gets the country's information corresponding the first number row in db and sets it as option A
        optionA = getCountriesRow(db, randomNums[0]);
        // Gets the country's information corresponding the second number row in db and sets it as option B
        optionB = getCountriesRow(db, randomNums[1]);

        // Sets the answer
        setAnswer();
    }

    /**
     * Produces 2 different natural random numbers in a range between 1 and max.
     *
     * @param max Maximum natural number of the range
     * @return an array of 2 numbers
     */
    private int[] getTwoRandomNumbers(int max) {
        int[] randomNums = new int[2];
        Random rand = new Random();
        randomNums[0] = rand.nextInt(max) + 1;

        // While both numbers are the same, we take another second number
        do {
            randomNums[1] = rand.nextInt(max) + 1;
        } while (randomNums[1] == randomNums[0]);

        return randomNums;
    }

    /**
     * Sets the answer of the question as the biggest country between the two options.
     */
    private void setAnswer() {
        if (optionA.isBigger(optionB))
            answer = Answers.OPTION_A;
        else
            answer = Answers.OPTION_B;
    }

    /**
     * Gets the question's option A.
     * @return the country object corresponding to option A
     */
    public Country getOptionA() {
        return optionA;
    }

    /**
     * Gets the question's option B.
     * @return the country object corresponding to option B
     */
    public Country getOptionB() {
        return optionB;
    }

    /**
     * Gets the answer of the question.
     * @return an option in the enum Answers
     */
    public Answers getAnswer() {
        return answer;
    }
}
