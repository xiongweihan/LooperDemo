package com.hj.looperdemo;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.hj.looperdemo.view.LooperPager;
import com.hj.looperdemo.view.PagerBean;
import com.hj.looperdemo.view.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * viewpager打造无限轮询的轮播图
 */
public class MainActivity extends AppCompatActivity {

    private LooperPager looperView;

    private List<PagerBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
        initListener();

    }

    private void initListener() {

        if (looperView != null) {
            looperView.setOnItemClickListener(new LooperPager.onItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    Toast.makeText(MainActivity.this,"点击---》" +(position+1),Toast.LENGTH_SHORT).show();
                    // TODO: 2020/11/4 这里根据实际业务写逻辑
                }
            });
        }
    }

    private void initData() {

        mData.add(new PagerBean("第一个,左边", R.drawable.bg_looper_one));
        mData.add(new PagerBean("第二个，中间", R.drawable.bg_looper_two));
//        mData.add(new PagerBean("第三个，右边", R.drawable.bg_looper_three));
    }

    private void initView() {
        looperView = findViewById(R.id.looperpager);
        looperView.setData(innerAdapter,
                new LooperPager.BindTitleListener() {
                    @Override
                    public String getTitle(int position) {
                        return mData.get(position).getTitle();
                    }
                });

    }

    private LooperPager.InnerAdapter innerAdapter = new LooperPager.InnerAdapter() {

        @Override
        protected int getDataSize() {
            return mData.size();
        }

        @Override
        protected View getSubView(ViewGroup container, int position) {
            ImageView iv = new ImageView(container.getContext());
            iv.setImageResource(mData.get(position).getPicResId());
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            return iv;
        }
    };

}
