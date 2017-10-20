package com.android.slowlife.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.text.TextUtils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

public class QRUtils {

	private static final int WHITE = 0xFFFFFFFF;
	private static final int BLACK = 0xFF000000;

	public static Bitmap encodeToQR(String contentsToEncode, int dimension)
			throws Exception {
		if (TextUtils.isEmpty(contentsToEncode))
			return null;

		BarcodeFormat format = BarcodeFormat.QR_CODE;
		@SuppressWarnings("unchecked")
		Map<EncodeHintType, String> hints = new EnumMap(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix result = new MultiFormatWriter().encode(contentsToEncode,
				format, dimension, dimension, hints);
		int width = result.getWidth();
		int height = result.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		return bitmap;
	}

	public static Bitmap encodeToQRWidth(String contentsToEncode, int dimension)
			throws Exception {
		if (TextUtils.isEmpty(contentsToEncode))
			return null;

		BarcodeFormat format = BarcodeFormat.QR_CODE;
		Map hints = new EnumMap(EncodeHintType.class);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		BitMatrix result = new MultiFormatWriter().encode(contentsToEncode,
				format, dimension, dimension, hints);
		int width = result.getWidth();
		int height = result.getHeight();

		boolean isFirstBlack = true;
		int startX = 0;
		int startY = 0;

		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			int offset = y * width;
			for (int x = 0; x < width; x++) {
				pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
				if (result.get(x, y) && isFirstBlack) {
					isFirstBlack = false;
					startX = x;
					startY = y;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

		Matrix m = new Matrix();
		float sx = (width + 2f * startX) / width;
		float sy = (height + 2f * startY) / height;
		m.postScale(sx, sy);

		Bitmap qrBitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(qrBitmap);
		canvas.translate(-startX, -startY);

		Paint paint = new Paint();
		paint.setAntiAlias(true);

		canvas.drawBitmap(bitmap, m, paint);
		canvas.save();

		return qrBitmap;
	}
	public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}  
}
