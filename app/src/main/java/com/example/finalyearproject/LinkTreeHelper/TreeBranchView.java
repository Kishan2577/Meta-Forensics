package com.example.finalyearproject.LinkTreeHelper;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalyearproject.R;

public class TreeBranchView extends View {

    private Paint paint;

    public TreeBranchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFF4CAF50); // Green branches
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        ViewGroup parent = (ViewGroup) getParent();
        View center = parent.findViewById(R.id.centerButton);

        if (center == null) return;

        float cx = center.getX() + center.getWidth() / 2f;
        float cy = center.getY() + center.getHeight() / 2f;

        int[] buttonIds = {
                R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4,
                R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8
        };

        for (int id : buttonIds) {
            View child = parent.findViewById(id);
            if (child != null) {
                float bx = child.getX() + child.getWidth() / 2f;
                float by = child.getY() + child.getHeight() / 2f;

                // Use quadratic Bezier for curved branch
                Path path = new Path();
                path.moveTo(cx, cy);
                path.quadTo((cx + bx) / 2, cy - 200, bx, by);
                canvas.drawPath(path, paint);
            }
        }
    }
}
