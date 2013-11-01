package ua.cooperok.foxhunting.game;

import android.graphics.Point;
import android.util.Log;

public class FieldsCollection {

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

    /**
     * Creates collections of empty fields, without placing foxes on them
     */
    private Field[][] createEmptyCollection() {
        Field[][] emptyCells = new Field[getWidth()][getHeight()];
        for (int x = 0; x < getHeight(); x++) {
            for (int y = 0; y < getWidth(); y++) {
                emptyCells[x][y] = new Field(x, y);
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
            field = mFields[x][y];
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

}
