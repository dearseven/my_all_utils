
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashMap;


/**
 * Created by Administrator on 2018/6/11.
 */

public abstract class HanderHelp {
    public static Handler h = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    public void backMainThread() {
        h.post(getJobTread());
    }

    public abstract Thread getJobTread();
}
