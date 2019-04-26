
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

public class CircleImageView extends ImageView {
	private Path path;
	private Paint paint;
	public PaintFlagsDrawFilter mPaintFlagsDrawFilter;// 毛边过滤
	public String boardColr = "#888888";

	public CircleImageView(Context context) {
		super(context);
		init();
	}

	public CircleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		mPaintFlagsDrawFilter = new PaintFlagsDrawFilter(0,
				Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);

		path = new Path();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setColor(Color.GRAY);
	}

	public CircleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	// @Override
	// protected void onDraw(Canvas cns) {
	// // // 首先找到圆心
	// float cx = getMeasuredWidth() / 2;
	// float cy = getMeasuredHeight() / 2;
	// float cr = cx < cy ? cx : cy;
	//
	// // 确定一个圆的宽和高
	// float h = getMeasuredHeight() / 2;
	// float w = getMeasuredWidth() / 2;
	//
	// // 画出一个圆形,这个画出来作为背景
	// cns.drawCircle(cr, cr, Math.max(w, h), paint);
	// // int saveCount = cns.getSaveCount();
	// cns.save();
	//
	// // 添加一个圆形,这个圆形是最后用于剪切成为显示区，他比上面的那个圆小一点,就可以留出一个灰色的边界
	// path.addCircle(cr, cr, (float) Math.max(w - 5, h - 5),
	// Path.Direction.CCW);
	// path.close();
	//
	// // 剪切出来一个圆作为显示区域，这个圆比背景的那个圆小一点，在这个显示区域里显示图片
	// cns.setDrawFilter(mPaintFlagsDrawFilter);
	// cns.clipPath(path, Region.Op.REPLACE);
	// cns.setDrawFilter(mPaintFlagsDrawFilter);
	//
	// // 显示图片
	// super.onDraw(cns);
	// // cns.restoreToCount(saveCount);
	//
	// }

	@Override
	protected void onDraw(Canvas canvas) {

		try {

			Drawable drawable = getDrawable();

			if (drawable == null) {
				return;
			}

			if (getWidth() == 0 || getHeight() == 0) {
				return;
			}
			this.measure(0, 0);
			if (drawable.getClass() == NinePatchDrawable.class)
				return;
			Bitmap b = ((BitmapDrawable) drawable).getBitmap();
			Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);

			int w = getWidth(), h = getHeight();

			// 边框宽度
			int mBorderThickness = 3;

			int radius = (w < h ? w : h) / 2 - mBorderThickness;
			Bitmap roundBitmap = getCroppedBitmap(bitmap, radius);

			paint.setAntiAlias(true);
			paint.setFilterBitmap(true);
			paint.setDither(true);
			// 边框颜色
			paint.setColor(Color.parseColor(boardColr));
			canvas.drawCircle(w / 2, h / 2, radius + mBorderThickness, paint);
			canvas.drawBitmap(roundBitmap, w / 2 - radius, h / 2 - radius, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 裁剪图片
	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap scaledSrcBmp;
		int diameter = radius * 2;
		if (bmp.getWidth() != diameter || bmp.getHeight() != diameter)
			scaledSrcBmp = Bitmap.createScaledBitmap(bmp, diameter, diameter,
					false);
		else
			scaledSrcBmp = bmp;
		Bitmap output = Bitmap.createBitmap(scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, scaledSrcBmp.getWidth(),
				scaledSrcBmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(scaledSrcBmp.getWidth() / 2,
				scaledSrcBmp.getHeight() / 2, scaledSrcBmp.getWidth() / 2,
				paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(scaledSrcBmp, rect, rect, paint);

		return output;
	}

}
