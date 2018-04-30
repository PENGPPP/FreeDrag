package ppp.tlg.freedrag;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FreeDragLayout extends RelativeLayout {

    private static final long ANIMATION_DURATION = 200L;
    private static final float TOUCH_BEGIN_OFFSET = 200;
    private static final int DEFAULT_BG_COLOR = Color.BLACK;
    private static final float MIN_SCALE = 0.3F;

    private RectF contentRect = new RectF();
    private Rect contentRectProxy = new Rect();
    private DragListener dragListener;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    private float initX = 0f;
    private float initY = 0f;
    private float lastScaleFactor = 1f;
    private float lastDragDownScaleFactor = 1f;
    private int lastBgAlpha = 255;

    private boolean touchFocusBackToParentEnable = false;
    private boolean startMoveFlag = false;
    private boolean scaleEnable = true;

    public FreeDragLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public FreeDragLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FreeDragLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return getChild().performClick();
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

                if (isDragDown()) {
                    onDragDown(e1, e2, distanceX, distanceY);
                } else {
                    onFreeDrag(e1, e2, distanceX, distanceY);
                }

                return true;
            }

            private void onFreeDrag(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!checkBegin(e1, e2) && !startMoveFlag) {
                    return;
                }
                startMoveFlag = true;

                View view = getChild();
                view.setY(view.getY() - distanceY);
                view.setX(view.getX() - distanceX);
                convertRect(distanceX, distanceY, contentRect);
            }

            private void onDragDown(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if (!checkBegin(e1, e2) && !startMoveFlag) {
                    return;
                }

                startMoveFlag = true;

                float dis = e1.getY() - e2.getY() + TOUCH_BEGIN_OFFSET;

                float scale = 1 - Math.abs(dis) / 1000f;

                View view = getChild();
                view.setY(view.getY() - distanceY);
                view.setX(view.getX() - distanceX);

                if (scale >= 0f && scale <= 1f && dis <= 0f) {

                    if (scale >= MIN_SCALE) {
                        view.setScaleX(scale);
                        view.setScaleY(scale);
                    }

                    if (getBackground() != null) {
                        lastBgAlpha = (int) (scale * 255);
                        getBackground().setAlpha(lastBgAlpha);
                    }
                }

                if (dis > 0) {
                    lastDragDownScaleFactor = 1f;
                } else {
                    lastDragDownScaleFactor = scale;
                }
            }

            private boolean checkBegin(MotionEvent e1, MotionEvent e2) {

                float spanY = e1.getY() - e2.getY();
                float spanX = e1.getX() - e2.getX();

                return Math.hypot(spanX, spanY) > TOUCH_BEGIN_OFFSET;
            }


        });

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {

            @Override
            public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
                return true;
            }

            @Override
            public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

                lastScaleFactor *= scaleGestureDetector.getScaleFactor();

                convertRect(scaleGestureDetector.getScaleFactor(), contentRect);

                View child = getChild();
                child.setScaleX(lastScaleFactor);
                child.setScaleY(lastScaleFactor);

                ViewCompat.postInvalidateOnAnimation(child);
                return true;
            }

        });


        if (getBackground() == null) {
            setBackgroundColor(DEFAULT_BG_COLOR);
        }
        getBackground().setAlpha(255);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (getChildCount() != 1) {
            return super.onTouchEvent(ev);
        }

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:

                if (scaleEnable && !startMoveFlag && ev.getPointerCount() > 1) {
                    scaleGestureDetector.onTouchEvent(ev);
                } else {
                    gestureDetector.onTouchEvent(ev);
                }

                break;
            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
                checkStatus();
            default:
                gestureDetector.onTouchEvent(ev);
                scaleGestureDetector.onTouchEvent(ev);
                break;
        }
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    private void checkStatus() {
        startMoveFlag = false;

        if (isDragDown()) {

            if (dragListener == null || !dragListener.dragDownResult(lastDragDownScaleFactor)) {
                resetBounds();
                animationToInitStatus();
            }
            lastScaleFactor = 1f;
            lastDragDownScaleFactor = 1f;

        } else {
            borderTest();
        }

    }

    private boolean isDragDown() {
        return lastScaleFactor <= 1f;
    }

    private void animationToInitStatus() {
        getChild().animate()
                .y(initY)
                .x(initX)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(ANIMATION_DURATION)
                .start();

        int startAlpha = lastBgAlpha;
        lastBgAlpha = 255;

        ValueAnimator va = ValueAnimator.ofInt(startAlpha, 255);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                getBackground().setAlpha(value);
            }
        });
        va.setDuration(ANIMATION_DURATION);
        va.start();
    }

    private void convertRect(float factor, RectF rectF) {
        float spanWidth = rectF.width() * (factor - 1f);
        float spanHeight = rectF.height() * (factor - 1f);

        rectF.set(rectF.left - spanWidth / 2f,
                rectF.top - spanHeight / 2f,
                rectF.right + spanWidth / 2f,
                rectF.bottom + spanHeight / 2f);

    }

    private void convertRect(float x, float y, RectF rectF) {
        float left = rectF.left - x;
        float top = rectF.top - y;
        rectF.set(left, top, left + rectF.width(), top + rectF.height());
    }

    private void borderTest() {


        View child = getChild();

        float vx = 0;

        if (contentRect.left < contentRectProxy.left && contentRect.right < contentRectProxy.right) {

            if (contentRect.width() < contentRectProxy.width()) {
                vx = contentRect.left;
            } else {
                vx = contentRect.right - contentRectProxy.right;
            }


        } else if (contentRect.left > contentRectProxy.left && contentRect.right > contentRectProxy.right) {

            if (contentRect.width() < contentRectProxy.width()) {
                vx = contentRect.right - contentRectProxy.right;
            } else {
                vx = contentRect.left;
            }
        }

        float vy = 0;

        if (contentRect.top < contentRectProxy.top && contentRect.bottom < contentRectProxy.bottom) {


            if (contentRect.height() < contentRectProxy.height()) {
                vy = contentRect.top;
            } else {
                vy = contentRect.bottom - contentRectProxy.bottom;
            }

        } else if (contentRect.top > contentRectProxy.top && contentRect.bottom > contentRectProxy.bottom) {

            if (contentRect.height() < contentRectProxy.height()) {
                vy = contentRect.bottom - contentRectProxy.bottom;
            } else {
                vy = contentRect.top;
            }

        }

        touchFocusBackToParentEnable = vx != 0;

        convertRect(vx, vy, contentRect);

        child.animate()
                .y(child.getY() - vy)
                .x(child.getX() - vx)
                .setDuration(ANIMATION_DURATION)
                .start();

    }

    private View getChild() {

        if (getChildCount() > 1) {
            throw new RuntimeException(this.getClass().getName() + " can have only one child!");
        } else if (getChildCount() == 0) {
            return new View(getContext());
        } else {
            return getChildAt(0);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initBounds();
    }

    private void initBounds() {
        initY = getChild().getY();
        initX = getChild().getX();

        View child = getChild();

        contentRectProxy.set((int) initX, (int) initY, (int) (initX + child.getMeasuredWidth()), (int) (initY + child.getMeasuredHeight()));

        if (child instanceof ImageView && ((ImageView) child).getDrawable() != null) {
            ImageView imageView = (ImageView) child;
            Rect bounds = imageView.getDrawable().getBounds();
            contentRect.set(bounds);
            imageView.getImageMatrix().mapRect(contentRect);
        } else {
            contentRect.set(contentRectProxy);
        }

    }

    private void resetBounds() {

        View child = getChild();
        if (child instanceof ImageView && ((ImageView) child).getDrawable() != null) {
            ImageView imageView = (ImageView) child;
            Rect bounds = imageView.getDrawable().getBounds();
            contentRect.set(bounds);
            imageView.getImageMatrix().mapRect(contentRect);
        } else {
            contentRect.set(contentRectProxy);
        }
    }

    public void resetLayoutStatus() {
        lastScaleFactor = 1f;
        lastDragDownScaleFactor = 1f;
        startMoveFlag = false;
        resetBounds();

        View child = getChild();
        child.setX(initX);
        child.setY(initY);
        child.setScaleX(1f);
        child.setScaleY(1f);

        lastBgAlpha = 255;
        getBackground().setAlpha(lastBgAlpha);

        ViewCompat.postInvalidateOnAnimation(this);
    }

    public boolean touchFocusBackToParent() {
        return lastScaleFactor <= 1f || touchFocusBackToParentEnable;
    }

    public void setDragListener(DragListener dragListener) {
        this.dragListener = dragListener;
    }

    public interface DragListener {
        boolean dragDownResult(float progress);
    }

    public void setScaleEnable(boolean scaleEnable) {
        this.scaleEnable = scaleEnable;
    }
}
