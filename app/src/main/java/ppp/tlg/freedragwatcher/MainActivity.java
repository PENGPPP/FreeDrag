package ppp.tlg.freedragwatcher;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ppp.tlg.freedrag.FreeDragLayout;
import ppp.tlg.freedrag.ViewPagerAdapter;
import ppp.tlg.freedrag.FreeDragViewPager;
import ppp.tlg.freedrag.PagerHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DemoPagerAdapter adapter = new DemoPagerAdapter();

        FreeDragViewPager viewPager = findViewById(R.id.dragViewPager);

        viewPager.setDragListener(new FreeDragLayout.DragListener() {
            @Override
            public boolean dragDownResult(float progress) {

                Log.i("TEST_DRAG_RESULT", "P:" + progress);
                return false;
            }
        });
        viewPager.setAdapter(adapter);

    }

    private static final class DemoPagerAdapter extends ViewPagerAdapter {

        @Override
        public PagerHolder createHolder(int position, int type) {
            return new DemoPagerHolder();
        }

        @Override
        public void bindHolder(PagerHolder holder, int position) {
            holder.fillView(position);
        }

        @Override
        public int getCount() {
            return 10;
        }
    }

    private static final class DemoPagerHolder extends PagerHolder<Integer> {
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container) {
            return inflater.inflate(R.layout.demo_holder_layout, container, false);
        }

        @Override
        public void fillView(@NonNull Integer integer) {
            rootView.findViewById(R.id.demo_image_view)
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("DRAG_CLICK_TAG", "-----------:");
                        }
                    });
        }

        @Override
        protected void userInvisible() {
            Log.i("DRAG_POSITION_TAG", "INVISIBLE:" + position);
        }

        @Override
        protected void userVisible() {
            Log.i("DRAG_POSITION_TAG", "VISIBLE:" + position);

        }
    }

}
