package com.android.slowlife;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.slowlife.view.BitmapView;
import com.dialog.LoadDialog;

import okhttp3.OkHttpClient;

public class TestActivity extends AppCompatActivity {

    OkHttpClient okHttpClient = new OkHttpClient();

    public static LinearLayout layout;
    public static TextView express;
    private BitmapView bitmapView;
    private LoadDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        dialog = new LoadDialog(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setTitle("title")
                .setMessage("message")
                .setNegativeButton("negative", null)
                .setNeutralButton("neutral", null).create();
        dialog.show();
    }

    public void onClick(View v) {
        dialog.show();
    }

}
