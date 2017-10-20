package com.android.slowlife.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.slowlife.MainActivity;
import com.android.slowlife.R;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by Administrator on 2017/1/22 0022.
 */

public class Common {
    public static String selectProvince = "选择区域";
    /**
     * 判断是否为空
     */
    public static boolean isNull(Object o) {
        if (o == null || o.equals("") || o == "" || o == "null"
                || o.equals("null") || o.equals("undefined")) {
            return true;
        }
        if (o != null) {
            if (o.toString().replaceAll("\\s*", "").length() != 0) {
                return false;
            }
        }
        return true;
    }
    /**
     * 复制文本到剪切板
     * */
    public static void copyText(Context mContext,String text){
        ClipboardManager copy = (ClipboardManager) mContext
                .getSystemService(Context.CLIPBOARD_SERVICE);
        copy.setText(text);
        Toast.makeText(mContext, "复制成功", Toast.LENGTH_SHORT).show();
    }
    /**
     * 弹出拨打电话窗口
     */
    public static void phoneDialog(final Context mContext,String phone) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        LinearLayout mDialog = (LinearLayout) inflater.inflate(R.layout.dialog_revise_phone, null);
        final Dialog dialog = new AlertDialog.Builder(mContext).create();
        final TextView text = (TextView) mDialog.findViewById(R.id.title);
        text.setText(phone);
        Button mYes = (Button) mDialog.findViewById(R.id.sure_bt);
        mYes.setText("呼叫");
        mYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = text.getText().toString().trim();
                dialPhone(mContext,s);
                dialog.dismiss();
            }
        });
        Button mNo = (Button) mDialog.findViewById(R.id.cancel_bt);
        mNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setContentView(mDialog);
    }
    /**
     * 拨打电话
     * */
    public static void dialPhone(Context mContext,String phone){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        mContext.startActivity(intent);
    }
    /**
     * 删除指定的字符串
     * */
    public static String getString(String s, String deleteStr)//s是需要删除某个子串的字符串deleteStr是需要删除的子串
    {
        int postion = s.indexOf(deleteStr);
        int length = deleteStr.length();
        int Length = s.length();
        String newString = s.substring(0,postion) + s.substring(postion + length, Length);
        return newString;//返回已经删除好的字符串
    }
    /**
     *把客户端对象值赋值给服务器端对象(不为空的值)
     */
    public static Object objectMerge(Object serverObject, Object clientObject,
                                     String[] attributes) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        Field s_field = null, c_field = null;
        // 属性名
        for (String attr : attributes) {
            s_field = serverObject.getClass().getDeclaredField(attr);
            c_field = clientObject.getClass().getDeclaredField(attr);
            s_field.setAccessible(true);
            c_field.setAccessible(true);
            s_field.set(serverObject, c_field.get(clientObject)); // 对象合并允许为空
        }
        return serverObject;
    }
    /**
     * 判断一个对象的必填属性
     * */
    public static boolean isObjectRequiredAttributes(Object o,
                                                     String[] attributes) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        boolean result = true;
        Field field = null;
        for (String attr : attributes) {
            field = o.getClass().getDeclaredField(attr);
            field.setAccessible(true);
            if (field.get(o) == null || field.get(o) == ""
                    || "".equals((field.get(o) + "").trim())) {
                result = false;
                break;
            }
        }
        return result;
    }
    /**
    *获取状态栏高度
    */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView()
                .getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass
                        .getField("status_bar_height").get(localObject)
                        .toString());
                statusHeight = activity.getResources()
                        .getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }
    /**
     * 注册信鸽推送接口
     * */
    public static void getRegisterPush(MainActivity activity){
        // 1.获取设备Token
        Handler handler = new HandlerExtension(activity);
        final Message m = handler.obtainMessage();
        // 开启logcat输出，方便debug，发布时请关闭
        // XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        // 注册接口
        XGPushManager.registerPush(activity.getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.e(Constants.LogTag,
                                "+++ register push sucess. token:" + data);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.e(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);

                        m.obj = "+++ register push fail. token:" + data
                                + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                    }
                });
    }
    private static class HandlerExtension extends Handler {
        WeakReference<MainActivity> mActivity;

        HandlerExtension(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity theActivity = mActivity.get();
            if (theActivity == null) {
                theActivity = new MainActivity();
            }
            if (msg != null) {
                Log.e(Constants.LogTag, msg.obj.toString());
                Log.e("getToken-------", XGPushConfig.getToken(theActivity));
            }
        }
    }
}
