package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.android.slowlife.R;
import com.android.slowlife.view.BitmapView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ImageDisposeActivity extends AppCompatActivity {

    @Bind(R.id.bitmapView)
    BitmapView bitmapView;
    @Bind(R.id.edit_title)
    TextView title;
    public static final String TITLE = "title";
    public static final String RESULT_IMG = "img";
    public static final String BITMAPPATH = "bitmap_path";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dispose);
        String titleTxt = getIntent().getStringExtra(TITLE);
        if (!TextUtils.isEmpty(titleTxt)) title.setText(titleTxt);
        ButterKnife.bind(this);
        String path = getIntent().getStringExtra(BITMAPPATH);
        Bitmap img = BitmapFactory.decodeFile(path);
        bitmapView.setBitmap(img);
    }


    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_back:
                finish();
                break;
            case R.id.edit_done:
                Intent intent = new Intent();
                intent.putExtra(RESULT_IMG, bitmapView.getDisposedBitmap());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        ButterKnife.unbind(this);
        super.onDestroy();
    }
}
