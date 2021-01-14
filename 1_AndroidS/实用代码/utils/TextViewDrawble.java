
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

public class TextViewDrawble {
	public static void drawLeft(TextView tv, Context ctx, int drawableId) {
		Drawable drawable = ctx.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv.setCompoundDrawables(drawable, null, null, null);
	}

	public static void drawRight(TextView tv, Context ctx, int drawableId) {
		Drawable drawable = ctx.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv.setCompoundDrawables(null, null, drawable, null);
	}

	public static void drawTop(TextView tv, Context ctx, int drawableId) {
		Drawable drawable = ctx.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv.setCompoundDrawables(null, drawable, null, null);
	}

	public static void drawBottom(TextView tv, Context ctx, int drawableId) {
		Drawable drawable = ctx.getResources().getDrawable(drawableId);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		tv.setCompoundDrawables(null, null, null, drawable);
	}
}
