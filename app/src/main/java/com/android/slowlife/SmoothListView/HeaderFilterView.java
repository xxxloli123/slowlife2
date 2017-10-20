package com.android.slowlife.SmoothListView;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

import com.android.slowlife.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by sunfusheng on 16/4/20.
 */
public class HeaderFilterView extends AbsHeaderView<Object> {


    @Bind(R.id.fake_filterView)
    FilterView fakeFilterView;

    public HeaderFilterView(Activity context) {
        super(context);
    }

    @Override
    protected void getView(Object obj, ListView listView) {
        View view = mInflate.inflate(R.layout.header_filter_layout, listView, false);
        ButterKnife.bind(this, view);
        listView.addHeaderView(view);
    }

    public FilterView getFilterView() {
        return fakeFilterView;
    }

}
