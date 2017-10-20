package com.android.slowlife.view;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.R;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/22.
 */
public class InfiniteLoopViewNet extends LinearLayout {

    /**
     * 图片轮播控件
     */
    private ViewPager mViewPager;
    /**
     * 当前选中的图片
     */
    private TextView current_page;
    /**
     * 总图片大小
     */
    private TextView overall_page;
    /**
     * 图片轮播指示器空间
     */
    private LinearLayout mPointLL;
    /**
     * 图片描述控件
     */
    private TextView mDescribeTV;
    /**
     * 图片轮播控件适配器
     */
    private ImageLoopAdapter mImageLoopAdapter;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 单个图片控件列表
     */
    private ArrayList<ImageView> mIconList;
    /**
     * 图片资源数组
     */
    private int[] mIconArray;
    /**
     * 指示器前一个选中下标
     */
    private int mPreviousIndex = 0;
    /**
     * 数据的长度
     */
    int mDataLength;
    private boolean isLeftToRight = true;
    private boolean isRightToLeft = false;
    private int which = 0;
    private Handler handler = new Handler();

    /**
     * @param context
     */
    public InfiniteLoopViewNet(Context context) {
        super(context);
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public InfiniteLoopViewNet(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    /**
     * 初始化视图控件
     *
     * @param context
     */
    private void initView(Context context) {
        mContext = context;
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_infinite_loop, this);
        mViewPager = (ViewPager) view.findViewById(R.id.loop_vp);
        //mPointLL = (LinearLayout) view.findViewById(R.id.loop_ll);
        current_page = (TextView) view.findViewById(R.id.current_page);
        overall_page = (TextView) view.findViewById(R.id.overall_page);
        //初始化单个图片控件列表
        mIconList = new ArrayList<ImageView>();
        mDataLength = mIconList.size();
        //设置监听事件
        mViewPager.setOnPageChangeListener(new ImageLoopPagerListener());
        handler.postDelayed(runnable, 5000);
    }

    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            if (isLeftToRight) {
                if (which < mIconList.size() - 1) {
                    which++;
                    mViewPager.setCurrentItem(which);
                } else {
                    isLeftToRight = false;
                    isRightToLeft = true;
                    handler.post(runnable);
                    return;
                }
            } else {
                if (which > 0) {
                    which--;
                    mViewPager.setCurrentItem(which);
                } else {
                    isRightToLeft = false;
                    isLeftToRight = true;
                    handler.post(runnable);
                    return;
                }
            }
            handler.postDelayed(runnable, 3000);
        }
    };

    /**
     * 图片轮播控件监听事件
     */
    private class ImageLoopPagerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (position != 0) {
                //mPointLL.getChildAt(0).setEnabled(false);
                int page = position + 1;
                current_page.setText(page + "");
            }
        }

        @Override
        public void onPageSelected(int position) {
            //设置文字
//            mPointLL.getChildAt(mPreviousIndex).setEnabled(false);
//            mPointLL.getChildAt(position % mDataLength).setEnabled(true);
            mPreviousIndex = position % mDataLength;
            which = position;
            int page = position + 1;
            current_page.setText(page + "");
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
//
//    /**
//     * 装填数据
//     *
//     * @param mIconArray
//     * @param mInfiniteLoopClickListener
//     */
//    public void setImageLoopResourcesUri(List<Uri> mIconArray, InfiniteLoopClickListener mInfiniteLoopClickListener, InfiniteLoopLongClickListener mInfiniteLoopLongClickListener) {
//        //删除所有子视图
//        //mPointLL.removeAllViews();
//        ImageView imageView = null;
//        View view = null;
//        mIconList.clear();
//        for (int i = 0; i < mIconArray.size(); i++) {
//            imageView = new ImageView(mContext);
//            //设置图片
//            //Glide.with(mContext).load(mIconArray.get(i)).placeholder(R.drawable.loading2).error(R.drawable.loading2).into(imageView);
//            //添加到列表
//            mIconList.add(imageView);
//            //指示器
////            view = new View(mContext);
////            view.setBackgroundResource(R.drawable.point_background);
////            LayoutParams layoutParams = new LayoutParams(20, 20);
////            layoutParams.leftMargin = 10;
////            //设置参数
////            view.setLayoutParams(layoutParams);
////            view.setEnabled(false);
////            //添加到父控件
////            mPointLL.addView(view);
//        }
//        //数据的长度
//        mDataLength = mIconList.size();
//        overall_page.setText(mDataLength + "");
//        Log.e("打印出来的mDataLength2", mDataLength + "");
////        //默认选中
////        mPointLL.getChildAt(0).setEnabled(true);
//        current_page.setText("1");
//        //初始化适配器
////        mImageLoopAdapter = new ImageLoopAdapter(mInfiniteLoopClickListener,mInfiniteLoopLongClickListener,mIconList);
//        mImageLoopAdapter = new ImageLoopAdapter(mIconList);
//        //设置适配器
//        mViewPager.setAdapter(mImageLoopAdapter);
//    }
//
//    /**
//     * 装填数据
//     *
//     * @param mIconArray
//     * @param mInfiniteLoopClickListener
//     */
//    public void setImageLoopResourcesNet(List<ImageBean> mIconArray, InfiniteLoopClickListener mInfiniteLoopClickListener, InfiniteLoopLongClickListener mInfiniteLoopLongClickListener) {
//        //删除所有子视图
//        mPointLL.removeAllViews();
//        ImageView imageView = null;
//        View view = null;
//        for (int i = 0; i < mIconArray.size(); i++) {
//            imageView = new ImageView(mContext);
//            //设置图片
//            // Picasso.with(mContext).load(mIconArray.get(i).getImage()).placeholder(R.drawable.loading2).error(R.drawable.loading2).into(imageView);
//            //添加到列表
//            mIconList.add(imageView);
////                //指示器
////                view = new View(mContext);
////                view.setBackgroundResource(R.drawable.point_background);
////                LayoutParams layoutParams = new LayoutParams(20, 20);
////                layoutParams.leftMargin = 10;
////                //设置参数
////                view.setLayoutParams(layoutParams);
////                view.setEnabled(false);
////                //添加到父控件
////                mPointLL.addView(view);
//        }
//        //数据的长度
//        mDataLength = mIconList.size();
//        overall_page.setText(mDataLength + "");
//        Log.e("打印出来的mDataLength2", mDataLength + "");
//        //默认选中
////            mPointLL.getChildAt(0).setEnabled(true);
//        current_page.setText("1");
//        //初始化适配器
//        mImageLoopAdapter = new ImageLoopAdapter( mIconList);
//        //设置适配器
//        mViewPager.setAdapter(mImageLoopAdapter);
//
//    }

    /**
     * 装填数据
     *
     * @param mIconArray
     */
    public void setImageLoopResources(int[] mIconArray) {
        //删除所有子视图
       // mPointLL.removeAllViews();
        ImageView imageView = null;
        View view = null;
        for (int i = 0; i < mIconArray.length; i++) {
            imageView = new ImageView(mContext);
            //设置图片
            imageView.setBackgroundResource(mIconArray[i]);
            //添加到列表
            mIconList.add(imageView);
//            //指示器
//            view = new View(mContext);
//            view.setBackgroundResource(R.drawable.point_background);
//            LayoutParams layoutParams = new LayoutParams(20, 20);
//            layoutParams.leftMargin = 10;
//            //设置参数
//            view.setLayoutParams(layoutParams);
//            view.setEnabled(false);
//            //添加到父控件
//            mPointLL.addView(view);
        }
        //数据的长度
        mDataLength = mIconList.size();
        overall_page.setText(mDataLength + "");
        //默认选中
//            mPointLL.getChildAt(0).setEnabled(true);
        current_page.setText("1");
//        //初始化适配器
        mImageLoopAdapter = new ImageLoopAdapter( mIconList);
        //设置适配器
        mViewPager.setAdapter(mImageLoopAdapter);
    }
//
//    /**
//     * 装填数据
//     *
//     * @param mIconArray
//     * @param mInfiniteLoopClickListener
//     */
//    public void setImageLoopResources1(int[] mIconArray, InfiniteLoopClickListener mInfiniteLoopClickListener, InfiniteLoopLongClickListener mInfiniteLoopLongClickListener) {
//        //删除所有子视图
//        mPointLL.removeAllViews();
//        ImageView imageView = null;
//        View view = null;
//        mIconList.clear();
//        Log.e("打印出来的+++++++++++++++", mIconArray.length + "");
//        for (int i = 0; i < mIconArray.length; i++) {
//            imageView = new ImageView(mContext);
//            //设置图片
//            imageView.setBackgroundResource(mIconArray[i]);
//            //添加到列表
//            mIconList.add(imageView);
//            //指示器
//            view = new View(mContext);
//            view.setBackgroundResource(R.drawable.point_background);
//            LayoutParams layoutParams = new LayoutParams(20, 20);
//            layoutParams.leftMargin = 10;
//            //设置参数
//            view.setLayoutParams(layoutParams);
//            view.setEnabled(false);
//            //添加到父控件
//            mPointLL.addView(view);
//        }
//        //数据的长度
//        mDataLength = mIconList.size();
//        Log.e("打印出来的+++++++++++++++2", mDataLength + "");
//        //默认选中
//        mPointLL.getChildAt(0).setEnabled(true);
//        //初始化适配器
//        mImageLoopAdapter = new ImageLoopAdapter(mInfiniteLoopClickListener, mInfiniteLoopLongClickListener, mIconList);
//        //设置适配器
//        mViewPager.setAdapter(mImageLoopAdapter);
//    }

    /**
     * 图片轮播控件适配器
     */
    private class ImageLoopAdapter extends PagerAdapter {
        //轮播控件中单个图片监听事件
        private InfiniteLoopClickListener mInfiniteLoopClickListener;
        private InfiniteLoopLongClickListener mInfiniteLoopLongClickListener;
        private ArrayList<ImageView> mIconList;

        public ImageLoopAdapter(ArrayList<ImageView> mIconList) {
            this.mIconList = mIconList;
        }

        public ImageLoopAdapter(InfiniteLoopClickListener mInfiniteLoopClickListener, ArrayList<ImageView> mIconList) {
            this.mInfiniteLoopClickListener = mInfiniteLoopClickListener;
            this.mIconList = mIconList;
        }

        public ImageLoopAdapter(InfiniteLoopClickListener mInfiniteLoopClickListener, InfiniteLoopLongClickListener mInfiniteLoopLongClickListener, ArrayList<ImageView> mIconList) {
            this.mInfiniteLoopClickListener = mInfiniteLoopClickListener;
            this.mInfiniteLoopLongClickListener = mInfiniteLoopLongClickListener;
            this.mIconList = mIconList;
        }

        @Override
        public int getCount() {
            //return Integer.MAX_VALUE;
            return mIconList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //         container.removeView(mIconList.get(position % mDataLength));
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imageView = mIconList.get(position % mDataLength);
            try {
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mInfiniteLoopClickListener.onImageClick(position % mDataLength, (ImageView) v);
                    }
                });
                imageView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mInfiniteLoopLongClickListener.onImageLongClick(position % mDataLength, (ImageView) v);
                        return true;
                    }
                });
                container.addView(imageView);
            } catch (Exception e) {
                //handler something  
            }
            return imageView;
        }
    }

    /**
     * 轮播控件中单个图片监听事件
     */
    public interface InfiniteLoopClickListener {
        /**
         * 单击图片监听事件
         *
         * @param position
         * @param imageView
         */
        void onImageClick(int position, ImageView imageView);
    }

    /**
     * 轮播控件中单个图片长按监听事件
     */
    public interface InfiniteLoopLongClickListener {
        /**
         * 单击图片监听事件
         *
         * @param position
         * @param imageView
         */
        void onImageLongClick(int position, ImageView imageView);
    }
}
