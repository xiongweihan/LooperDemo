package com.hj.looperdemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.hj.looperdemo.R;


public class LooperPager extends LinearLayout {

    private static final String TAG = "LooperPager";
    private LoopViewPager mViewPager;
    private TextView mTvTitle;
    private LinearLayout mLlDotsLayout;
    private InnerAdapter innerAdapter = null;

    public BindTitleListener titleBindListener = null;
    private onItemClickListener itemClickListener = null;
    private boolean isTitleShow;
    private int pagerCount;
    private int mSwitchTime;

    public LooperPager(Context context) {
        this(context, null);
    }

    public LooperPager(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LooperPager(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //第三个参数，attachToRoot --
        LayoutInflater.from(context).inflate(R.layout.item_looper_pager_layout, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.looper_style);
        //读取属性值
        isTitleShow = typedArray.getBoolean(R.styleable.looper_style_is_title_show, true);
        pagerCount = typedArray.getInteger(R.styleable.looper_style_show_pager_count, 1);
        mSwitchTime = typedArray.getInteger(R.styleable.looper_style_show_time, -1);
        Log.e(TAG,"isTitleShow-->" + isTitleShow);
        Log.e(TAG,"pagerCount-->" + pagerCount);
        Log.e(TAG,"mSwitchTime-->" + mSwitchTime);
        typedArray.recycle();//释放掉资源
        init();
    }

    private void init() {
        initView();
        initEvent();


    }

    /**
     * 滑动监听
     */
    private void initEvent() {
        mViewPager.setPagerItemClickListener(new LoopViewPager.OnPagerItemClickListener() {
            @Override
            public void PagerItemClickListener(int position) {
                if(itemClickListener != null && innerAdapter != null){
                    itemClickListener.onItemClick(position % innerAdapter.getDataSize());
                }

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //切换到回调
            }

            @Override
            public void onPageSelected(int position) {
                //切换停下的回调
                if (innerAdapter != null) {
                    int realPosition = position % innerAdapter.getDataSize();
                    //停下来，设置title
                    if (titleBindListener != null) {
                        mTvTitle.setText(titleBindListener.getTitle(realPosition));
                    }
                    //切换指示器焦点
                    upDataIndcator();
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initView() {
        mViewPager = this.findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setPageMargin(ScreenUtil.dip2px(getContext(), 10));
        mTvTitle = this.findViewById(R.id.tv_title);
        mLlDotsLayout = this.findViewById(R.id.ll_dots_layout);
        mTvTitle.setVisibility(isTitleShow ? VISIBLE: GONE);
        mViewPager.setDelayTime(mSwitchTime != -1? mSwitchTime: LoopViewPager.DEFAULT_DELAY_TIME);

    }

    //设置数据
    public void setData(InnerAdapter innerAdapter, BindTitleListener listener) {
        this.innerAdapter = innerAdapter;
        this.titleBindListener = listener;
        mViewPager.setAdapter(innerAdapter);
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2 + 1);
        if (listener != null) {
            mTvTitle.setText(listener.getTitle(mViewPager.getCurrentItem() % innerAdapter.getDataSize()));
        }
        //可以得到数据的个数，根据数据个数来动态创建小圆点指示器
        upDataIndcator();
    }

    /**
     * 创建小圆点指示器
     */
    private void upDataIndcator() {
        if (innerAdapter != null) {
            int count = innerAdapter.getDataSize();
            mLlDotsLayout.removeAllViews();//每次添加前先清空
            for (int i = 0; i < count; i++) {
                //创建view点
                View pointView = new View(getContext());
                if ((mViewPager.getCurrentItem() % innerAdapter.getDataSize()) == i) {
//                    pointView.setBackgroundColor(Color.parseColor("#000000"));
                    pointView.setBackgroundResource(R.drawable.shape_select_circle_red);//todo 这里是自定义小圆点了，下面未选中一样
                } else {
//                    pointView.setBackgroundColor(Color.parseColor("#ffffff"));
                    pointView.setBackgroundResource(R.drawable.shape_unselect_circle_white);
                }
                //设置大小
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ScreenUtil.dip2px(getContext(), 5), ScreenUtil.dip2px(getContext(), 5));
                layoutParams.setMargins(ScreenUtil.dip2px(getContext(), 5), 0, ScreenUtil.dip2px(getContext(), 5), 0);
                pointView.setLayoutParams(layoutParams);
                //添加到容器里
                mLlDotsLayout.addView(pointView);

            }
        }

    }


    //定义接口获取标题
    public interface BindTitleListener {
        String getTitle(int position);
    }

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.itemClickListener = listener;

    }

    public abstract static class InnerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            final int realPosition = position % getDataSize();
            View itemView = getSubView(container, realPosition);
            container.addView(itemView);
            return itemView;

        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        protected abstract View getSubView(ViewGroup container, int position);

        protected abstract int getDataSize();

    }


}
