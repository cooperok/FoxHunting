package ua.cooperok.foxhunting.gui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import ua.cooperok.foxhunting.R;
import ua.cooperok.foxhunting.game.Field;
import ua.cooperok.foxhunting.game.FieldsCollection;

/**
 * View creating and managing game field
 * 
 * @author cooperok
 * 
 */
public class GameFieldView extends View {

    private Paint mDefaultCellColor;
    private Paint mTouchedCellColor;
    private Paint mCellColorWithFox;
    private Paint mCellTextValueColor;

    private FieldsCollection mFields;

    private int mCellVerticalPadding;
    private int mCellHorizontalPadding;

    private float mCellWidth;
    private float mCellHeight;

    private Field mTouchedField;
    private Field mSelectedField;

    private OnStepListener mOnStepListener;

    public GameFieldView(Context context) {
        super(context);
        init();
    }

    public GameFieldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GameFieldView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mDefaultCellColor = new Paint();
        mDefaultCellColor.setColor(Color.WHITE);
        mTouchedCellColor = new Paint();
        mTouchedCellColor.setColor(Color.DKGRAY);
        mCellColorWithFox = new Paint();
        mCellColorWithFox.setColor(Color.YELLOW);
        mCellTextValueColor = new Paint();
        mCellTextValueColor.setColor(Color.BLACK);
        mCellVerticalPadding = getResources().getDimensionPixelOffset(R.dimen.game_field_cell_vertical_padding);
        mCellHorizontalPadding = getResources().getDimensionPixelOffset(R.dimen.game_field_cell_horizontal_padding);
    }

    public void initGameField() {
        mFields = new FieldsCollection();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        mCellWidth = (widthSize - getPaddingLeft() - getPaddingRight()) / mFields.getWidth();
        mCellHeight = (heightSize - getPaddingLeft() - getPaddingRight()) / mFields.getHeight();

        setMeasuredDimension(widthSize, heightSize);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cellLeft;
        float cellTop;

        // Drawing cells in accordance with the size of game field
        for (int row = 0; row < mFields.getWidth(); row++) {
            for (int col = 0; col < mFields.getHeight(); col++) {
                Field field = mFields.getField(row, col);
                cellLeft = (row * mCellWidth);
                cellTop = (col * mCellHeight);

                // If field was scanned draw cell, depends on whether contains it fox or no
                if (field.isScanned()) {
                    if (field.isContainsFox()) {
                        canvas.drawRect(
                                        cellLeft,
                                        cellTop,
                                        cellLeft + mCellWidth - mCellHorizontalPadding,
                                        cellTop + mCellHeight - mCellVerticalPadding,
                                        mCellColorWithFox
                              );
                    } else {
                        canvas.drawRect(
                                        cellLeft,
                                        cellTop,
                                        cellLeft + mCellWidth - mCellHorizontalPadding,
                                        cellTop + mCellHeight - mCellVerticalPadding,
                                        mDefaultCellColor
                              );

                        String value = String.valueOf(field.getValue());

                        // Measuring text position
                        int xPos = (int) (cellLeft + (mCellWidth / 2) -
                                   (mCellTextValueColor.measureText(value) / 2));
                        int yPos = (int) (cellTop + ((mCellHeight / 2) -
                                   ((mCellTextValueColor.descent() + mCellTextValueColor.ascent()) / 2)));
                        canvas.drawText(value, xPos, yPos, mCellTextValueColor);
                    }
                } else {

                    // Drawing empty cell
                    canvas.drawRect(
                                    cellLeft,
                                    cellTop,
                                    cellLeft + mCellWidth - mCellHorizontalPadding,
                                    cellTop + mCellHeight - mCellVerticalPadding,
                                    mDefaultCellColor
                          );
                }
            }
        }

        // Drawing cell as touched only if it wasn't scanned before
        if (mTouchedField != null && !mTouchedField.isScanned()) {
            Point position = mTouchedField.getPosition();
            canvas.drawRect(
                            position.x * mCellWidth,
                            position.y * mCellHeight,
                            (position.x * mCellWidth) + mCellWidth - mCellHorizontalPadding,
                            (position.y * mCellHeight) + mCellHeight - mCellVerticalPadding,
                            mTouchedCellColor
                  );
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (event.getAction()) {
        // case MotionEvent.ACTION_MOVE:
        case MotionEvent.ACTION_DOWN:
            mTouchedField = getFieldAtPoint(x, y);
            postInvalidate();
            break;
        case MotionEvent.ACTION_UP:
            mSelectedField = getFieldAtPoint(x, y);

            if (mTouchedField != null) {
                if (!mTouchedField.equals(mSelectedField)) {
                    mSelectedField = null;
                } else if (!mSelectedField.isScanned()) {
                    mFields.calculateFieldValue(mSelectedField);
                    mSelectedField.setScanned();
                    dispatchStepDone();
                }
            }

            mTouchedField = null;
            postInvalidate();
            break;
        case MotionEvent.ACTION_CANCEL:
            mTouchedField = null;
            break;
        }

        return true;
    }

    /**
     * Returns field by point on view, where touch was
     * 
     * @param x
     *            coordinate of x axis
     * @param y
     *            coordinate of y axis
     * @return
     */
    private Field getFieldAtPoint(int x, int y) {
        int row = (int) ((x - getPaddingLeft()) / mCellWidth);
        int col = (int) ((y - getPaddingTop()) / mCellHeight);
        return mFields.getField(row, col);
    }

    /**
     * Set up onStepListener
     * 
     * @param listener
     */
    public void setOnStepListener(OnStepListener listener) {
        mOnStepListener = listener;
    }

    /**
     * @return
     *         Count of foxes on game field
     */
    public int getFoxesCount() {
        return mFields.getFoxesCount();
    }

    public void dispatchStepDone() {
        if (mOnStepListener != null) {
            mOnStepListener.onStep();
        }
    }

    public Parcelable getParcelable() {
        return mFields;
    }

    public void setParcelable(Parcelable parcelableFields) {
        mFields = (FieldsCollection) parcelableFields;
    }

    /**
     * Interface declares methods which must react on every new step on game field
     * 
     * @author cooperok
     * 
     */
    public interface OnStepListener {
        public void onStep();
    }

}