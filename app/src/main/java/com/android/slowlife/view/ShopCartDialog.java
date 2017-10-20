package com.android.slowlife.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.adapter.PopupDishAdapter;
import com.android.slowlife.bean.ShopCartImp;
import com.android.slowlife.objectmodel.ShopCartEntity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopCartDialog extends Dialog implements View.OnClickListener, ShopCartImp {
    @Bind(R.id.clear_layout)
    TextView clearLayout;
    @Bind(R.id.recycleview)
    RecyclerView recyclerView;
    @Bind(R.id.linearlayout)
    LinearLayout linearLayout;
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
    @Bind(R.id.layout_main)
    RelativeLayout layoutMain;
    @Bind(R.id.price)
    TextView price;
    private ShopCartEntity shopCart;
    private PopupDishAdapter dishAdapter;
    private ShopCartDialogImp shopCartDialogImp;

    public ShopCartDialog(Context context, ShopCartEntity shopCart, int themeResId) {
        super(context, themeResId);
        this.shopCart = shopCart;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_popupview);
        ButterKnife.bind(this);
        shoppingCartBottom.setAlpha(0.0f);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dishAdapter = new PopupDishAdapter(getContext(), shopCart);
        recyclerView.setAdapter(dishAdapter);
        dishAdapter.setShopCartImp(this);
        showTotalPrice();
    }

    @Override
    public void show() {
        super.show();
        animationShow(1000);
    }

    @Override
    public void dismiss() {
        animationHide(1000);
    }

    private void showTotalPrice() {
        if (shopCart != null && shopCart.getShoppingTotalPrice() > 0) {
            price.setVisibility(View.VISIBLE);
            price.setText("￥ " + shopCart.getShoppingTotalPrice());
            shoppingCartLayout.setBackgroundResource(R.drawable.circle);
            shoppingCart.setImageResource(R.drawable.chart);
            tvTotalPrice.setText( "另需配送费5元");
            tvTotalPrice.setVisibility(View.VISIBLE);
            shoppingCartTotalNum.setVisibility(View.VISIBLE);
            shoppingCartTotalNum.setText("" + shopCart.getShoppingAccount());
        } else {
            price.setVisibility(View.GONE);
            shoppingCartLayout.setBackgroundResource(R.drawable.circle1);
            shoppingCart.setImageResource(R.drawable.chart1);
            tvTotalPrice.setText("购物车为空");
            shoppingCartTotalNum.setVisibility(View.INVISIBLE);
        }
    }
    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY", 0, 1000).setDuration(mDuration)
        );
        animatorSet.start();

        if (shopCartDialogImp != null) {
            shopCartDialogImp.dialogDismiss();
        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ShopCartDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }


    @Override
    public void add(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
        if (shopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }

    public ShopCartDialogImp getShopCartDialogImp() {
        return shopCartDialogImp;
    }

    public void setShopCartDialogImp(ShopCartDialogImp shopCartDialogImp) {
        this.shopCartDialogImp = shopCartDialogImp;
    }

    @OnClick({R.id.clear_layout, R.id.shopping_cart_bottom, R.id.shopping_cart_layout, R.id.layout_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear_layout:
                clear();
                break;
            case R.id.shopping_cart_bottom:
                break;
            case R.id.shopping_cart_layout:
                break;
            case R.id.layout_main:
                break;
        }
        this.dismiss();
    }

    public interface ShopCartDialogImp {
        public void dialogDismiss();
    }

    public void clear() {
        shopCart.clear();
        showTotalPrice();
        if (shopCart.getShoppingAccount() == 0) {
            this.dismiss();
        }
    }
}
