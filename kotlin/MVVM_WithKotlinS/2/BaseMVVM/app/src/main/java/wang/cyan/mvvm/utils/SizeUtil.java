package wang.cyan.mvvm.utils;

import android.content.Context;

import java.math.BigDecimal;

/**
 * 各单位转像素
 * 
 * @author WangXu 2014-7-7 下午5:37:54
 */
public class SizeUtil {
	// public final static int COMPLEX_UNIT_PX = 0;
	// public final static int COMPLEX_UNIT_DIP = 1;
	// public final static int COMPLEX_UNIT_SP = 2;
	// public final static int COMPLEX_UNIT_PT = 3;
	// public final static int COMPLEX_UNIT_IN = 4;
	// public final static int COMPLEX_UNIT_MM = 5;
	//
	// public static float applyDimension(int unit, float value,
	// DisplayMetrics metrics) {
	// switch (unit) {
	// case COMPLEX_UNIT_PX:
	// return value;
	// case COMPLEX_UNIT_DIP:
	// return value * metrics.density;
	// case COMPLEX_UNIT_SP:
	// return value * metrics.scaledDensity;
	// case COMPLEX_UNIT_PT:
	// return value * metrics.xdpi * (1.0f / 72);
	// case COMPLEX_UNIT_IN:
	// return value * metrics.xdpi;
	// case COMPLEX_UNIT_MM:
	// return value * metrics.xdpi * (1.0f / 25.4f);
	// }
	// return 0;
	// }

	/**
	 * 将px值转换为dip或dp值，保证尺寸大小不变
	 * 
	 * @param pxValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * 
	 * @param dipValue
	 *            （DisplayMetrics类中属性density）
	 * @return
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	public static int new_dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().densityDpi;
		return (int) (dipValue * (scale / 160) + 0.5f);
	}

	public static int new_px2dp(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().densityDpi;
		return (int) ((pxValue * 160) / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (pxValue / fontScale + 0.5f);
	}

	/**
	 * 将sp值转换为px值，保证文字大小不变
	 * 
	 * @param spValue
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * 计算高度为屏幕高度的0~1.0（好吧其实没做最大和最小限制呢）
	 * 
	 * @param context
	 * @return
	 */
	public static int partOfHeight(Context context, float rate) {
		int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
		return new BigDecimal(screenHeight * rate).intValue();
	}

	/**
	 * 计算宽度为屏幕宽度的0~1.0（好吧其实没做最大和最小限制呢）
	 * 
	 * @param context
	 * @return
	 */
	public static int partOfWidth(Context context, float rate) {
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		return new BigDecimal(screenWidth * rate).intValue();
	}
}
