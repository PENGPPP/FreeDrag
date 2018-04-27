package ppp.tlg.freedrag;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class SuperViewPager extends ViewPager {

    private SuperPagerAdapter adapter;

    public SuperViewPager(@NonNull Context context) {
        super(context);
    }

    public SuperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.i("SUPER_VIEW_PAGER", "GETITEM:" + getCurrentItem());
        Object ob = adapter.getCurrentRootView();

        if (ob != null && ob instanceof TouchLayout) {
            return ((TouchLayout) ob).touchFocusBackToParent() && super.onInterceptTouchEvent(ev);
        } else {
            return super.onInterceptTouchEvent(ev);
        }

    }

    @Override
    public void setAdapter(@Nullable PagerAdapter adapter) {
        if (!(adapter instanceof SuperPagerAdapter)) {
            throw new RuntimeException("this pager adapter must be " + SuperPagerAdapter.class.getName());
        }

        this.adapter = (SuperPagerAdapter) adapter;
        super.setAdapter(adapter);
    }
}
