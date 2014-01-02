package ua.cooperok.foxhunting.game;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

public class Field implements Parcelable {

    public static final int FOX_VALUE = -1;

    /**
     * Value of field, which contain count of foxes from visible fields
     */
    private int mValue;

    /**
     * Does this field contains fox
     */
    private boolean mContainFox;

    /**
     * Was this field scanned, and its value had been shown
     */
    private boolean mIsScanned;

    private Point mPosition;

    public Field(int x, int y) {
        mPosition = new Point(x, y);
        mContainFox = false;
        mValue = 0;
    }

    public Field(Parcel in) {
        mPosition = new Point(in.readInt(), in.readInt());
        mValue = in.readInt();
        mContainFox = in.readByte() != 0;
        mIsScanned = in.readByte() != 0;
    }

    void setFox() {
        mContainFox = true;
    }

    public Field(Point position) {
        mPosition = position;
    }

    public boolean isScanned() {
        return mIsScanned;
    }

    public boolean isContainsFox() {
        return mContainFox;
    }

    public int getValue() {
        return mValue;
    }

    /**
     * Sets value of field
     * 
     * @param value
     *            count of foxes in visible fields, can be a Field.FOX_VALUE, which means, that this field contains fox
     */
    public void setValue(int value) {
        mValue = value;
        mContainFox = value == FOX_VALUE;
    }

    /**
     * Means that this field was scanned and its value had been shown
     */
    public void setScanned() {
        mIsScanned = true;
    }

    public Point getPosition() {
        return mPosition;
    }

    @Override
    public boolean equals(Object o) {
        boolean equals = false;
        if (o != null) {
            try {
                Point position = ((Field) o).getPosition();
                equals = position.equals(mPosition);
            } catch (ClassCastException e) {
                // don't do anything, anyway equals is false
            }
        }
        return equals;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mPosition.x);
        dest.writeInt(mPosition.y);
        dest.writeInt(mValue);
        dest.writeByte((byte) (mContainFox ? 1 : 0));
        dest.writeByte((byte) (mIsScanned ? 1 : 0));
    }

    public static final Parcelable.Creator<Field> CREATOR = new Parcelable.Creator<Field>() {
        public Field createFromParcel(Parcel in) {
            return new Field(in);
        }

        public Field[] newArray(int size) {
            return new Field[size];
        }
    };
}
