package com.android.slowlife.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.slowlife.R;
import com.android.slowlife.activity.DetailActivity;
import com.android.slowlife.activity.SisterMarketActivity;
import com.android.slowlife.adapter.ShopListAdapter;
import com.android.slowlife.objectmodel.ShopEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FragmentContent extends Fragment {
    @Bind(R.id.listview)
    ListView listview;

    public static Fragment getInstance(Bundle bundle) {
        FragmentContent fragment = new FragmentContent();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmnet_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        final List<ShopEntity> hotList = getShopList();
        ShopListAdapter adapter = new ShopListAdapter(hotList, getActivity());
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("----","listclick");
                startActivity(new Intent(getActivity(), DetailActivity.class));
            }
        });
    }

    private List<ShopEntity> getShopList() {

        List<ShopEntity> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ShopEntity shopEntity = new ShopEntity();
            shopEntity.shopName = "王家铺子" + i;
            list.add(shopEntity);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
