package ppp.tlg.freedrag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class FreeDragViewPager extends ViewPager {

    private ViewPagerAdapter adapter;
    private FreeDragLayout.DragListener dragListener;

    public FreeDragViewPager(@NonNull Context context) {
        super(context);
    }

    public FreeDragViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Object ob = adapter.getCurrentRootView();

        if (ob != null && ob instanceof FreeDragLayout) {
            ((FreeDragLayout) ob).setDragListener(dragListener);
            return ((FreeDragLayout) ob).touchFocusBackToParent() && super.onInterceptTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        if (!(adapter instanceof ViewPagerAdapter)) {
            throw new RuntimeException("this pager adapter must be " + ViewPagerAdapter.class.getName());
        }

        this.adapter = (ViewPagerAdapter) adapter;
        super.setAdapter(adapter);

    }

    public void setDragListener(FreeDragLayout.DragListener dragListener) {
        this.dragListener = dragListener;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (adapter != null) {
            adapter.onDetachedFromWindow();
        }
    }
}
