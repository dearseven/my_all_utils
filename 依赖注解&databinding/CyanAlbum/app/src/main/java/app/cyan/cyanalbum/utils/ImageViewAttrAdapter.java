package app.cyan.cyanalbum.utils;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Cyan on 2017/10/28.
 */

public class ImageViewAttrAdapter {
    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, Bitmap bitmap) {
        view.setImageBitmap(bitmap);
    }

    @BindingAdapter("android:src")
    public static void setSrc(ImageView view, int resId) {
        view.setImageResource(resId);
    }

//    @BindingAdapter({"app:imageUrl", "app:placeHolder", "app:error"})
//    public static void loadImage(ImageView imageView, String url, Drawable holderDrawable, Drawable errorDrawable) {
////        Glide.with(imageView.getContext())
////                .load(url)
////                .placeholder(holderDrawable)
////                .error(errorDrawable)
////                .into(imageView);
//    }
}
