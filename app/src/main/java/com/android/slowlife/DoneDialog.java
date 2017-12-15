package com.android.slowlife;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

/**
 * Created by sgrape on 2017/6/27.
 * e-mail: sgrape1153@gmail.com
 */

public class DoneDialog<T> extends Dialog {
    private TextView msg;
    private DialogButtonClickListener<T> listener;
    private T tag;

    public DoneDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        setContentView(R.layout.dialog_done);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        msg = (TextView) findViewById(R.id.dialog_msg);
        findViewById(R.id.dialog_calcel)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.cancel(DoneDialog.this, tag);
                    }
                });
        findViewById(R.id.dialog_done).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listener != null)
                            listener.done(DoneDialog.this, tag);
                    }
                }
        );
    }

    public DoneDialog<T> setMessage(CharSequence message) {
        if (msg==null)msg= (TextView) findViewById(R.id.dialog_msg);
        msg.setText(message);
        return this;
    }

    public DoneDialog<T> setListener(DialogButtonClickListener l) {
        listener = l;
        return this;
    }

    public void setVisible(){

    }

    public T getTag() {
        return tag;
    }

    public DoneDialog<T> setTag(T tag) {
        this.tag = tag;
        return this;
    }

    public static interface DialogButtonClickListener<T> {
        void done(Dialog dialog, T tag);

        void cancel(Dialog dialog, T tag);
    }
}
