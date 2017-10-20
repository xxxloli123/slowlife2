package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.DisplayUtil;
import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.util.SimpleCallback;
import com.google.zxing.client.android.CaptureActivity;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class SearchOrderActivity extends BaseActivity {
    @Bind(R.id.order_num)
    EditText editText;
    @Bind(R.id.spinner)
    AppCompatSpinner spinner;
    @Bind(R.id.nothing)
    View view;
    @Bind(R.id.tableLayout)
    TableLayout tabLayout;
    private static final String[][] codes = new String[17][2];

    public static final String ORDERNUM = "number";
    public static final String COMPANY = "company";


    static {
//        顺丰	SF
//        百世快递	HTKY
//        中通	ZTO
//        申通	STO
//        圆通	YTO
//        韵达	YD
//        邮政平邮	YZPY
//        EMS	EMS
//        天天	HHTT
//        京东	JD
//        全峰	QFKD
//        国通	GTO
//        优速	UC
//        德邦	DBL
//        快捷	FAST
//        亚马逊	AMAZON
//        宅急送	ZJS
        codes[0] = new String[]{"请选择快递公司", ""};
        codes[1] = new String[]{"顺丰", "SF"};
        codes[2] = new String[]{"百世汇通", "HTKY"};
        codes[3] = new String[]{"中通", "ZTO"};
        codes[4] = new String[]{"申通", "STO"};
        codes[5] = new String[]{"圆通", "YTO"};
        codes[6] = new String[]{"韵达", "YD"};
        codes[7] = new String[]{"邮政平邮", "YZPY"};
        codes[8] = new String[]{"天天", "HHTT"};
        codes[9] = new String[]{"京东", "JD"};
        codes[10] = new String[]{"全峰", "QFKD"};
        codes[11] = new String[]{"国通", "GTO"};
        codes[12] = new String[]{"优速", "UC"};
        codes[13] = new String[]{"德邦", "DBL"};
        codes[14] = new String[]{"快捷", "FAST"};
        codes[15] = new String[]{"亚马逊", "AMAZON"};
        codes[16] = new String[]{"宅急送", "ZJS"};
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        if (savedInstanceState != null) {
            String orderNum = savedInstanceState.getString("order_num");
            if (!isEmpty(orderNum)) editText.setText(orderNum);
        }
        editText.setId(EditorInfo.IME_ACTION_SEARCH);
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == editText.getId()) {
                    search();
                }
                return false;
            }
        });
        spinner.setAdapter(new Adapter());
        String num = getIntent().getStringExtra(ORDERNUM);
        String company = getIntent().getStringExtra(COMPANY);
        if (!isEmpty(num))
            editText.setText(num);
        if (!isEmpty(company)) {
            for (int i = 0; i < codes.length; i++) {
                final String[] strs = codes[i];
                if (company.indexOf(strs[0]) > -1) {
                    spinner.setSelection(i, true);
                    return;
                }
            }
        }
        if (!isEmpty(num)&&spinner.getSelectedItemPosition()>=0){
            search();
        }
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.sao:
                startActivityForResult(new Intent(this, CaptureActivity.class), 99);
                break;
            case R.id.search:
                search();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 99 && resultCode == Activity.RESULT_OK) {
            String result = data.getStringExtra("result");
            if (isEmpty(result)) {
                Toast.makeText(this, "扫描失败", Toast.LENGTH_SHORT).show();
                return;
            }
            editText.setText(result);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("order_num", editText.getText().toString().trim());
        super.onSaveInstanceState(outState);
    }


    private void search() {
        if (isEmpty(editText.getText().toString().trim())) {
            Toast.makeText(SearchOrderActivity.this, "请输入物流单号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinner.getSelectedItemPosition() == 0) {
            Toast.makeText(SearchOrderActivity.this, "请选择快递公司", Toast.LENGTH_SHORT).show();
            return;
        }
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("expCode", codes[spinner.getSelectedItemPosition()][1])
                .addFormDataPart("expNo", editText.getText().toString().trim())
                .build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl("slowlife/appuser/logistics"))
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(
                new SimpleCallback(this) {
                    @Override
                    public void onSuccess(String tag, JSONObject json) throws JSONException {
                        show(json);
                    }
                }
        );
    }

    private void show(JSONObject json) throws JSONException {
        tabLayout.removeAllViews();
        JSONArray arr = json.getJSONArray("Traces");
        if (arr.length() == 0) {
            view.setVisibility(View.VISIBLE);
            Toast.makeText(this, "暂无数据", Toast.LENGTH_SHORT).show();
            return;
        }
        final int minHei = DisplayUtil.dip2px(this, 50);
        final int maxWidth = tabLayout.getMeasuredWidth();
        final int leftWidth = maxWidth / 3 - 1;
        final int rightWidth = maxWidth / 3 * 2;
        for (int i = 0; i < arr.length(); i++) {
            final JSONObject item = arr.getJSONObject(i);
            final TableRow row = new TableRow(this);
            final TextView left = new TextView(this);
            final TextView right = new TextView(this);
            final TextView line = new TextView(this);
            final TextView line2 = new TextView(this);
            line2.setBackgroundColor(Color.GRAY);
            line.setBackgroundColor(Color.GRAY);
            left.setMinHeight(minHei);
            left.setWidth(leftWidth);
            left.setGravity(Gravity.CENTER);
            right.setMinHeight(minHei);
            right.setWidth(rightWidth);
            right.setGravity(Gravity.CENTER);
            left.setText(item.getString("AcceptTime").replace(" ", "\n"));
            right.setText(item.getString("AcceptStation"));
            row.addView(left);
            row.addView(line, 1, -1);
            row.addView(right);
            row.setGravity(Gravity.CENTER_VERTICAL);
            tabLayout.addView(row);
            tabLayout.addView(line2, -1, 1);
        }
        tabLayout.removeViewAt(tabLayout.getChildCount() - 1);
        tabLayout.requestLayout();
        view.setVisibility(View.GONE);
    }

    class Adapter extends BaseAdapter {
        int hei;

        Adapter() {
            hei = DisplayUtil.dip2px(SearchOrderActivity.this, 35);
        }

        @Override
        public int getCount() {
            return codes.length;
        }

        @Override
        public Object getItem(int position) {
            return codes[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView;
            if (convertView == null) {
                textView = new TextView(SearchOrderActivity.this);
                textView.setGravity(Gravity.CENTER);
                textView.setMinHeight(hei);
            } else textView = (TextView) convertView;
            textView.setText(codes[position][0]);
            return textView;
        }
    }
}
