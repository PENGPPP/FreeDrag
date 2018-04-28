package ppp.tlg.freedrag;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerHolder<T> {

    protected View rootView;
    protected int position;

    public abstract View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container);

    public abstract void fillView(@NonNull T t);

    protected void userVisible() {
    }

    protected void userInvisible() {
    }

    protected void onDestroyView() {
    }

    protected void update(@NonNull T t) {
    }

}
