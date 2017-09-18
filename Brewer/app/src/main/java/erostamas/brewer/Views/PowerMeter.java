package erostamas.brewer.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static android.graphics.Color.parseColor;
import static android.view.View.MeasureSpec.getSize;

/**
 * Created by etamero on 2017.09.18..
 */

public class PowerMeter extends View {
    private static final String _unit = "%";

    private double _value = 100;

    public PowerMeter(Context context) {
        super(context);
        init(null);
    }

    public PowerMeter(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PowerMeter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
    }

    public void set(double value) {
        _value = value;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRectangle(canvas);
        //drawValue(canvas);
    }

    private void drawRectangle(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint =  new Paint();
        int [] colors = {parseColor("#FF0000"), parseColor("#00FF33")};
        float[] positions = {0, 1};
        paint.setColor(parseColor("#2C3E50"));

        Paint strokePaint = new Paint();
        strokePaint.setColor(Color.BLACK);
        strokePaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < 100; i++) {
            canvas.drawRect(0, y - y / 100 * i, x, y - y / 100 * (i + 1), paint);
            canvas.drawRect(0, y - y / 100 * i, x, y - y / 100 * (i + 1), strokePaint);
        }

        paint.setShader(new LinearGradient(0, 0, 0, y, colors, positions, Shader.TileMode.MIRROR));

        //for (int i = 0; i < _value; i++) {
        //    canvas.drawRect(0, 0, x, y / 100 * i, paint);
        //}
        //canvas.drawRect(0, (float)_value / 100 * y, x, y, paint);
    }

    void drawValue(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setTextSize(y / 2);
        String text = String.valueOf(_value) + " " + _unit;
        canvas.drawText(text, x / 2 - paint.measureText(text) / 2, y * 3 / 4 , paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = getSize(heightMeasureSpec);
        int width = height / 6;
        setMeasuredDimension(width, height);
        //super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
