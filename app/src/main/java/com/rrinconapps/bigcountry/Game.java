package com.rrinconapps.bigcountry;

import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ricardo on 19/12/2015.
 */
public class Game {
    private int _numQuestions;
    private int _score;
    private int _current_question_num;
    private ArrayList<Question> _questions;

    public Game(int numQuestions) {
        _numQuestions = numQuestions;
        _score = 0;
        _current_question_num = 0;
        _questions = new ArrayList<Question>(_numQuestions);
    }

    public int get_score() {
        return _score;
    }

    public void add_points(int points) {
        _score = _score + points;
    }

    public int get_numQuestions() {
        return _numQuestions;
    }

    public int get_current_question_num() {
        return _current_question_num;
    }

    public void new_question(SQLiteDatabase db, ImageButton optionA_IM, TextView optionA_TV,
                             ImageButton optionB_IM, TextView optionB_TV) {
        Question q = new Question(db, optionA_IM, optionA_TV, optionB_IM, optionB_TV);
        _questions.add(q);
        _current_question_num++;
    }

    public Question get_current_question() {
        return _questions.get(_current_question_num - 1);
    }
}
