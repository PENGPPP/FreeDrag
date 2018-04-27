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

    private FreeDragPagerAdapter adapter;

    public FreeDragViewPager(@NonNull Context context) {
        super(context);
    }

    public FreeDragViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
        if (!(adapter instanceof FreeDragPagerAdapter)) {
            throw new RuntimeException("this pager adapter must be " + FreeDragPagerAdapter.class.getName());
        }

        this.adapter = (FreeDragPagerAdapter) adapter;
        super.setAdapter(adapter);
    }
}
