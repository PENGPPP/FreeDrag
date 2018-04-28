package ppp.tlg.freedragwatcher;

import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ppp.tlg.freedrag.FreeDragPagerAdapter;
import ppp.tlg.freedrag.FreeDragViewPager;
import ppp.tlg.freedrag.PagerHolder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DemoPagerAdapter adapter = new DemoPagerAdapter();

        FreeDragViewPager viewPager = findViewById(R.id.dragViewPager);
        viewPager.setAdapter(new DemoPagerAdapter());

        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
            }
        });

    }

    private static final class DemoPagerAdapter extends FreeDragPagerAdapter {

        @Override
        public PagerHolder createHolder(int position, int type) {
            return new DemoPagerHolder();
        }

        @Override
        public void bindHolder(PagerHolder holder, int position) {

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
