package com.android.slowlife.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by xxxloli on 2017/12/5.
 */

public final class EditTextChange extends EditText {

    private Context mContext;
    private Context contextl;

    public EditTextChange(Context context) {
        super(context);
        mContext = context;
    }

    public EditTextChange(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    public boolean onTextContextMenuItem(int id) {

        switch (id) {
            case android.R.id.cut:
                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onCut(null);
                    break;
                }
            case android.R.id.copy:
                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onCopy(null);
                    break;
                }
            case android.R.id.paste:
                if (mContext instanceof IClipCallback) {
                    ((IClipCallback) mContext).onPaste(null);
                }
        }
        return super.onTextContextMenuItem(id);
    }

    public interface IClipCallback {
        void onCut(Object o);
        void onCopy(Object o);
        void onPaste(Object o);
    }
}