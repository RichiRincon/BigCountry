package com.rrinconapps.bigcountry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Models a game in Big Country app.
 *
 * @author Ricardo Rincon
 * @since 2015-12-19
 */
public class Game {
    private int numQuestions;
    private int isChallenge;
    private int score;
    private int lives;
    private int timeToAnswerQuestion; // in seconds
    private int currentQuestionNum;
    private ArrayList<Question> questions;

    /**
     * Creates a game.
     *
     * @param numQuestions in the game
     */
    public Game(int numQuestions) {
        this.numQuestions = numQuestions;
        isChallenge = 0;
        score = 0;
        lives = 0;
        currentQuestionNum = 0;
        timeToAnswerQuestion = 10;
        questions = new ArrayList<Question>(numQuestions);
    }
    public Game() {
        numQuestions = 1;
        isChallenge = 1;
        score = 0;
        lives = 2;
        currentQuestionNum = 0;
        timeToAnswerQuestion = 10;
        questions = new ArrayList<Question>(numQuestions);
    }

    /**
     * Gets the time the user has to answer a question.
     * @return the current time to answer a question
     */
    public int getTimeToAnswerQuestion() {
        return timeToAnswerQuestion;
    }

    /**
     * Gets whether or not the game is a challenge.
     * @return 1 if the game is a challenge; 0 in any other cases
     */
    public int getIsChallenge() {
        return isChallenge;
    }

    /**
     * Gets the number of points of the user in the game.
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the number of lives of the user in the game.
     * @return the number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Subtracts a life.
     */
    public void loseLife() {
        if (lives > 0) {
            lives = lives - 1;
        }
    }

    /**
     * Adds points to the score.
     *
     * @param points Number of points to add
     */
    public void addPoints(int points) {
        score = score + points;
    }

    /**
     * Gets the total number of questions in the game.
     * @return the number of questions
     */
    public int getNumQuestions() {
        if (isChallenge == 0) {
            return numQuestions;
        } else {
            return questions.size();
        }
    }

    /**
     * Gets which the current question's number is.
     * @return the number of the current question
     */
    public int getCurrentQuestionNum() {
        return currentQuestionNum;
    }

    /**
     * Creates a new question.
     *
     * @param db Data base where obtain the information of the question
     * @param optionA_IM ImageButton to show the option A flag
     * @param optionA_TV TextView to display option A name
     * @param optionB_IM ImageButton to show the option B flag
     * @param optionB_TV TextView to display option B name
     */
    public void newQuestion(SQLiteDatabase db, ImageButton optionA_IM, TextView optionA_TV,
                             ImageButton optionB_IM, TextView optionB_TV) {
        Question q = new Question(db);

        // Sets option A information to show
        optionA_TV.setText(q.getOptionA().getName());
        optionA_IM.setImageResource(q.getOptionA().getFlag_id());

        // Sets option B information to show
        optionB_TV.setText(q.getOptionB().getName());
        optionB_IM.setImageResource(q.getOptionB().getFlag_id());

        // Stores the question
        questions.add(q);
        // Advances current question counter
        currentQuestionNum++;
    }

    /**
     * Gets the current question.
     * @return the question object
     */
    public Question getCurrentQuestion() {
        return questions.get(currentQuestionNum - 1);
    }

    /**
     * Sets if the current question has been correctly answered.
     * @param isRight true if it has been correctly answered, false if not
     */
    public void setCurrentQuestionIsRight(boolean isRight) {
        questions.get(currentQuestionNum - 1).setIsRight(isRight);
    }

    /**
     * Gets the question at a given index.
     * @param index Position in the ArrayList of the question we want
     * @return the question object
     */
    public Question getQuestion(int index) {
        return questions.get(index);
    }

    /**
     * Selects an end message for the game depending on the score.
     *
     * @param context Activity context
     * @return the appropriate end message
     */
    public String getEndGameMessage(Context context) {
        String endGameMessage;
        double score = (double) getScore() / getNumQuestions();

        if (score < 0.5) {
            endGameMessage = context.getString(R.string.end_message_fail);
        }
        else if (score < 0.7) {
            endGameMessage = context.getString(R.string.end_message_medium);
        } else {
            endGameMessage = context.getString(R.string.end_message_success);
        }

        return endGameMessage;
    }

    /**
     * Creates the game summary.
     *
     * @param context Activity context
     * @return the game summary message
     */
    public String getGameSummary(Context context) {
        String gameSummaryMessage = "";
        for (int i = 0; i < currentQuestionNum; i++) {
            Question q = questions.get(i);
            String country1;
            String country2;
            String isRight;

            // Sets the country answer to upper case
            if (q.getAnswer()== Answers.OPTION_A) {
                country1 = q.getOptionA().getName().toUpperCase();
                country2 = q.getOptionB().getName();
            }
            else {
                country1 = q.getOptionA().getName();
                country2 = q.getOptionB().getName().toUpperCase();
            }

            if (q.isRight()) {
                isRight = "\u2713"; // Unicode character for right answer
            }
            else {
                isRight = "\u2716"; // Unicode character for wrong answer
            }

            gameSummaryMessage += "\n" + context.getString(R.string.question_summary, i + 1,
                    country1, country2) + "  : " + isRight + "\n";
        }
        return gameSummaryMessage;
    }

}
