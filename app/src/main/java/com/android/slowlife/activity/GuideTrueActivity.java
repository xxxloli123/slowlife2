package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.slowlife.MainActivity;
import com.android.slowlife.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GuideTrueActivity extends Activity {

    @Bind(R.id.cancel)
    ImageView cancel;

    private SharedPreferences SP_FirstLunch;
    private SharedPreferences.Editor SP_FirstLunch_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_true);
        ButterKnife.bind(this);
        main();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent intent=new Intent();
                intent.setClass(GuideTrueActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }


    //主程序接口
    private void main() {
        SP_FirstLunch=getSharedPreferences("FirstChk",MODE_PRIVATE);
        //判断是否首次启动
        if (SP_FirstLunch.getBoolean("FirstAllow",false)!=true){

            SP_FirstLunch_editor=SP_FirstLunch.edit();
            SP_FirstLunch_editor.putBoolean("FirstAllow",true);
            SP_FirstLunch_editor.commit();
        }else {
            Intent intent=new Intent(GuideTrueActivity.this,MainActivity.class);
            startActivity(intent);
            GuideTrueActivity.this.finish();
        }
    }
}
