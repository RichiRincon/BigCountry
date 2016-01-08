package com.rrinconapps.bigcountry;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ricardo on 19/12/2015.
 */
public class Game {
    private int numQuestions;
    private int score;
    private int currentQuestionNum;
    private ArrayList<Question> questions;

    public Game(int numQuestions) {
        this.numQuestions = numQuestions;
        score = 0;
        currentQuestionNum = 0;
        questions = new ArrayList<Question>(numQuestions);
    }

    public int getScore() {
        return score;
    }

    public void add_points(int points) {
        score = score + points;
    }

    public int getNumQuestions() {
        return numQuestions;
    }

    public int getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    public void newQuestion(SQLiteDatabase db, ImageButton optionA_IM, TextView optionA_TV,
                             ImageButton optionB_IM, TextView optionB_TV) {
        Question q = new Question(db, optionA_IM, optionA_TV, optionB_IM, optionB_TV);
        questions.add(q);
        currentQuestionNum++;
    }

    public Question getCurrentQuestion() {
        return questions.get(currentQuestionNum - 1);
    }
}
