package ua.cooperok.foxhunting.gui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import ua.cooperok.foxhunting.R;
import ua.cooperok.foxhunting.gui.views.GameFieldView;
import ua.cooperok.foxhunting.gui.views.GameFieldView.OnStepListener;

public class GameActivity extends Activity {

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

        mFoxesCount = mGameView.getFoxesCount();
        mStepsCount = 0;

        setStepsCount();
        setFoxesCount();
    }

    private void setStepsCount() {
        mStepsCountView.setText(String.valueOf(mStepsCount));
    }

    private void setFoxesCount() {
        mFoxesCountView.setText(String.valueOf(mFoxesCount));
    }

    private class GameStepListener implements OnStepListener {
        @Override
        public void onStep() {
            ++mStepsCount;
            setStepsCount();
        }
    }

}
