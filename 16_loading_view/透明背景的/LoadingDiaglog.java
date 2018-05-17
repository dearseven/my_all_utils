
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;



/**
 * 通用等待dialog
 * Created by  wx 2018/5/17.
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
            AlertDialog.Builder bd = new AlertDialog.Builder(ctx,R.style.NoBackGroundDialog);
            //View view = inflater.inflate(R.layout.customview_loading_view, null);
            //bd.setView(view);
            ad = bd.create();
           // ad.getWindow().setDimAmount(0);//设置昏暗度为0
            ad.getWindow().setBackgroundDrawable(new ColorDrawable());
            //ad.setCanceledOnTouchOutside(false);
            ad.setCancelable(false);
        }
        ad.show();
        ad.setContentView(R.layout.customview_loading_view);
        ad.setCanceledOnTouchOutside(false);

        //ad.getWindow().setLayout(SizeUtil.dip2px(ctx, 100), SizeUtil.dip2px(ctx, 100));
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

