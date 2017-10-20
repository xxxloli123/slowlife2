package com.android.slowlife;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

/**
 * Created by sgrape on 2017/6/27.
 * e-mail: sgrape1153@gmail.com
 */

public class MsgDialog extends Dialog {
    private Activity activity;

    public MsgDialog(@NonNull Activity context) {
        super(context, R.style.DialogStyle);
        this.activity = context;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_msg);
        findViewById(R.id.dialog_msg_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        activity.finish();
                    }
                }
        );
    }
}
