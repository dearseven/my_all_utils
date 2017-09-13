package cyan.view.cyan_autosize.configs;

import android.content.Context;

/**
 * Created by Cyan on 2017/8/6.
 */

public class AutoSizeConfigs {
    /**
     * 宽度的基数
     */
    public  static int STANDHARD_W = 1440;
    /**
     * 高度的基数
     */
    public static int STANDHARD_H = 2560;

    /**
     * 计算高宽并更新基数
     *
     * @param ctx
     * @return
     */
    public static void resetStandHardWH(Context ctx) {
        Integer[] wh = new Integer[2];
        float wDh = ctx.getResources().getDisplayMetrics().widthPixels / ctx.getResources().getDisplayMetrics().heightPixels;
        if (wDh == 0.5f) {//小米mix2 18:9=0.5
            wh[0] = 1080;
            wh[1] = 2160;
        } else {//16:9 =0.5625
            wh[0] = 1440;
            wh[1] = 2560;
        }
        STANDHARD_W = wh[0];
        STANDHARD_H = wh[1];
    }
}
