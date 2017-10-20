package com.android.slowlife.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.adapter.ZoomHeaderViewPagerAdapter;
import com.android.slowlife.bean.ShopCartImp;
import com.android.slowlife.fragment.DetailMenuFragment;
import com.android.slowlife.objectmodel.DishEntity;
import com.android.slowlife.objectmodel.DishMenuEntity;
import com.android.slowlife.objectmodel.ShopCartEntity;
import com.android.slowlife.util.Config;
import com.android.slowlife.util.PointFTypeEvaluator;
import com.android.slowlife.view.FakeAddImageView;
import com.android.slowlife.view.SystemBarTintManager;
import com.android.slowlife.view.ZoomHeaderView;
import com.android.slowlife.view.ZoomHeaderViewPager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/4 .
 */

public class ZoomHeaderViewPagerActivity extends AppCompatActivity implements ShopCartImp {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.tv_close)
    TextView tvClose;
    @Bind(R.id.viewpager)
    ZoomHeaderViewPager mViewPager;
    @Bind(R.id.zoomHeader)
    ZoomHeaderView mZoomHeader;
    @Bind(R.id.rv_bottom)
    RelativeLayout mBottomView;
    @Bind(R.id.activity_main)
    CoordinatorLayout activityMain;
    //    @Bind(R.id.recyclerView1)
//    RecyclerView recyclerView1;
    @Bind(R.id.view3)
    View view3;
    @Bind(R.id.tv_total_price)
    TextView tvTotalPrice;
    @Bind(R.id.tv_commit_order)
    TextView tvCommitOrder;
    @Bind(R.id.shopping_cart_bottom)
    LinearLayout shoppingCartBottom;
    @Bind(R.id.shopping_cart)
    ImageView shoppingCart;
    @Bind(R.id.shopping_cart_layout)
    FrameLayout shoppingCartLayout;
    @Bind(R.id.shopping_cart_total_num)
    TextView shoppingCartTotalNum;
    @Bind(R.id.price)
    TextView price;
//    @Bind(R.id.main_abl_app_bar)
//    AppBarLayout mainAblAppBar;
    private boolean isFirst = true;
    public static int bottomY;
    private ShopCartEntity shopCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(Color.TRANSPARENT);//状态栏设置为透明色
            tintManager.setNavigationBarTintEnabled(true);
            tintManager.setNavigationBarTintResource(Color.TRANSPARENT);//导航栏设置为透明色
        }
        shopCart = new ShopCartEntity();
        ArrayList<DishMenuEntity> dishMenuList = new ArrayList<>();
        ArrayList<DishEntity> dishs1 = new ArrayList<>();
        dishs1.add(new DishEntity("面包", 1.0, 10));
        dishs1.add(new DishEntity("蛋挞", 1.0, 10));
        dishs1.add(new DishEntity("牛奶", 1.0, 10));
        dishs1.add(new DishEntity("肠粉", 1.0, 10));
        dishs1.add(new DishEntity("绿茶饼", 1.0, 10));
        dishs1.add(new DishEntity("花卷", 1.0, 10));
        dishs1.add(new DishEntity("包子", 1.0, 10));
        DishMenuEntity breakfast = new DishMenuEntity("早点", dishs1);
        dishMenuList.add(breakfast);
        setContentView(R.layout.layout_zoom_viewpager);
        ButterKnife.bind(this);
        Adapter adapter1 = new Adapter(dishMenuList);
        adapter1.setShopCartImp(this);
        mViewPager.setAdapter(adapter1);
        mViewPager.setOffscreenPageLimit(4);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new ZoomHeaderViewPagerAdapter());
        recyclerView.setAlpha(0);

        // AppBar的监听
//        mainAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
//            @Override
//            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//                int maxScroll = appBarLayout.getTotalScrollRange();
//                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
//            }
//        });
//        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView1.setAdapter(new ZoomHeaderViewPagerAdapter());
//        recyclerView1.setAlpha(0);

    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
            for (int i = 0; i < mViewPager.getChildCount(); i++) {
                View v = mViewPager.getChildAt(i).findViewById(R.id.ll_bottom);
                v.setY(mViewPager.getChildAt(i).findViewById(R.id.imageView).getHeight());
                v.setX(Config.MARGIN_LEFT_RIGHT);
                //触发一次dependency变化，让按钮归位
                mZoomHeader.setY(mZoomHeader.getY() - 1);
                isFirst = false;
            }
        }
        //隐藏底部栏]
        bottomY = (int) mBottomView.getY();
        mBottomView.setTranslationY(mBottomView.getY() + mBottomView.getHeight());
        mZoomHeader.setmBottomView(mBottomView, bottomY);
    }

    @Override
    public void add(View view, int postion) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shoppingCart.getLocationInWindow(cartLocation);
//        rightMenu.getLocationInWindow(recycleLocation);
        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();
        startP.x = addLocation[0];
        startP.y = addLocation[1] - recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1] - recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;
        final FakeAddImageView fakeAddImageView = new FakeAddImageView(this);
        activityMain.addView(fakeAddImageView);
        fakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp);
        fakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(fakeAddImageView, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fakeAddImageView.setVisibility(View.GONE);
                activityMain.removeView(fakeAddImageView);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shoppingCart, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shoppingCart, "scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();
        showTotalPrice();
    }

    @Override
    public void remove(View view, int position) {
        showTotalPrice();
    }

    private void showTotalPrice() {
        if (shopCart != null && shopCart.getShoppingTotalPrice() > 0) {
            price.setVisibility(View.VISIBLE);
            price.setText("￥ " + shopCart.getShoppingTotalPrice());
            shoppingCartLayout.setBackgroundResource(R.drawable.circle);
            shoppingCart.setImageResource(R.drawable.chart);
            tvTotalPrice.setText("另需配送费5元");
            shoppingCartTotalNum.setVisibility(View.VISIBLE);
            shoppingCartTotalNum.setText("" + shopCart.getShoppingAccount());
            tvCommitOrder.setBackgroundColor(Color.RED);
            tvCommitOrder.setText("去结算");
        } else {
            price.setVisibility(View.GONE);
            shoppingCartLayout.setBackgroundResource(R.drawable.circle1);
            shoppingCart.setImageResource(R.drawable.chart1);
            tvTotalPrice.setText("购物车为空");
            tvCommitOrder.setBackgroundColor(Color.GRAY);
            tvCommitOrder.setText("￥70元起");
            shoppingCartTotalNum.setVisibility(View.INVISIBLE);
        }
    }

    class Adapter extends PagerAdapter {

        private ArrayList<DishMenuEntity> dishMenuList;
        private ShopCartImp shopCartImp;

        public Adapter(ArrayList<DishMenuEntity> dishMenuList) {
            this.dishMenuList = dishMenuList;
            views = new ArrayList<>();
            views.add((RelativeLayout) View.inflate(getApplicationContext(), R.layout.item_img, null));
        }

        public ShopCartImp getShopCartImp() {
            return shopCartImp;
        }

        public void setShopCartImp(ShopCartImp shopCartImp) {
            this.shopCartImp = shopCartImp;
        }

        public DishEntity getDishByPosition(int position) {
            for (DishMenuEntity menu : dishMenuList) {
                if (position > 0 && position <= menu.getDishList().size()) {
                    return menu.getDishList().get(position - 1);
                } else {
                    position -= menu.getDishList().size() + 1;
                }
            }
            return null;
        }

        private ArrayList<RelativeLayout> views;
        private int[] imgs = {R.mipmap.pic2, R.mipmap.pic2, R.mipmap.pic3};

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final DishEntity dish = dishMenuList.get(position).getDishList().get(position);
            views.get(position).findViewById(R.id.imageView).setBackgroundResource(imgs[position]);
            views.get(position).findViewById(R.id.btn_buy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (shopCart.addShoppingSingle(dish)) {
                        shopCartImp.add(v, position);
                        DetailMenuFragment.showTotalPrice1();
                    }
                }
            });
            container.addView(views.get(position));
            return views.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (mZoomHeader.isExpand()) {
            mZoomHeader.restore(mZoomHeader.getY());
        } else {
            finish();
        }

    }
}
