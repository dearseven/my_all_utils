
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.teetaa.outsourcing.carinspection.R;
import com.teetaa.outsourcing.carinspection.util.SizeUtil;


/**
 * 通用等待dialog
 * Created by  wx 2017/7/4.
 */
public class LoadingDiaglog {
    private IBackCall iBackCall;
    private AlertDialog ad;
    private volatile boolean isShow = false;

    public interface IBackCall {
        void onBackCalled();
    }


    public LoadingDiaglog() {
    }

    public LoadingDiaglog(IBackCall iBackCall) {
        this.iBackCall = iBackCall;
    }

    public synchronized void showDialog(final Context ctx) {
        if (isShow) {
            return;
        }
        isShow = true;
        if (ad == null) {
            LayoutInflater inflater = LayoutInflater.from(ctx);
            AlertDialog.Builder bd = new AlertDialog.Builder(ctx, R.style.NoBackGroundDialog);
            //View view = inflater.inflate(R.layout.customview_loading_view, null);
            //bd.setView(view);
            ad = bd.create();
            // ad.getWindow().setDimAmount(0);//设置昏暗度为0
            ad.getWindow().setBackgroundDrawable(new ColorDrawable());
            //ad.setCanceledOnTouchOutside(false);
            ad.setCancelable(false);
        }
        if (iBackCall != null) {
            ad.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK &&
                            event.getAction() == KeyEvent.ACTION_UP &&
                            !event.isCanceled()) {
                        iBackCall.onBackCalled();
                        //ToastUtil.Companion.showToast(ctx, "onKey BackPressed!!!!");
//                    dialog.cancel();
//                    showDialog(DIALOG_MENU);
                        return true;
                    }
                    return false;
                }
            });
        }
        ad.show();

        Window dialogWindow = ad.getWindow();
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // 设置宽度
        p.width = (int) (new DisplayMetrics().widthPixels * 0.95); // 宽度设置为屏幕的0.95
        p.gravity = Gravity.CENTER;//设置位置

        ad.setContentView(R.layout.customview_loading_view);
        ad.setCanceledOnTouchOutside(false);
//        ad.getWindow().setLayout(SizeUtil.dip2px(ctx, 190), SizeUtil.dip2px(ctx, 170));
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

