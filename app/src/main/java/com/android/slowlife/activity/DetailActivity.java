package com.android.slowlife.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity1;
import com.android.slowlife.R;
import com.android.slowlife.adapter.MyDetailAdapter;
import com.android.slowlife.fragment.DetailAppraiseFragment;
import com.android.slowlife.fragment.DetailMenuFragment;
import com.android.slowlife.objectmodel.ShopCartEntity;
import com.android.slowlife.util.CacheActivity;
import com.android.slowlife.util.Common;
import com.android.slowlife.view.PagerSlidingTabStrip;
import com.android.slowlife.view.ShopCartDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/2/1 .
 */

public class DetailActivity extends BaseActivity1 implements View.OnClickListener, ShopCartDialog.ShopCartDialogImp {
    @Bind(R.id.img_icon)
    ImageView imgIcon;
    @Bind(R.id.right_dish_name)
    TextView rightDishName;
    @Bind(R.id.right_dish_count)
    TextView rightDishCount;
    @Bind(R.id.right_dish_price)
    TextView rightDishPrice;
    @Bind(R.id.right_dish_account)
    ImageView rightDishAccount;
    @Bind(R.id.tabs)
    PagerSlidingTabStrip tabs;
    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.top_layout)
    FrameLayout topLayout;
    @Bind(R.id.campaign_layout)
    LinearLayout campaignLayout;
    @Bind(R.id.back_rl)
    RelativeLayout backRl;
    @Bind(R.id.rlayout)
    RelativeLayout rlayout;
    @Bind(R.id.business_phone)
    ImageView businessPhone;
    @Bind(R.id.layout)
    LinearLayout layout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.layout1)
    LinearLayout layout1;
    @Bind(R.id.main_abl_app_bar)
    AppBarLayout mainAblAppBar;
    @Bind(R.id.relativeLayout3)
    RelativeLayout relativeLayout3;
    @Bind(R.id.shop_name)
    TextView shopName;
    //购物车
    public static TextView tvTotalPrice;
    public static TextView tvCommitOrder;
    public static ImageView shoppingCart;
    public static FrameLayout shoppingCartLayout;
    public static TextView shoppingCartTotalNum;
    public static TextView price;
    private LinearLayout shoppingCartBottom;
    public static ShopCartEntity shopCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(this)) {
            CacheActivity.addActivity(this);
        }
        setContentView(R.layout.layout_detail);
        ButterKnife.bind(this);
        initView();
        initPagers();
        suspensionIcon();
        mainAblAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset <= -layout.getHeight() / 2) {
                    shopName.setText(rightDishName.getText().toString().trim());
                } else {
                    shopName.setText(" ");
                }
            }
        });
    }

    private void initView() {
        price = (TextView) findViewById(R.id.price);
        shoppingCartLayout = (FrameLayout) findViewById(R.id.shopping_cart_layout);
        shoppingCart = (ImageView) findViewById(R.id.shopping_cart);
        tvTotalPrice = (TextView) findViewById(R.id.tv_total_price);
        tvCommitOrder = (TextView) findViewById(R.id.tv_commit_order);
        shoppingCartTotalNum = (TextView) findViewById(R.id.shopping_cart_total_num);
        shoppingCartBottom = (LinearLayout) findViewById(R.id.shopping_cart_bottom);
        shoppingCartBottom.setAlpha(0.8f);
        tvTotalPrice.setText("购物车为空");
    }
    /**
     * 已选购物车列表
     *
     * @param view
     */
    private void showCart(View view) {
        if (shopCart != null && shopCart.getShoppingAccount() > 0) {
            ShopCartDialog dialog = new ShopCartDialog(this, shopCart, R.style.cartdialog);
            Window window = dialog.getWindow();
            dialog.setShopCartDialogImp(this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            params.dimAmount = 0.5f;
            window.setAttributes(params);
        }
    }

    private Fragment menuFragment, appraiseFragment;
    private List<Fragment> pages = new ArrayList<Fragment>();

    private void initPagers() {
        ArrayList<String> list = new ArrayList<>();
        list.add("商品");
        list.add("评价" + "(4.8分)");
        if (menuFragment == null) {
            menuFragment = new DetailMenuFragment();
        }
        if (appraiseFragment == null) {
            appraiseFragment = new DetailAppraiseFragment();
        }
        pages.add(menuFragment);
        pages.add(appraiseFragment);

        pager.setAdapter(new MyDetailAdapter(getSupportFragmentManager(), pages, list));
        pager.setCurrentItem(0);
        tabs.setViewPager(pager);
        tabs.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.d("", "onPageSelected:" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabs.setOnPagerTitleItemClickListener(new PagerSlidingTabStrip.OnPagerTitleItemClickListener() {
            @Override
            public void onSingleClickItem(int position) {
//                Toast.makeText(getApplicationContext(), "单击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDoubleClickItem(int position) {
//                Toast.makeText(getApplicationContext(), "双击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_commit_order, R.id.relativeLayout3, R.id.business_phone, R.id.back_rl, R.id.rlayout, R.id.top_layout, R.id.campaign_layout})
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.top_layout://商家详情
                intent = new Intent(this, MerchantEtailsActivity.class);
                startActivity(intent);
                break;
            case R.id.campaign_layout://活动
                break;
            case R.id.back_rl:
                finish();
                break;
            case R.id.rlayout:
                popwindows();
                break;
            case R.id.relativeLayout3:
                showCart(view);
                break;
            case R.id.tv_commit_order:
                intent = new Intent(this, ConfirmOrderActivity.class);
                startActivity(intent);
                break;
            case R.id.business_phone:
                Common.dialPhone(this, "023-12345678");
                break;
            case R.id.store_search://店内搜索
                Toast.makeText(this, "店内搜索", Toast.LENGTH_SHORT).show();
                break;
            case R.id.share_merchant://分享商家
                Toast.makeText(this, "分享商家", Toast.LENGTH_SHORT).show();
                break;
            case R.id.merchant_details://商家详情
                intent = new Intent(this, MerchantEtailsActivity.class);
                startActivity(intent);
                break;
            case R.id.collection_merchant://收藏商家
                Toast.makeText(this, "收藏商家", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 弹出popwindows
     */
    private void popwindows() {
        LayoutInflater mInflater = LayoutInflater.from(DetailActivity.this);
        View view = mInflater.inflate(R.layout.popwindows_detail, null);
        final PopupWindow popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //店内搜索
        view.findViewById(R.id.store_search).setOnClickListener(DetailActivity.this);
        //分享商家
        view.findViewById(R.id.share_merchant).setOnClickListener(DetailActivity.this);
        //商家详情
        view.findViewById(R.id.merchant_details).setOnClickListener(DetailActivity.this);
        //收藏商家
        view.findViewById(R.id.collection_merchant).setOnClickListener(DetailActivity.this);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        // 允许点击外部消失
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        //设置popwindows弹窗在某控件下面
        popupWindow.showAsDropDown(rlayout);
    }

    /**
     * 悬浮图标，可拖动
     */
    private void suspensionIcon() {
        final int screenHeight;
        final int screenWidth;
        final SharedPreferences sp;
        screenHeight = getWindowManager().getDefaultDisplay()
                .getHeight();
        screenWidth = getWindowManager().getDefaultDisplay()
                .getWidth();
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        int lastx = sp.getInt("lastx", 0);
        int lasty = sp.getInt("lasty", 0);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) businessPhone.getLayoutParams();
        params.leftMargin = lastx;
        params.topMargin = lasty;
        businessPhone.setLayoutParams(params);
        businessPhone.setOnTouchListener(new View.OnTouchListener() {
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 手指第一次触摸到屏幕
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:// 手指移动
                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        int dx = newX - startX;
                        int dy = newY - startY;
                        // 计算出来控件原来的位置
                        int l = businessPhone.getLeft();
                        int r = businessPhone.getRight();
                        int t = businessPhone.getTop();
                        int b = businessPhone.getBottom();
                        int newt = t + dy;
                        int newb = b + dy;
                        int newl = l + dx;
                        int newr = r + dx;
                        if ((newl < 0) || (newt < 0)
                                || (newr > screenWidth)
                                || (newb > screenHeight)) {
                            break;
                        }
                        // 更新iv在屏幕的位置.
                        businessPhone.layout(newl, newt, newr, newb);
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_UP: // 手指离开屏幕的一瞬间
                        int lastx = businessPhone.getLeft();
                        int lasty = businessPhone.getTop();
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("lastx", lastx);
                        editor.putInt("lasty", lasty);
                        editor.commit();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void dialogDismiss() {
        DetailMenuFragment.showTotalPrice();
        DetailMenuFragment.rightAdapter.notifyDataSetChanged();
    }
}
