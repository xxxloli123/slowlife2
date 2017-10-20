package com.android.slowlife.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.BaseFragment;
import com.android.slowlife.R;
import com.android.slowlife.activity.DetailActivity;
import com.android.slowlife.activity.ZoomHeaderViewPagerActivity;
import com.android.slowlife.adapter.LeftMenuAdapter;
import com.android.slowlife.adapter.RightDishAdapter;
import com.android.slowlife.bean.OnItemClickListener;
import com.android.slowlife.bean.ShopCartImp;
import com.android.slowlife.objectmodel.DishEntity;
import com.android.slowlife.objectmodel.DishMenuEntity;
import com.android.slowlife.objectmodel.ShopCartEntity;
import com.android.slowlife.util.PointFTypeEvaluator;
import com.android.slowlife.view.FakeAddImageView;
import com.android.slowlife.view.ShopCartDialog;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/2 .
 */

public class DetailMenuFragment extends BaseFragment implements LeftMenuAdapter.onItemSelectedListener
        , ShopCartImp, ShopCartDialog.ShopCartDialogImp, OnItemClickListener {
    @Bind(R.id.left_menu)
    RecyclerView leftMenu;
    @Bind(R.id.right_menu)
    RecyclerView rightMenu;
    @Bind(R.id.right_menu_tv)
    TextView rightMenuTv;
    @Bind(R.id.right_menu_item)
    LinearLayout rightMenuItem;
    @Bind(R.id.main_layout)
    RelativeLayout mainLayout;
    private boolean leftClickType = false;//左侧菜单点击引发的右侧联动
    private DishMenuEntity headMenu;
    private LeftMenuAdapter leftAdapter;
    public static RightDishAdapter rightAdapter;
    private ArrayList<DishMenuEntity> dishMenuList;//数据源

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_menu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView(view);
        initAdapter();
    }

    private void initView(View view) {
        leftMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        rightMenu.setLayoutManager(new LinearLayoutManager(getActivity()));
        rightMenu.addOnScrollListener(scrollListener);
    }

    /**
     * 模拟商品菜单数据
     */
    private void initData() {
        DetailActivity.shopCart = new ShopCartEntity();
        dishMenuList = new ArrayList<>();
        ArrayList<DishEntity> dishs1 = new ArrayList<>();
        dishs1.add(new DishEntity("面包", 1.0, 10));
        dishs1.add(new DishEntity("蛋挞", 1.0, 10));
        dishs1.add(new DishEntity("牛奶", 1.0, 10));
        dishs1.add(new DishEntity("肠粉", 1.0, 10));
        dishs1.add(new DishEntity("绿茶饼", 1.0, 10));
        dishs1.add(new DishEntity("花卷", 1.0, 10));
        dishs1.add(new DishEntity("包子", 1.0, 10));
        DishMenuEntity breakfast = new DishMenuEntity("早点", dishs1);

        ArrayList<DishEntity> dishs2 = new ArrayList<>();
        dishs2.add(new DishEntity("粥", 1.0, 10));
        dishs2.add(new DishEntity("炒饭", 1.0, 10));
        dishs2.add(new DishEntity("炒米粉", 1.0, 10));
        dishs2.add(new DishEntity("炒粿条", 1.0, 10));
        dishs2.add(new DishEntity("炒牛河", 1.0, 10));
        dishs2.add(new DishEntity("炒菜", 1.0, 10));
        DishMenuEntity launch = new DishMenuEntity("午餐", dishs2);

        ArrayList<DishEntity> dishs3 = new ArrayList<>();
        dishs3.add(new DishEntity("淋菜", 1.0, 10));
        dishs3.add(new DishEntity("川菜", 1.0, 10));
        dishs3.add(new DishEntity("湘菜", 1.0, 10));
        dishs3.add(new DishEntity("粤菜", 1.0, 10));
        dishs3.add(new DishEntity("赣菜", 1.0, 10));
        dishs3.add(new DishEntity("东北菜", 1.0, 10));
        DishMenuEntity evening = new DishMenuEntity("晚餐", dishs3);

        ArrayList<DishEntity> dishs4 = new ArrayList<>();
        dishs4.add(new DishEntity("烧烤", 1.0, 10));
        dishs4.add(new DishEntity("火锅", 1.0, 10));
        dishs4.add(new DishEntity("冒菜", 1.0, 10));
        dishs4.add(new DishEntity("粤菜", 1.0, 10));
        dishs4.add(new DishEntity("赣菜", 1.0, 10));
        dishs4.add(new DishEntity("东北菜", 1.0, 10));
        DishMenuEntity menu1 = new DishMenuEntity("宵夜", dishs4);

        dishMenuList.add(breakfast);
        dishMenuList.add(launch);
        dishMenuList.add(evening);
        dishMenuList.add(menu1);
    }

    /**
     * 把测试数据注入列表和分类列表类名滑动到顶部停住
     */
    private void initAdapter() {
        leftAdapter = new LeftMenuAdapter(getActivity(), dishMenuList);
        rightAdapter = new RightDishAdapter(getActivity(), dishMenuList, DetailActivity.shopCart);
        rightMenu.setAdapter(rightAdapter);
        leftMenu.setAdapter(leftAdapter);
        leftAdapter.addItemSelectedListener(this);
        rightAdapter.setShopCartImp(this);
        rightAdapter.setmOnItemClickListener(this);
        initHeadView();
    }

    private void initHeadView() {
        headMenu = rightAdapter.getMenuOfMenuByPosition(0);
        rightMenuItem.setContentDescription("0");
        rightMenuTv.setText(headMenu.getMenuName());
    }

    private void showHeadView() {
        rightMenuItem.setTranslationY(0);
        View underView = rightMenu.findChildViewUnder(rightMenuTv.getX(), 0);
        if (underView != null && underView.getContentDescription() != null) {
            int position = Integer.parseInt(underView.getContentDescription().toString());
            DishMenuEntity menu = rightAdapter.getMenuOfMenuByPosition(position + 1);
            headMenu = menu;
            rightMenuTv.setText(headMenu.getMenuName());
            for (int i = 0; i < dishMenuList.size(); i++) {
                if (dishMenuList.get(i) == headMenu) {
                    leftAdapter.setSelectedNum(i);
                    break;
                }
            }
        }
    }

    /**
     * 左菜单点击右菜单联动
     *
     * @param position
     * @param menu
     */
    @Override
    public void onLeftItemSelected(int position, DishMenuEntity menu) {
        int sum = 0;
        for (int i = 0; i < position; i++) {
            sum += dishMenuList.get(i).getDishList().size() + 1;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) rightMenu.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(sum, 0);
        leftClickType = true;
    }

    /**
     * 右菜单滑动左菜单联动
     */

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (recyclerView.canScrollVertically(1) == false) {//无法下滑
                showHeadView();
                return;
            }
            View underView = null;
            if (dy > 0)
                underView = rightMenu.findChildViewUnder(rightMenuItem.getX(), rightMenuItem.getMeasuredHeight() + 1);
            else
                underView = rightMenu.findChildViewUnder(rightMenuItem.getX(), 0);
            if (underView != null && underView.getContentDescription() != null) {
                int position = Integer.parseInt(underView.getContentDescription().toString());
                DishMenuEntity menu = rightAdapter.getMenuOfMenuByPosition(position);

                if (leftClickType || !menu.getMenuName().equals(headMenu.getMenuName())) {
                    if (dy > 0 && rightMenuItem.getTranslationY() <= 1 && rightMenuItem.getTranslationY() >= -1 * rightMenuItem.getMeasuredHeight() * 4 / 5 && !leftClickType) {// underView.getTop()>9
                        int dealtY = underView.getTop() - rightMenuItem.getMeasuredHeight();
                        rightMenuItem.setTranslationY(dealtY);
                    } else if (dy < 0 && rightMenuItem.getTranslationY() <= 0 && !leftClickType) {
                        rightMenuTv.setText(menu.getMenuName());
                        int dealtY = underView.getBottom() - rightMenuItem.getMeasuredHeight();
                        rightMenuItem.setTranslationY(dealtY);
                    } else {
                        rightMenuItem.setTranslationY(0);
                        headMenu = menu;
                        rightMenuTv.setText(headMenu.getMenuName());
                        for (int i = 0; i < dishMenuList.size(); i++) {
                            if (dishMenuList.get(i) == headMenu) {
                                leftAdapter.setSelectedNum(i);
                                break;
                            }
                        }
                        if (leftClickType) leftClickType = false;
                    }
                }
            }
        }
    };
    /**
     * 对右菜单添加删除操作购物车联动改变效果
     */
    @Override
    public void add(View view, int position) {

        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        DetailActivity.shoppingCart.getLocationInWindow(cartLocation);
        rightMenu.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1] - recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1] - recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final FakeAddImageView fakeAddImageView = new FakeAddImageView(getActivity());
        mainLayout.addView(fakeAddImageView);
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
                mainLayout.removeView(fakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(DetailActivity.shoppingCart, "scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(DetailActivity.shoppingCart, "scaleY", 0.6f, 1.0f);
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

    public static void showTotalPrice1() {
        DetailActivity.price.setVisibility(View.VISIBLE);
        DetailActivity.price.setText("￥ " + DetailActivity.shopCart.getShoppingTotalPrice());
        DetailActivity.shoppingCartLayout.setBackgroundResource(R.drawable.circle);
        DetailActivity.shoppingCart.setImageResource(R.drawable.chart);
        DetailActivity.tvTotalPrice.setText("另需配送费5元");
        DetailActivity.shoppingCartTotalNum.setVisibility(View.VISIBLE);
        DetailActivity.shoppingCartTotalNum.setText("" + DetailActivity.shopCart.getShoppingAccount());
        DetailActivity.tvCommitOrder.setBackgroundColor(Color.RED);
        DetailActivity.tvCommitOrder.setText("去结算");
    }

    public static void showTotalPrice() {
        if (DetailActivity.shopCart != null && DetailActivity.shopCart.getShoppingTotalPrice() > 0) {
            DetailActivity.price.setVisibility(View.VISIBLE);
            DetailActivity.price.setText("￥ " + DetailActivity.shopCart.getShoppingTotalPrice());
            DetailActivity.shoppingCartLayout.setBackgroundResource(R.drawable.circle);
            DetailActivity.shoppingCart.setImageResource(R.drawable.chart);
            DetailActivity.tvTotalPrice.setText("另需配送费5元");
            DetailActivity.shoppingCartTotalNum.setVisibility(View.VISIBLE);
            DetailActivity.shoppingCartTotalNum.setText("" + DetailActivity.shopCart.getShoppingAccount());
            DetailActivity.tvCommitOrder.setBackgroundColor(Color.RED);
            DetailActivity.tvCommitOrder.setText("去结算");
        } else {
            DetailActivity.price.setVisibility(View.GONE);
            DetailActivity.shoppingCartLayout.setBackgroundResource(R.drawable.circle1);
            DetailActivity.shoppingCart.setImageResource(R.drawable.chart1);
            DetailActivity.tvTotalPrice.setText("购物车为空");
            DetailActivity.tvCommitOrder.setBackgroundColor(Color.GRAY);
            DetailActivity.tvCommitOrder.setText("￥70元起");
            DetailActivity.shoppingCartTotalNum.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void dialogDismiss() {
        showTotalPrice();
        rightAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        leftAdapter.removeItemSelectedListener(this);
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        startActivity(new Intent(getActivity(), ZoomHeaderViewPagerActivity.class));
    }
}
