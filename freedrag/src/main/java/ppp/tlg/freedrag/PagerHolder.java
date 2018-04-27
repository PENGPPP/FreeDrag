package ppp.tlg.freedrag;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerHolder<T> {

    View rootView;

    abstract View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, int position);

    abstract void fillView(@NonNull T t);

    protected void userVisible(int position) {
    }

    protected void userInvisible() {
    }

}
