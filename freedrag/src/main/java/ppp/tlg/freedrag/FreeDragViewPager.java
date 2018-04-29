package ppp.tlg.freedrag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class FreeDragViewPager extends ViewPager {

    private ViewPagerAdapter adapter;
    private FreeDragLayout.DragListener dragListener;

    public FreeDragViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    public FreeDragViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if (dragListener == null) {
                    return;
                }

                Object ob = adapter.getCurrentRootView();
                if (ob != null && ob instanceof FreeDragLayout) {
                    ((FreeDragLayout) ob).setDragListener(dragListener);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.i("SUPER_VIEW_PAGER", "GETITEM:" + getCurrentItem());
        Object ob = adapter.getCurrentRootView();

        if (ob != null && ob instanceof FreeDragLayout) {
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
}
