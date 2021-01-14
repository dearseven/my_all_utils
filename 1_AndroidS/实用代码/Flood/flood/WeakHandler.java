
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.lang.ref.WeakReference;

/**
 * 在Activity上用Static来建立匿名内部类就好了,不用也可以啦
 * Created by wx on 2017/10/24.
 */

public class WeakHandler extends Handler {
    public WeakReference<? extends Context> wr = null;

    public WeakHandler(Context aca) {
        wr = new WeakReference<Context>(aca);
    }
}
