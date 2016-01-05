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

    private SQLiteDatabase _db;

    Game game;

    ProgressBar qProgressBar;
    CountDownTimer mCountDownTimer;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    public void startGame(View view) {
        setContentView(R.layout.activity_question);
        game = new Game(6);
        displayScore();

        //Abrimos la base de datos 'DBWorld' en modo lectura
        DataBaseHelper myDbHelper = new DataBaseHelper(this, "DBWorld", null, 1);
        _db = myDbHelper.getWritableDatabase();

        nextQuestion(view);
    }

    public void nextQuestion(View view) {
        enableImageButtons();
        if (game.get_current_question_num() < game.get_numQuestions()) {
            ImageButton optionA_IM = (ImageButton) findViewById(R.id.option_a_image_button);
            TextView optionA_TV = (TextView) findViewById(R.id.option_a_name);
            ImageButton optionB_IM = (ImageButton) findViewById(R.id.option_b_image_button);
            TextView optionB_TV = (TextView) findViewById(R.id.option_b_name);
            questionProgressBar();
            game.new_question(_db, optionA_IM, optionA_TV, optionB_IM, optionB_TV);
        } else {
            gameFinished();
        }
    }

    private void questionProgressBar() {
        qProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        counter = 0;
        int timeToAnswer = 10; // in seconds
        int secondParts = 20;
        long countDownInterval = (long) (1.0/secondParts)*1000;
        qProgressBar.setProgress(counter);
        qProgressBar.setMax(timeToAnswer*secondParts);
        mCountDownTimer = new CountDownTimer(timeToAnswer*1000, 50) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Log.v("Log_tag", "Tick of Progress" + counter + millisUntilFinished);
                counter++;
                qProgressBar.setProgress(counter);
            }

            @Override
            public void onFinish() {
                counter++;
                qProgressBar.setProgress(counter);

                // Show a time out message as a toast
                Context context = getBaseContext();
                CharSequence timeOutMessage = "Time out!";
                int toastDuration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, timeOutMessage, toastDuration);
                toast.show();

                nextQuestion(getCurrentFocus());
            }
        };
        mCountDownTimer.start();
    }

    public void gameFinished() {
        setContentView(R.layout.activity_end);

        //Cerramos la base de datos
        if(_db != null) {
            _db.close();
        }

        TextView end_message = (TextView) findViewById(R.id.end_message);
        TextView finalScoreMessage = (TextView) findViewById(R.id.final_score_message);
        double score = (double) game.get_score() / game.get_numQuestions();
        finalScoreMessage.setText(game.get_score() + " / " + game.get_numQuestions());
        if (score < 0.5) {
            end_message.setText("Best luck next time! try harder!");
        }
        else if (score < 0.7) {
            end_message.setText("Give me 5!");
        } else {
            end_message.setText("Wooow! That was pretty amazing!");
        }
    }

    public void validateOptionA(View view) {
        disableImageButtons();
        mCountDownTimer.cancel();
        ImageButton optionA_ImageButton = (ImageButton) findViewById(R.id.option_a_image_button);
        Question q = game.get_current_question();
        if (q.get_answer().equals(q.get_optionA())) {
            game.add_points(1);
            displayScore();
            rightAnswerEffect(optionA_ImageButton);
        } else {
            wrongAnswerEffect(optionA_ImageButton);
        }
        resetAnswerEffect(optionA_ImageButton);
        nextQuestion(view);
    }

    public void validateOptionB(View view) {
        disableImageButtons();
        mCountDownTimer.cancel();
        ImageButton optionB_ImageButton = (ImageButton) findViewById(R.id.option_b_image_button);
        Question q = game.get_current_question();
        if (q.get_answer().equals(q.get_optionB())) {
            game.add_points(1);
            displayScore();
            rightAnswerEffect(optionB_ImageButton);
        } else {
            wrongAnswerEffect(optionB_ImageButton);
        }
        resetAnswerEffect(optionB_ImageButton);
        nextQuestion(view);
    }

    private void rightAnswerEffect(ImageButton option_ImageButton) {
        option_ImageButton.getBackground().setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);

    }

    private void wrongAnswerEffect(ImageButton option_ImageButton) {
        option_ImageButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
    }

    private void resetAnswerEffect(ImageButton option_ImageButton) {
        option_ImageButton.getBackground().clearColorFilter();
    }

    private void disableImageButtons() {
        ImageButton optionA_ImageButton = (ImageButton) findViewById(R.id.option_a_image_button);
        ImageButton optionB_ImageButton = (ImageButton) findViewById(R.id.option_b_image_button);
        optionA_ImageButton.setEnabled(false);
        optionB_ImageButton.setEnabled(false);
    }

    private void enableImageButtons() {
        ImageButton optionA_ImageButton = (ImageButton) findViewById(R.id.option_a_image_button);
        ImageButton optionB_ImageButton = (ImageButton) findViewById(R.id.option_b_image_button);
        optionA_ImageButton.setEnabled(true);
        optionB_ImageButton.setEnabled(true);
    }

    /**
     * This method displays the new score on the screen while quizz is still alive.
     */
    private void displayScore() {
        TextView scoreTextView = (TextView) findViewById(R.id.score_view);
        scoreTextView.setText("Score: " + game.get_score() + "/" + game.get_numQuestions());
    }
}
