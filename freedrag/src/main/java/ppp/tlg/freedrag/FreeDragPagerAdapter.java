package ppp.tlg.freedrag;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class FreeDragPagerAdapter extends PagerAdapter {

    private List<View> viewPool = new ArrayList<>();
    private List<View> removeViewPool = new ArrayList<>();
    private List<PagerHolder> holderPool = new ArrayList<>();
    private PagerHolder primaryItemHolder;

    private static final int NO_TYPE = -1;

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        log("instantiateItem");

        PagerHolder holder = createHolder(position, getHolderType(position));
        holder.position = position;
        holderPool.add(holder);
        View view = holder.onCreateView(LayoutInflater.from(container.getContext()), container);
        holder.rootView = view;
        viewPool.add(view);
        bindHolder(holder, position);
        return holder;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

//        log("destroyItem");

        PagerHolder holder = ((PagerHolder) object);
        holderPool.remove(holder);
        holder.onDestroyView();
        removeViewPool.add(holder.rootView);
    }


    @Override
    public void startUpdate(@NonNull ViewGroup container) {
//        log("startUpdate");
    }

    @Override
    public void finishUpdate(@NonNull ViewGroup container) {
//        log("finishUpdate");

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
        log("setPrimaryItem:" + position);

        PagerHolder holder = (PagerHolder) object;
        if (holder != primaryItemHolder) {
            if (primaryItemHolder != null && position >= 0) {
                primaryItemHolder.userInvisible();
            }

            holder.userVisible();
            primaryItemHolder = holder;
        }
    }

    public View getCurrentRootView() {
        return primaryItemHolder.rootView;
    }

    private void log(String log) {
        Log.i("FREE_DRAG_ADAPTER", log);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ((PagerHolder) object).rootView == view;
    }

    public abstract PagerHolder createHolder(int position, int type);

    public void update(PagerHolder holder, int position) {
    }

    public abstract void bindHolder(PagerHolder holder, int position);

    protected int getHolderType(int position) {
        return NO_TYPE;
    }

    public void notifyDataSetChanged(int position) {
        for (PagerHolder holder : holderPool) {
            if (holder.position == position) {
                update(holder, position);
            }
        }
    }
}
