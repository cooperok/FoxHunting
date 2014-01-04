package ua.cooperok.foxhunting.gui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import ua.cooperok.foxhunting.R;
import ua.cooperok.foxhunting.gui.GameActivity;
import ua.cooperok.foxhunting.gui.RecordsActivity;

public class MainActivity extends Activity implements OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        addListeners();
    }

    protected void addListeners() {
        findViewById(R.id.button_new).setOnClickListener(this);
        findViewById(R.id.button_records).setOnClickListener(this);
        findViewById(R.id.button_settings).setOnClickListener(this);
        findViewById(R.id.button_about).setOnClickListener(this);
    }

    /**
     * Listener on buttons click
     */
    public void onClick(View v) {
        switch (v.getId()) {
        case R.id.button_new:
            Intent intent = new Intent(this, GameActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            break;

        case R.id.button_records:
            intent = new Intent(this, RecordsActivity.class);
            startActivity(intent);
            break;

        // @TODO implement every listener
        default:

            Toast.makeText(getApplicationContext(), ((Button) v).getText(),
                           Toast.LENGTH_SHORT).show();

            break;
        }
    }

}
