
import android.content.Context;
import android.widget.ImageView;
import cc.m2u.lottery.R;

/**
 * Created by wx on 2017/7/22.
 */
public class GlideUtils {
    public static void loadUrlImage(Context context, String url, ImageView imageView){
        GlideApp.with(context).asBitmap()
                .load(url).error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);
    }
}
