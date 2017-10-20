package com.android.slowlife.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.slowlife.BaseActivity;
import com.android.slowlife.R;
import com.android.slowlife.adapter.UseRpkgAdapter;
import com.android.slowlife.app.MyApplication;
import com.android.slowlife.objectmodel.RPkg;
import com.android.slowlife.util.SimpleCallback;
import com.google.gson.Gson;
import com.interfaceconfig.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UseRPKGActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    private String[] ids;
    private UseRpkgAdapter adapter;
    private ListView listView;
    private List<Integer> selectedPositions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_rpkg);
        int count = getIntent().getIntExtra("count", 0);
        if (count <= 0) {
            Toast.makeText(this, "参数错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        ids = new String[count];
        selectedPositions = new LinkedList<>();
        init();
    }

    @Override
    protected void init() {
        listView = findView(R.id.listview);
        listView.setItemsCanFocus(false);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        adapter = new UseRpkgAdapter(this, null, "UnUsed");
        adapter.setSelectAble(true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("userId", ((MyApplication) getApplicationContext()).getInfo().getId())
                .addFormDataPart("type", "UnUsed").build();
        Request request = new Request.Builder()
                .url(Config.Url.getUrl(Config.REDPKG))
                .post(requestBody)
                .build();
        new OkHttpClient().newCall(request).enqueue(new SimpleCallback() {
            @Override
            public void onSuccess(String tag, JSONObject json) throws JSONException {
                try {
                    JSONArray arr = json.getJSONArray("RedPackets");
                    Gson gson = new Gson();
                    List<RPkg> data = new ArrayList<>();
                    for (int i = 0; i < arr.length(); i++) {
                        final JSONObject item = arr.getJSONObject(i);
                        if (item.has("gainNumber"))
                            item.put("name", String.format("%s (库存%s)", item.getString("name"), item.getString("gainNumber")));
                        data.add(gson.fromJson(arr.getString(i), RPkg.class));
                    }
                    adapter.notifyDataSetChanged(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_rl:
                finish();
                break;
            case R.id.done:
                Intent result = new Intent();
                ArrayList<RPkg> pkgs = new ArrayList<>();
                for (int i = 0; i < selectedPositions.size(); i++) {
                    pkgs.add(adapter.getData().get(selectedPositions.get(i)));
                }
                result.putExtra("pkgs", pkgs);
                setResult(RESULT_OK,result);
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int count = listView.getCheckedItemCount();
        if (selectedPositions.contains(position)) {
            selectedPositions.remove((Integer) position);
        } else {
            if (ids.length < count) {
                listView.setItemChecked(selectedPositions.get(0), false);
                selectedPositions.remove(0);
            }
            selectedPositions.add(position);
        }
    }
}
