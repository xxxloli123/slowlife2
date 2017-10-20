package com.android.slowlife.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.slowlife.R;
import com.android.slowlife.adapter.MoreAddressAdapter;
import com.android.slowlife.objectmodel.MoreAddressEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/2/11 0011.
 */

public class MoreAddressFragment extends Fragment {
    private ListView listview;
    private MoreAddressAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more_address, container, false);
        ButterKnife.bind(getActivity());
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview= (ListView) view.findViewById(R.id.listview);
        adapter=new MoreAddressAdapter(getActivity(),getList());
        listview.setAdapter(adapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(getActivity());
    }

    /**
     * listview测试数据
     */
    private List<MoreAddressEntity> getList() {
        List<MoreAddressEntity> list = new ArrayList<>();
        MoreAddressEntity entity = null;
        for (int i = 0; i < 10; i++) {
            entity = new MoreAddressEntity();
            entity.setTag("1");
            entity.setAddress("重庆市江北区南桥寺武江西路" + i);
            entity.setName("那可环境优惠店" + i);
            if(i==0){
                entity.setTag("0");
            }
            list.add(entity);
        }
        return list;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
