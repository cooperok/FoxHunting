package ua.cooperok.foxhunting.game;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class FieldsCollection implements Parcelable {

    private final static String TAG = "FieldsCollection";

    private Field[][] mFields;

    /**
     * Size of game field
     * 
     * @TODO I think the field not always can be standard size 10x10, user can change it for example 15x15, so i need to
     *       keep this value in SharedPreferenses
     */
    private int mWidth = 10;
    private int mHeight = 10;

    /**
     * Count of foxes in game field
     * 
     * @TODO Same as size of field, user can change count
     */
    private int mFoxesCount = 10;

    private int[] mFoxesPositions;

    public FieldsCollection() {
        mFields = createEmptyCollection();
        createFoxes();
        placeFoxesOnField();
    }

    public FieldsCollection(Parcel in) {
        mWidth = in.readInt();
        mHeight = in.readInt();
        mFoxesCount = in.readInt();

        in.readIntArray(mFoxesPositions);
        
        try {
            Parcelable[] fields = in.readParcelableArray(Field.class.getClassLoader());
            mFields = new Field[mHeight][mWidth];
            for (int i = 0, x = 0, y = 0, l = fields.length; i < l; ++i) {
                if (i % mHeight == 0) {
                    ++x;
                    y = 0;
                }
                mFields[x][y++] = (Field) fields[i];
            }
        } catch (ClassCastException e) {
            //Something went wrong wile trying to create Field object from Parcel 
            Log.e(TAG, e.getMessage());
            mFields = createEmptyCollection();
        } catch (ArrayIndexOutOfBoundsException e) {
            //Size of saved fields doesn't match 
            Log.e(TAG, e.getMessage());
            mFields = createEmptyCollection();
        }
    }

    /**
     * Creates collections of empty fields, without placing foxes on them
     */
    private Field[][] createEmptyCollection() {
        Field[][] emptyCells = new Field[mHeight][mWidth];
        for (int col = 0; col < getHeight(); col++) {
            for (int row = 0; row < getWidth(); row++) {
                emptyCells[col][row] = new Field(row, col);
            }
        }
        return emptyCells;
    }

    /**
     * Generate random positions of foxes in the field
     */
    private void createFoxes() {
        mFoxesPositions = new int[mFoxesCount];
        int fieldCount = getWidth() * getHeight() - 1;
        for (int i = 0; i < mFoxesCount; i++) {
            mFoxesPositions[i] = (int) (Math.random() * fieldCount);
            for (int j = 0; j < i; j++) {
                if (mFoxesPositions[i] == mFoxesPositions[j]) {
                    i--;
                    break;
                }
            }
        }
    }

    private void placeFoxesOnField() {
        for (int i = 0, l = mFoxesPositions.length; i < l; ++i) {
            Field field = getFieldByPosition(mFoxesPositions[i]);
            if (field != null) {
                field.setFox();
            }
        }
    }

    /**
     * Returns field by its position
     * 
     * @param position
     *            index number of field in whole game field. Calculates from top left field to right
     * @return
     *         Field if exists in position or null otherwise
     */
    private Field getFieldByPosition(int position) {
        int x = position % getWidth();
        int y = (int) Math.floor(position / getWidth());
        Field field = getField(x, y);
        return field;
    }

    /**
     * Returns field by its position
     * 
     * @param x
     *            coordinate
     * @param y
     *            coordinate
     */
    public Field getField(int x, int y) {
        Field field = null;
        try {
            field = mFields[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            Log.i(TAG, "Trying to get field with wrong coordinates (" + x + "," + y + ")");
        }
        return field;
    }

    /**
     * Calculates field value if it doesn't contains fox
     * 
     * @param field
     */
    public void calculateFieldValue(Field field) {
        if (!field.isContainsFox()) {
            field.setValue(getFieldValue(field));
        }
    }

    private int getFieldValue(Field field) {
        Point position = field.getPosition();
        Field tmpField;
        int value = 0, w = getWidth(), h = getHeight();

        // check vertical
        for (int y = 0; y < w; ++y) {
            if (y != position.y) {
                tmpField = getField(position.x, y);
                if (tmpField != null && tmpField.isContainsFox()) {
                    value++;
                }
            }
        }

        // check horizontal
        for (int x = 0; x < h; ++x) {
            if (x != position.x) {
                tmpField = getField(x, position.y);
                if (tmpField != null && tmpField.isContainsFox()) {
                    value++;
                }
            }
        }

        // check diagonal left top
        for (int x = position.x - 1, y = position.y - 1; x >= 0 && y >= 0; --x, --y) {
            tmpField = getField(x, y);
            if (tmpField != null && tmpField.isContainsFox()) {
                value++;
            }
        }

        // check diagonal right top
        for (int x = position.x + 1, y = position.y - 1; x < w && y >= 0; ++x, --y) {
            tmpField = getField(x, y);
            if (tmpField != null && tmpField.isContainsFox()) {
                value++;
            }
        }

        // check diagonal left bottom
        for (int x = position.x - 1, y = position.y + 1; x >= 0 && y < h; --x, ++y) {
            tmpField = getField(x, y);
            if (tmpField != null && tmpField.isContainsFox()) {
                value++;
            }
        }

        // check diagonal right bottom
        for (int x = position.x + 1, y = position.y + 1; x < w && y < h; ++x, ++y) {
            tmpField = getField(x, y);
            if (tmpField != null && tmpField.isContainsFox()) {
                value++;
            }
        }

        return value;
    }

    /**
     * @return
     *         Width of game field
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     * @return
     *         Height of game field
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     * @return
     *         Count of aviable foxes on game field
     */
    public int getFoxesCount() {
        return mFoxesCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        
        dest.writeInt(mWidth);
        dest.writeInt(mHeight);
        dest.writeInt(mFoxesCount);

        dest.writeIntArray(mFoxesPositions);
        
        //Making flatten array fields
        Parcelable [] fields = new Parcelable [mWidth*mHeight];
        for (int x = 0, i = 0; x < mHeight; x++) {
            for (int y = 0; y < mWidth; y++) {
                fields[i++] = mFields[x][y];
            }
        }
        dest.writeParcelableArray(fields, 0);
       
    }

    public static final Parcelable.Creator<FieldsCollection> CREATOR = new Parcelable.Creator<FieldsCollection>() {
        public FieldsCollection createFromParcel(Parcel in) {
            return new FieldsCollection(in);
        }

        public FieldsCollection[] newArray(int size) {
            return new FieldsCollection[size];
        }
    };

}