package ua.cooperok.foxhunting.gui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

    private Bitmap mFoxImage;

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
        mCellColorWithFox.setColor(Color.WHITE);
        mCellTextValueColor = new Paint();
        mCellTextValueColor.setColor(Color.BLACK);
        mCellVerticalPadding = getResources().getDimensionPixelOffset(R.dimen.game_field_cell_vertical_padding);
        mCellHorizontalPadding = getResources().getDimensionPixelOffset(R.dimen.game_field_cell_horizontal_padding);
        mFoxImage = BitmapFactory.decodeResource(getResources(), R.drawable.fox);
    }

    public void initGameField() {
        mFields = new FieldsCollection();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int fieldsWidth = mFields.getWidth();
        int fieldsHeight = mFields.getHeight();

        mCellWidth = (widthSize - (mCellHorizontalPadding * (fieldsWidth - 1))) / fieldsWidth;
        mCellHeight = (heightSize - (mCellVerticalPadding * (fieldsHeight - 1))) / fieldsHeight;

        mCellWidth = mCellHeight = Math.min(mCellWidth, mCellHeight);

        float gameFieldWidth = mCellWidth * fieldsWidth + mCellHorizontalPadding * (fieldsWidth - 1);
        float gameFieldHeight = mCellWidth * fieldsWidth + mCellHorizontalPadding * (fieldsWidth - 1);

        int horizontalSpacing = (int) ((widthSize - gameFieldWidth) / 2);
        int verticalSpacing = (int) ((heightSize - gameFieldHeight) / 2);

        setPadding(horizontalSpacing, verticalSpacing, horizontalSpacing, verticalSpacing);

        setMeasuredDimension(widthSize, heightSize);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float cellLeft;
        float cellTop;

        int fieldsWidth = mFields.getWidth();
        int fieldsHeight = mFields.getHeight();

        int leftOffset = getPaddingLeft();
        int topOffset = getPaddingTop();

        // Drawing cells in accordance with the size of game field
        for (int row = 0; row < fieldsWidth; row++) {
            for (int col = 0; col < fieldsHeight; col++) {
                Field field = mFields.getField(row, col);
                cellLeft = leftOffset + row * mCellWidth + (row == 0 ? 0 : mCellHorizontalPadding * row);
                cellTop = topOffset + col * mCellHeight + (col == 0 ? 0 : mCellVerticalPadding * col);

                // If field was scanned draw cell, depends on whether contains it fox or no
                if (field.isScanned()) {
                    if (field.isContainsFox()) {
                        canvas.drawRect(
                                        cellLeft,
                                        cellTop,
                                        cellLeft + mCellWidth,
                                        cellTop + mCellHeight,
                                        mCellColorWithFox
                              );

                        drawFox(canvas, cellLeft, cellTop);

                    } else {
                        canvas.drawRect(
                                        cellLeft,
                                        cellTop,
                                        cellLeft + mCellWidth,
                                        cellTop + mCellHeight,
                                        mDefaultCellColor
                              );

                        String value = String.valueOf(field.getValue());

                        // Measuring text position
                        int xPos = (int) (cellLeft + (mCellWidth / 2) -
                                   (mCellTextValueColor.measureText(value) / 2));
                        int yPos = (int) (cellTop + ((mCellHeight / 2) -
                                   ((mCellTextValueColor.descent() + mCellTextValueColor.ascent()) / 2)));
                        mCellTextValueColor.setTextSize(getResources().getDimensionPixelOffset(R.dimen.game_field_value_text_size));
                        canvas.drawText(value, xPos, yPos, mCellTextValueColor);
                    }
                } else {

                    // Drawing empty cell
                    canvas.drawRect(
                                    cellLeft,
                                    cellTop,
                                    cellLeft + mCellWidth,
                                    cellTop + mCellHeight,
                                    mDefaultCellColor
                          );
                }
            }
        }

        // Drawing cell as touched only if it wasn't scanned before
        if (mTouchedField != null && !mTouchedField.isScanned()) {
            Point position = mTouchedField.getPosition();
            canvas.drawRect(
                            leftOffset + position.x * mCellWidth + position.x * mCellHorizontalPadding,
                            topOffset + position.y * mCellHeight + position.y * mCellVerticalPadding,
                            leftOffset + (position.x * mCellWidth) + mCellWidth + position.x * mCellHorizontalPadding,
                            topOffset + (position.y * mCellHeight) + mCellHeight + position.y * mCellVerticalPadding,
                            mTouchedCellColor
                  );
        }
    }

    private void drawFox(Canvas canvas, float left, float top) {

        canvas.drawBitmap(
                          Bitmap.createScaledBitmap(mFoxImage, (int) mCellWidth, (int) mCellHeight, false),
                          left,
                          top,
                          null
              );
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
                    if (mSelectedField.isContainsFox()) {
                        dispatchFoxFound();
                    }
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
        Field field = null;

        if (x > getPaddingLeft() && y > getPaddingTop()) {
            int left = getPaddingLeft() + (int) mCellWidth;
            int top = getPaddingTop() + (int) mCellHeight;
            int row = 0, col = 0, sizeX = mFields.getWidth(), sizeY = mFields.getHeight();

            // Calculating x position by adding width of one cell
            while (x > left) {
                left += mCellHorizontalPadding + (int) mCellWidth;
                if (row == sizeX) {
                    row = -1;
                    break;
                } else {
                    row++;
                }
            }

            // Calculating y position by adding height of one cell
            while (y > top) {
                top += mCellVerticalPadding + (int) mCellHeight;
                if (col == sizeY) {
                    col = -1;
                    break;
                } else {
                    col++;
                }
            }

            field = mFields.getField(row, col);
        }

        return field;
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

    public void dispatchFoxFound() {
        if (mOnStepListener != null) {
            mOnStepListener.onFoxFound();
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

        public void onFoxFound();
    }

}