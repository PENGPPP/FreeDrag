package ppp.tlg.freedrag;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class FreeDragPagerAdapter extends PagerAdapter {

    private List<View> viewPool = new ArrayList<>();
    private List<View> removeViewPool = new ArrayList<>();
    private PagerHolder primaryItemHolder;

    private static final int NO_TYPE = -1;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        PagerHolder holder = createHolder(position, getHolderType(position));
        View view = holder.onCreateView(LayoutInflater.from(container.getContext()), container, position);
        holder.rootView = view;
        viewPool.add(view);
        bindHolder(holder, position);
        return holder;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        removeViewPool.add(((PagerHolder) object).rootView);
    }


    @Override
    public void startUpdate(@NonNull ViewGroup container) {
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
        for (View view : viewPool) {
            container.addView(view);
        }
        viewPool.clear();

        for (View view : removeViewPool) {
            container.removeView(view);
        }
        removeViewPool.clear();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        PagerHolder holder = (PagerHolder) object;
        if (holder != primaryItemHolder) {
            if (primaryItemHolder != null && position > 0) {
                primaryItemHolder.userInvisible(position - 1);
            }

            holder.userVisible(position);
            primaryItemHolder = holder;
        }
    }

    public View getCurrentRootView() {
        return primaryItemHolder.rootView;
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((PagerHolder) object).rootView == view;
    }

    public abstract PagerHolder createHolder(int position, int type);

    public abstract void bindHolder(PagerHolder holder, int position);

    protected int getHolderType(int position) {
        return NO_TYPE;
    }

}
