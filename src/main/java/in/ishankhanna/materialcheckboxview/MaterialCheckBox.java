/**
 Copyright 2015 Ishan Khanna, Salam Thomas

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package in.ishankhanna.materialcheckboxview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Ishan Khanna
 */
public class MaterialCheckBox extends View {

    /**
     * Tells about the current state of the checkbox, if true show tick
     * else show a cross.
     */
    private boolean isChecked = false;

    private Paint unCheckedPaint;
    private Paint checkedPaint;
    private Paint linePaint;

    private float cX;
    private float cY;

    private int radius;

    private float lengthOfLine;
    private float sideOfRightAngledTriangleFormedByLines;

    private float TotalTimeForAnimation = 100;
    private float currentAnimationTime = 0;
    private float delta = currentAnimationTime / TotalTimeForAnimation;

    private float shadowRadius = 0;
    private float maxShadowRadius = 20;

    private float lf;

    private OnCheckedChangeListener onCheckedChangeListener;

    private int checkedStateColor;
    private int unCheckedStateColor;
    private int lineColor;
    private int shadowColor;

    public MaterialCheckBox(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.MaterialCheckBox);
        radius = attributes.getDimensionPixelSize(R.styleable.MaterialCheckBox_mcb_radius, getDimensionInPixel(18));
        checkedStateColor = attributes.getColor(R.styleable.MaterialCheckBox_mcb_checkedStateColor, Color.parseColor("#FF373D"));
        unCheckedStateColor = attributes.getColor(R.styleable.MaterialCheckBox_mcb_unCheckedStateColor, Color.parseColor("#00E676"));
        lineColor = attributes.getColor(R.styleable.MaterialCheckBox_mcb_lineColor, Color.parseColor("#FFFFFF"));
        shadowColor = attributes.getColor(R.styleable.MaterialCheckBox_mcb_shadowColor, Color.parseColor("#80000000"));
        maxShadowRadius = attributes.getDimensionPixelSize(R.styleable.MaterialCheckBox_mcb_shadowRadius, 0);

        lengthOfLine = radius / 2;
        sideOfRightAngledTriangleFormedByLines = lengthOfLine / (2 * (float)Math.sqrt(2));

        attributes.recycle();

        setupColorPallets();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int margin  = getDimensionInPixel(6);
        setMeasuredDimension((radius *2) + (margin * 4) , (radius *2) + (margin * 4));

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        setcX(w/2);
        setcY(h/2);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    }

    //get all dimensions in dp so that views behaves properly on different screen resolutions
    private int getDimensionInPixel(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(cX, cY, radius, unCheckedPaint);
        float interval1, interval2;

        interval1 = 0.0f;
        interval2 = 1.0f;

        if (delta <= interval1) {
            checkedPaint.setAlpha(0);
        } else if (delta <= interval2) {
            int alpha = (int) (((delta - interval1) / (interval2 - interval1)) * 255);
            checkedPaint.setAlpha(alpha);
        } else {
            checkedPaint.setAlpha(255);
        }

        canvas.drawCircle(cX, cY, delta * radius, checkedPaint);

        float a1,b1,a2,b2,a3,b3,a4,b4;

        lf = 0.5f * sideOfRightAngledTriangleFormedByLines * delta;

        // forward slash drawing
        a2 = cX + sideOfRightAngledTriangleFormedByLines; b2 = cY - sideOfRightAngledTriangleFormedByLines;
        a4 = cX - sideOfRightAngledTriangleFormedByLines; b4 = cY + sideOfRightAngledTriangleFormedByLines;
        a2 += delta * sideOfRightAngledTriangleFormedByLines;
        a4 += delta * sideOfRightAngledTriangleFormedByLines;
        a2 -= lf;
        a4 -= lf;
        canvas.drawLine(a2,b2,a4,b4, linePaint);

        // back slash drawing
        a1 = cX - sideOfRightAngledTriangleFormedByLines; b1 = cY - sideOfRightAngledTriangleFormedByLines;
        b3 = cY + sideOfRightAngledTriangleFormedByLines;
        b1 += delta * sideOfRightAngledTriangleFormedByLines;

        float l = lengthOfLine - ((lengthOfLine /2) * delta);
        a3 = a1 + l/(float)Math.sqrt(2);

        a1 -= lf;
        a3 -= lf;

        canvas.drawLine(a1,b1,a3,b3, linePaint);

    }

    private Runnable fromUncheckedToChecked = new Runnable() {
        @Override
        public void run() {
            if(currentAnimationTime < TotalTimeForAnimation) {
                currentAnimationTime += 16;
                delta = currentAnimationTime / TotalTimeForAnimation;
                if (delta < 0) {
                    delta = 0;
                } else if (delta > 1) {
                    delta = 1;
                }
                invalidate();
                postDelayed(this, 16);
            } else {
                isChecked = !isChecked;

                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(isChecked);
                }

                while (shadowRadius > 0) {
                    shadowRadius--;
                    unCheckedPaint.setShadowLayer(shadowRadius, 0, 20, Color.parseColor("#80000000"));
                    invalidate();
                }
            }

        }
    };

    private Runnable fromCheckedToUnChecked = new Runnable() {
        @Override
        public void run() {
            if(currentAnimationTime > 0) {
                currentAnimationTime -= 16;
                delta = currentAnimationTime / TotalTimeForAnimation;
                if (delta < 0) {
                    delta = 0;
                } else if (delta > 1) {
                    delta = 1;
                }
                invalidate();
                postDelayed(this, 16);
            } else {
                isChecked = !isChecked;

                if (onCheckedChangeListener != null) {
                    onCheckedChangeListener.onCheckedChanged(isChecked);
                }

                while (shadowRadius > 0) {
                    shadowRadius--;
                    unCheckedPaint.setShadowLayer(shadowRadius, 0, 20, shadowColor);
                    invalidate();
                }
            }

        }
    };


    /**
     * Initialise the paints.
     */
    private void setupColorPallets() {

        unCheckedPaint = new Paint();
        unCheckedPaint.setStyle(Paint.Style.FILL);
        unCheckedPaint.setColor(unCheckedStateColor);
        unCheckedPaint.setAntiAlias(true);

        checkedPaint = new Paint();
        checkedPaint.setColor(checkedStateColor);
        checkedPaint.setStyle(Paint.Style.FILL);
        checkedPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setColor(lineColor);
        linePaint.setStrokeWidth(12);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        linePaint.setAntiAlias(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

    }

    /**
     * Set center coordinate of circle on x-axis
     * @param cX
     */
    public void setcX(float cX) {
        this.cX = cX;
    }

    /**
     * Set center coordinate of circle on y-axis
     * @param cY
     */
    public void setcY(float cY) {
        this.cY = cY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                while (shadowRadius < maxShadowRadius) {
                    shadowRadius++;
                    unCheckedPaint.setShadowLayer(shadowRadius, 0, 20, shadowColor);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isChecked) {
                    post(fromUncheckedToChecked);
                } else {
                    post(fromCheckedToUnChecked);
                }
                break;
        }
        return true;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return onCheckedChangeListener;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener{
        void onCheckedChanged(boolean isChecked);
    }

}
