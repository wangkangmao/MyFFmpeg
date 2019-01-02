package module.video.jnc.myffmpeg.activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

import module.video.jnc.myffmpeg.R;
import module.video.jnc.myffmpeg.adapter.HomeViewPagerAdapter;
import module.video.jnc.myffmpeg.fragment.EditFragment;
import module.video.jnc.myffmpeg.fragment.OtherFragment;
import module.video.jnc.myffmpeg.fragment.MyFragment;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private HomeViewPagerAdapter adapter;
    private EditFragment editFragment;
    private MyFragment playFragment;
    private OtherFragment otherFragment;
    private List<Fragment> listFragment = new ArrayList<>();
    private Button btVideoEdit;
    private Button btOther;
    private Button btPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        findViewById();
        init();

    }


    private void rotateRectClockwiseDegree90(byte[] src, int srcOffset, int width, int height, byte dst[], int dstOffset) {
        int i, j;
        int index = dstOffset;
        for (i = 0; i < width; i++) {
            for (j = height - 1; j >= 0; j--) {
                dst[index] = src[srcOffset + j * width + i];
                index++;
            }
        }
    }


    private void rotateRectAnticlockwiseDegree90(byte[] src, int srcOffset, int width, int height, byte dst[],
                                                 int dstOffset) {
        int i, j;
        int index = dstOffset;
        for (i = width - 1; i >= 0; i--) {
            for (j = 0; j < height; j++) {
                dst[index] = src[srcOffset + j * width + i];
                index++;
            }
        }
    }

    private void findViewById() {
        viewPager = findViewById(R.id.view_pager);
        btVideoEdit = findViewById(R.id.bt_edit);
        btOther = findViewById(R.id.bt_other);
        btPlayer = findViewById(R.id.bt_play);

    }

    private void init() {
        editFragment = new EditFragment();
        playFragment = new MyFragment();
        otherFragment = new OtherFragment();
        listFragment.add(editFragment);
        listFragment.add(otherFragment);
        listFragment.add(playFragment);
        adapter = new HomeViewPagerAdapter(getSupportFragmentManager(), listFragment);
        viewPager.setAdapter(adapter);

        btVideoEdit.setOnClickListener(this);
        btOther.setOnClickListener(this);
        btPlayer.setOnClickListener(this);

        thread = new Mythread();
        thread.start();
    }

    Mythread thread;

    class Mythread extends Thread{
        @Override
        public void run() {
            super.run();

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                viewPager.setCurrentItem(0);
                break;
            case R.id.bt_other:
                viewPager.setCurrentItem(1);
                break;
            case R.id.bt_play:
                viewPager.setCurrentItem(2);
                break;
        }
    }

}
