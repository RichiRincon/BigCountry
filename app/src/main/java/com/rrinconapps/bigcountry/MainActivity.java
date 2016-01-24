package com.rrinconapps.bigcountry;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {

    // Data base to hold countries information
    private SQLiteDatabase db;

    Game game;

    // Progress bar and countdown timer variables
    ProgressBar qProgressBar;
    CountDownTimer mCountDownTimer;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Set the initial view of the app.
     *
     * @param view Screen view
     */
    public void setInitialView(View view) {
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Start a new game.
     *
     * @param view Screen view
     */
    public void startGame(View view) {
        // Changes the initial view to a question view
        setContentView(R.layout.activity_question);

        // Starts a new game
        game = new Game(8);
        // Display the initial score base on the number of questions
        displayScore();

        // Uses a helper to create a data base 'DBWorld'
        DataBaseHelper myDbHelper = new DataBaseHelper(this, "DBWorld", null, 1);
        db = myDbHelper.getWritableDatabase();

        // Produces the first question
        nextQuestion(view);
    }

    /**
     * Produces a question.
     *
     * @param view Screen view
     */
    public void nextQuestion(View view) {
        enableImageButtons();

        // If we have not finished doing the planned number of questions
        if (game.getCurrentQuestionNum() < game.getNumQuestions()) {

            // Takes the visual elements to change for the question
            ImageButton optionA_IM = (ImageButton) findViewById(R.id.option_a_image_button);
            TextView optionA_TV = (TextView) findViewById(R.id.option_a_name);
            ImageButton optionB_IM = (ImageButton) findViewById(R.id.option_b_image_button);
            TextView optionB_TV = (TextView) findViewById(R.id.option_b_name);

            // Starts progress bar
            questionProgressBar();

            // Creates question from data base information
            game.newQuestion(db, optionA_IM, optionA_TV, optionB_IM, optionB_TV);
        } else {
            gameFinished();
        }
    }

    /**
     * Manage the progress bar for a question.
     */
    private void questionProgressBar() {
        // Takes the visual element
        qProgressBar = (ProgressBar) findViewById(R.id.progressbar);

        // Initializes parameter for the count down timer
        counter = 0;
        qProgressBar.setProgress(counter);
        int timeToAnswer = 10; // in seconds
        int secondParts = 20;
        //long countDownInterval = (long) (1.0/secondParts)*1000;
        qProgressBar.setMax(timeToAnswer*secondParts);

        mCountDownTimer = new CountDownTimer(timeToAnswer*1000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                counter++;
                qProgressBar.setProgress(counter);
            }

            @Override
            public void onFinish() {
                counter++;
                qProgressBar.setProgress(counter);

                // Show a time is up message as a toast
                Context context = getBaseContext();
                CharSequence timeOutMessage = getString(R.string.time_up_message);
                int toastDuration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, timeOutMessage, toastDuration);
                toast.show();

                // If time is up, it changes to next question
                nextQuestion(getCurrentFocus());
            }
        };
        mCountDownTimer.start();
    }

    /**
     * Produces the game finished view.
     */
    public void gameFinished() {
        // Changes the question view to the end view
        setContentView(R.layout.activity_end);

        // Closes data base
        if(db != null) {
            db.close();
        }

        // Displays the final score
        TextView finalScoreMessage = (TextView) findViewById(R.id.final_score_message);
        finalScoreMessage.setText(getString(R.string.final_score, game.getScore(),
                game.getNumQuestions()));

        // Displays an end message depending on the score
        TextView end_message = (TextView) findViewById(R.id.end_message);
        double score = (double) game.getScore() / game.getNumQuestions();
        if (score < 0.5) {
            end_message.setText(getString(R.string.end_message_fail));
        }
        else if (score < 0.7) {
            end_message.setText(getString(R.string.end_message_medium));
        } else {
            end_message.setText(getString(R.string.end_message_success));
        }
    }

    /**
     * Validates whether or not option A is the answer of the question.
     *
     * @param view Screen view
     */
    public void validateOptionA(View view) {
        answerSelected();

        ImageButton optionA_ImageButton = (ImageButton) findViewById(R.id.option_a_image_button);
        Question q = game.getCurrentQuestion();
        if (q.getAnswer().equals(Answers.OPTION_A)) {
            game.add_points(1);
            displayScore();
            rightAnswerEffect(optionA_ImageButton);
        } else {
            wrongAnswerEffect(optionA_ImageButton);
        }
        resetAnswerEffect(optionA_ImageButton);

        nextQuestion(view);
    }

    /**
     * Validates whether or not option B is the answer of the question.
     *
     * @param view Screen view
     */
    public void validateOptionB(View view) {
        answerSelected();

        ImageButton optionB_ImageButton = (ImageButton) findViewById(R.id.option_b_image_button);
        Question q = game.getCurrentQuestion();
        if (q.getAnswer().equals(Answers.OPTION_B)) {
            game.add_points(1);
            displayScore();
            rightAnswerEffect(optionB_ImageButton);
        } else {
            wrongAnswerEffect(optionB_ImageButton);
        }
        resetAnswerEffect(optionB_ImageButton);

        nextQuestion(view);
    }

    /**
     * Manages common actions to perform when an option answer has been selected.
     */
    private void answerSelected() {
        disableImageButtons();
        mCountDownTimer.cancel();
    }

    /**
     * Displays an effect on an image button corresponding to a right answer.
     *
     * @param option_ImageButton which displays the effect
     */
    private void rightAnswerEffect(ImageButton option_ImageButton) {
        option_ImageButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

    }

    /**
     * Displays an effect on an image button corresponding to a wrong answer.
     *
     * @param option_ImageButton which displays the effect
     */
    private void wrongAnswerEffect(ImageButton option_ImageButton) {
        option_ImageButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
    }

    /**
     * Resets any possible effects on an image button.
     *
     * @param option_ImageButton which resets its effects
     */
    private void resetAnswerEffect(ImageButton option_ImageButton) {
        option_ImageButton.getBackground().clearColorFilter();
    }

    /**
     * Disable the image buttons to answer the question.
     */
    private void disableImageButtons() {
        ImageButton optionA_ImageButton = (ImageButton) findViewById(R.id.option_a_image_button);
        ImageButton optionB_ImageButton = (ImageButton) findViewById(R.id.option_b_image_button);
        optionA_ImageButton.setEnabled(false);
        optionB_ImageButton.setEnabled(false);
    }

    /**
     * Enable the image buttons to answer the question.
     */
    private void enableImageButtons() {
        ImageButton optionA_ImageButton = (ImageButton) findViewById(R.id.option_a_image_button);
        ImageButton optionB_ImageButton = (ImageButton) findViewById(R.id.option_b_image_button);
        optionA_ImageButton.setEnabled(true);
        optionB_ImageButton.setEnabled(true);
    }

    /**
     * This method displays the new score on the screen while quiz is still alive.
     */
    private void displayScore() {
        TextView scoreTextView = (TextView) findViewById(R.id.score_view);
        scoreTextView.setText(getString(R.string.score, game.getScore(), game.getNumQuestions()));
    }

    /**
     * This method displays a summary of the questions in a game.
     *
     * @param view Screen view
     */
    public void gameSummary(View view) {
        setContentView(R.layout.activity_game_summary);

        String gameSummaryMessage = "";
        for (int i = 0; i < game.getNumQuestions(); i++) {
            gameSummaryMessage += "\n" + getString(R.string.question_summary, i+1,
                    game.getQuestion(i).getOptionA().getName(),
                    game.getQuestion(i).getOptionB().getName()) + "\n";
        }

        TextView questionsSummaryTextView = (TextView) findViewById(R.id.questions_summary_text);
        questionsSummaryTextView.setText(gameSummaryMessage);
    }

}
