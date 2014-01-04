package ua.cooperok.foxhunting.gui;

import java.util.ArrayList;

import ua.cooperok.foxhunting.db.FoxhuntingDatabase;
import ua.cooperok.foxhunting.db.RecordsColumns;
import ua.cooperok.foxhunting.R;
import android.app.ListActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RecordsActivity extends ListActivity {

    private static final int RECORDS_LIMIT = 10;

    private FoxhuntingDatabase mDbAdapter;

    private Cursor mRecordsCursor;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbAdapter = new FoxhuntingDatabase(getApplicationContext());
        mRecordsCursor = getRecords();
        initListAdapter();
        ListView lv = getListView();
        lv.setTextFilterEnabled(true);
        setContentView(R.layout.records_layout);
    }

    private void initListAdapter() {
        setListAdapter(new ArrayAdapter<String>(this, R.layout.records_list_item, getRecordsArrayList()));
    }

    private Cursor getRecords() {
        return mDbAdapter.getRecords(RECORDS_LIMIT);
    }

    private ArrayList<String> getRecordsArrayList() {
        ArrayList<String> recordsList = new ArrayList<String>();
        if (mRecordsCursor.moveToFirst()) {
            do {
                recordsList.add(
                           mRecordsCursor.getString(mRecordsCursor.getColumnIndex(RecordsColumns.USERNAME))
                                   + " / " + mRecordsCursor.getInt(mRecordsCursor.getColumnIndex(RecordsColumns.STEPS))
                           );
            } while (mRecordsCursor.moveToNext());
        }

        return recordsList;
    }

}