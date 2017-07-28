
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import cc.m2u.hidrogen.R;
import cc.m2u.hidrogen.utils.SizeUtil;

/**
 * 通用等待dialog
 * Created by  wx 2017/7/4.
 */
public class LoadingDiaglog {
    private AlertDialog ad;
    private volatile boolean isShow = false;

    public synchronized void showDialog(Context ctx) {
        if (isShow) {
            return;
        }
        isShow = true;
        if (ad == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            AlertDialog.Builder bd = new AlertDialog.Builder(ctx);
            View view = inflater.inflate(R.layout.customview_loading_view, null);
            bd.setView(view);
            ad = bd.create();
            ad.setCanceledOnTouchOutside(false);
            ad.setCancelable(false);
        }
        ad.show();
        ad.getWindow().setLayout(SizeUtil.dip2px(ctx, 190), SizeUtil.dip2px(ctx, 170));
    }

    public synchronized void hideDialog() {
        if (!isShow) {
            return;
        }
        isShow = false;
        if (ad != null) {
            ad.hide();
        }
    }

    public void destroy() {
        if (ad != null) {
            isShow = false;
            ad.dismiss();
            ad = null;
        }
    }
}

