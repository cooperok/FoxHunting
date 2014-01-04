package ua.cooperok.foxhunting.gui;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import ua.cooperok.foxhunting.R;
import ua.cooperok.foxhunting.db.FoxhuntingDatabase;
import ua.cooperok.foxhunting.gui.views.GameFieldView;
import ua.cooperok.foxhunting.gui.views.GameFieldView.OnStepListener;
import ua.cooperok.foxhunting.helpers.Morphology;

public class GameActivity extends Activity {

    private final static String DEFAULT_USER_NAME = "username";

    /**
     * Keys uses in {@link GameActivity#onSaveInstanceState(Bundle)} and
     * {@link GameActivity#onRestoreInstanceState(Bundle)} methods
     */
    private final static String FOXES_COUNT_KEY = "foxes_count";
    private final static String STEPS_COUNT_KEY = "steps_count";
    private final static String GAME_PARCELABLE_KEY = "game_parcelable";

    private TextView mStepsCountView;
    private TextView mFoxesCountView;
    private GameFieldView mGameView;

    private int mFoxesCount;
    private int mStepsCount;

    private OnStepListener mGameStepsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        mStepsCountView = (TextView) findViewById(R.id.steps_count);
        mFoxesCountView = (TextView) findViewById(R.id.foxes_count);
        mGameView = (GameFieldView) findViewById(R.id.game_field);

        mGameStepsListener = new GameStepListener();
        mGameView.setOnStepListener(mGameStepsListener);

        // If activity was just created
        if (savedInstanceState == null) {
            mGameView.initGameField();
            mFoxesCount = mGameView.getFoxesCount();
            mStepsCount = 0;
            setStepsCount();
            setFoxesCount();
        }
    }

    private void setStepsCount() {
        mStepsCountView.setText(String.valueOf(mStepsCount));
    }

    private void setFoxesCount() {
        mFoxesCountView.setText(String.valueOf(mFoxesCount));
    }

    private void onGameEnd() {
        // Saving record to database
        FoxhuntingDatabase db = new FoxhuntingDatabase(this);
        // @TODO user name must be in settings
        db.insertRecord(mStepsCount, DEFAULT_USER_NAME);

        showDialog();
    }

    private void showDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        String title = getString(R.string.game_completed_dialog_title);
        alert.setTitle(title);

        String textRes = getString(R.string.game_completed_dialog_text);
        String text;
        String country = Locale.getDefault().getCountry();
        if (country.equalsIgnoreCase("ru")) {
            text = String.format(textRes, mStepsCount, Morphology.getWordEndingByNumber(mStepsCount, "ку", "ки", "ок"));
        } else if (country.equalsIgnoreCase("uk")) {
            text = String.format(textRes, mStepsCount, Morphology.getWordEndingByNumber(mStepsCount, "у", "и", ""));
        } else {
            text = String.format(textRes, mStepsCount);
        }
        
        final TextView textView = new TextView(this);
        textView.setPadding(10, 10, 10, 10);
        textView.setText(text);
        alert.setView(textView);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                // Starting records activity
                Intent intent = new Intent(GameActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });
        alert.show();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mFoxesCount = savedInstanceState.getInt(FOXES_COUNT_KEY, 0);
        mStepsCount = savedInstanceState.getInt(STEPS_COUNT_KEY, 0);

        setStepsCount();
        setFoxesCount();
        mGameView.setParcelable(savedInstanceState.getParcelable(GAME_PARCELABLE_KEY));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(FOXES_COUNT_KEY, mFoxesCount);
        outState.putInt(STEPS_COUNT_KEY, mStepsCount);
        outState.putParcelable(GAME_PARCELABLE_KEY, mGameView.getParcelable());
    }

    private class GameStepListener implements OnStepListener {
        @Override
        public void onStep() {
            ++mStepsCount;
            setStepsCount();
        }

        @Override
        public void onFoxFound() {
            --mFoxesCount;
            setFoxesCount();
            if (mFoxesCount == 0) {
                onGameEnd();
            }
        }
    }

}