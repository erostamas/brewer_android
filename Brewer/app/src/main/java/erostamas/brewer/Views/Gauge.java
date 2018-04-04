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
import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * Created by etamero on 2017.09.17..
 */

public class Gauge extends View {

    private static final String _unit = "°C";
    private String _name = new String();

    private double _value = 0;
    private double _min = 0.0;
    private double _max = 100.0;

    public Gauge(Context context) {
        super(context);
        init(null);
    }

    public Gauge(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Display, 0, 0);
        try {
            _name = ta.getString(R.styleable.Display_name);
        } finally {
            ta.recycle();
        }
        init(attrs);
    }

    public Gauge(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
    }

    public void set(double value) {
        _value = min(max(_min, value), _max);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArcs(canvas);
        drawValue(canvas);
        drawName(canvas);
    }

    private void drawArcs(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        RectF topRect = new RectF(30, 30, x - 30, y - 30);
        Paint paint =  new Paint();
        paint.setStrokeWidth(30);
        paint.setStyle(Paint.Style.STROKE);
        int [] colors = {parseColor("#2471A3"), parseColor("#F7DC6F"), parseColor("#FF0000")};
        float[] positions = {0.0f, 0.5f, 1f};
        canvas.save();
        canvas.rotate(90, x / 2, y / 2);
        paint.setColor(parseColor("#2C3E50"));
        canvas.drawArc (topRect, 30, 300, false, paint);
        paint.setShader(new SweepGradient(x / 2, y / 2, colors, positions));
        canvas.drawArc (topRect, 30, (float)(_value / (_max - _min) * 300), false, paint);
        canvas.restore();
    }

    void drawValue(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setTextSize(y / 4);
        String text = String.format("%.1f", _value);
        canvas.drawText(text, x / 2 - paint.measureText(text) / 2, y / 2, paint);
        paint.setTextSize(y / 7);
        canvas.drawText(_unit, x / 2 - paint.measureText(_unit) / 2, y * 5 / 7, paint);
    }

    void drawName(Canvas canvas) {
        int x = getWidth();
        int y = getHeight();
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        paint.setTextSize(y / 10);
        canvas.drawText(_name, x / 2 - paint.measureText(_name) / 2, y * 9 / 10, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
