package com.slowlife.xgpush;

import android.content.Context;

import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sgrape on 2017/4/29.
 */

public class XgReceiver extends XGPushBaseReceiver {

    @Override
    public void onRegisterResult(Context context, int i, XGPushRegisterResult xgPushRegisterResult) {
        System.out.println(xgPushRegisterResult);
    }

    @Override
    public void onUnregisterResult(Context context, int i) {

    }

    @Override
    public void onSetTagResult(Context context, int i, String s) {
        System.out.println(s);
    }

    @Override
    public void onDeleteTagResult(Context context, int i, String s) {
        System.out.println(s);
    }

    @Override
    public void onTextMessage(Context context, XGPushTextMessage xgPushTextMessage) {
        System.out.println(xgPushTextMessage);
        PushObserver.notifyPsuh("title=" + xgPushTextMessage.getTitle() +
                "\ncontent=" + xgPushTextMessage.getContent() +
                "\ncustomContent=" + xgPushTextMessage.getCustomContent());
    }

    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult xgPushClickedResult) {
        System.out.println(xgPushClickedResult);
    }

    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult xgPushShowedResult) {
        System.out.println(xgPushShowedResult);

    }


    public static interface PushCallback {

        void onPush(XGPushTextMessage xgPushTextMessage);

        void onPush(String message);
    }

    public static class PushObserver {
        private static List<PushCallback> callbacks = new ArrayList<>();

        public static void regisiter(PushCallback callback) {
            if (!callbacks.contains(callback)) callbacks.add(callback);
        }

        public static void unRegisiter(PushCallback callback) {
            if (callbacks.contains(callback)) callbacks.remove(callback);
        }

        public void clear() {
            callbacks.clear();
        }

        public static void notifyPsuh(XGPushTextMessage xgPushTextMessage) {
            for (PushCallback callback : callbacks) {
                callback.onPush(xgPushTextMessage);
            }
        }

        public static void notifyPsuh(String message) {
            for (PushCallback callback : callbacks) {
                callback.onPush(message);
            }
        }
    }
}
