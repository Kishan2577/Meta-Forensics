package com.example.finalyearproject.LinkTreeHelper;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class BranchLinesView extends View {

    private Paint paint;
    private float centerX, centerY;
    private final List<float[]> branches = new ArrayList<>();
    private float animationProgress = 0f;
    private ValueAnimator animator;

    public BranchLinesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(0xFFFBD77E); // Yellow color matching the theme
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    public void setCenter(float x, float y) {
        this.centerX = x;
        this.centerY = y;
        invalidate();
    }

    public void setBranches(List<float[]> positions) {
        branches.clear();
        branches.addAll(positions);
        animateLines();
    }

    private void animateLines() {
        if (animator != null) {
            animator.cancel();
        }
        
        animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(800);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(animation -> {
            animationProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        for (int i = 0; i < branches.size(); i++) {
            float[] pos = branches.get(i);
            if (pos != null && pos.length >= 2) {
                drawCurvedLine(canvas, centerX, centerY, pos[0], pos[1], i, animationProgress);
            }
        }
    }

    private void drawCurvedLine(Canvas canvas, float startX, float startY, float endX, float endY, int index, float progress) {
        Path path = new Path();
        
        // Calculate control points for curved lines
        float midX = (startX + endX) / 2f;
        float midY = (startY + endY) / 2f;
        
        // Add some curve based on position (left vs right side)
        float curveOffset = 120f;
        if (endX < startX) { // Left side
            curveOffset = -curveOffset;
        }
        
        // Animate the line drawing
        float animatedEndX = startX + (endX - startX) * progress;
        float animatedEndY = startY + (endY - startY) * progress;
        
        // Create a smooth curved path
        path.moveTo(startX, startY);
        
        // Use cubic Bezier for smoother curves
        float control1X = startX + (midX - startX) * 0.5f + curveOffset * 0.3f;
        float control1Y = startY + (midY - startY) * 0.5f;
        float control2X = midX + (animatedEndX - midX) * 0.5f + curveOffset * 0.3f;
        float control2Y = midY + (animatedEndY - midY) * 0.5f;
        
        path.cubicTo(control1X, control1Y, control2X, control2Y, animatedEndX, animatedEndY);
        
        // Create gradient effect for the line with yellow theme
        LinearGradient gradient = new LinearGradient(
            startX, startY, animatedEndX, animatedEndY,
            new int[]{0xFFFBD77E, 0xFFFFE4A3, 0xFFFBD77E},
            new float[]{0.0f, 0.5f, 1.0f},
            Shader.TileMode.CLAMP
        );
        paint.setShader(gradient);
        
        // Draw the path
        canvas.drawPath(path, paint);
        
        // Reset shader
        paint.setShader(null);
    }

    public void clearBranches() {
        branches.clear();
        if (animator != null) {
            animator.cancel();
        }
        animationProgress = 0f;
        invalidate();
    }
}
