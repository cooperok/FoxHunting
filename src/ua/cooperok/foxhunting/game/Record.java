package ua.cooperok.foxhunting.game;

import java.sql.Date;

/**
 * Class that represent one single record of all records
 */
public class Record {

    /**
     * Primary key of record
     */
    private long mId;

    /**
     * Date of record's creation
     */
    private long mCreated;

    /**
     * Count of steps
     */
    private int mSteps;

    public Record() {
    }

    public Record(long id, int steps, long created) {
        mId = id;
        mSteps = steps;
        mCreated = created;
    }

    /**
     * Returns id of record
     */
    public long getId() {
        return mId;
    }

    /**
     * Returns count of steps
     */
    public int getSteps() {
        return mSteps;
    }

    /**
     * Returns created date
     */
    public long getCreated() {
        return mCreated;
    }
    
    public String getCreatedAsString() {
        return new Date(mCreated).toString();
    }

}