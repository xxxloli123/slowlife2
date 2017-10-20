package com.android.slowlife.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.android.slowlife.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class CameraActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback, Camera.PictureCallback {
    private SurfaceView surfaceView;
    private SurfaceHolder holder;
    private Camera camera;
    private Camera.Parameters parameters;
    private byte[] preBuf;
    private String outputPath;
    public static final String EXTRA_OUTPUT = "EXTRA_OUTPUT";
    private static File DEFULT_OUTPUT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        CameraActivity.DEFULT_OUTPUT = new File(getExternalCacheDir(), "/imgs/tem.jpg");
        outputPath = getIntent().getStringExtra(EXTRA_OUTPUT);
    }

    public static File getDefultOutput(Context context) {
        return DEFULT_OUTPUT;
    }

    public void onClick(View v) {
        camera.takePicture(null, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (surfaceView == null)
            surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        holder = surfaceView.getHolder();
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);// 为了兼容Honeycomb之前版本的设备。
        holder.addCallback(this);
        if (camera == null) camera = Camera.open(0);
//        startCamera(surfaceView.getHolder());
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startCamera(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        stopCamera();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (preBuf == null && data != null) {
            preBuf = new byte[data.length];
        }
        if (preBuf != null) camera.addCallbackBuffer(preBuf);
    }

    private void startCamera(SurfaceHolder holder) {
        try {
            DisplayMetrics outMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
            parameters = camera.getParameters();
            parameters.setPictureFormat(ImageFormat.JPEG);//设置拍照后存储的图片格式
            parameters.setPictureSize(outMetrics.widthPixels, outMetrics.heightPixels);
            parameters.setPreviewSize(800, 400);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            camera.setParameters(parameters);
            camera.setPreviewCallback(this);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }

    private void stopCamera() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        File outputFile;
        if (outputPath != null) {
            outputFile = new File(outputPath);
        } else outputFile = CameraActivity.DEFULT_OUTPUT;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outputFile);
            fos.write(data);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
