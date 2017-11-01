package erostamas.brewer.Views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import erostamas.brewer.R;

import static android.graphics.Color.parseColor;
import static android.view.View.MeasureSpec.getSize;

/**
 * Created by etamero on 2017.09.20..
 */

public class DataDisplay extends View {
    private String _unit = new String();
    private String _name = new String();

    private String _value = new String();

    public DataDisplay(Context context) {
        super(context);
        init(null);
    }

    public DataDisplay(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Display, 0, 0);
        try {
            _name = ta.getString(R.styleable.Display_name);
            if (ta.hasValue(R.styleable.Display_unit)) {
                _unit = ta.getString(R.styleable.Display_unit);
            }
        } finally {
            ta.recycle();
        }
        init(attrs);
    }

    public DataDisplay(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
    }

    public void set(String value) {
        _value = value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawFrame(canvas);
        if (!_value.isEmpty()) {
            drawValue(canvas);
        }
        drawName(canvas);
    }

    private void drawFrame(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint =  new Paint();
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(parseColor("#2C3E50"));
        canvas.drawRect(0, 0, x, y, paint);
    }

    void drawValue(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setTextSize(y / 4);
        canvas.drawText(_value, x / 2 - paint.measureText(_value) / 2, y / 2, paint);
        paint.setTextSize(y / 7);
        canvas.drawText(_unit, x / 2 - paint.measureText(_unit) / 2, y * 5 / 7, paint);
    }

    void drawName(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint = new Paint();
        if (!_value.isEmpty()) {
            paint.setColor(Color.WHITE);
        } else {
            paint.setColor(Color.GRAY);
        }

        paint.setTextSize(y / 10);
        canvas.drawText(_name, 10, y / 10, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //int width = getSize(widthMeasureSpec) / 2;
        //int height = getSize(heightMeasureSpec);
        //setMeasuredDimension(width, height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
