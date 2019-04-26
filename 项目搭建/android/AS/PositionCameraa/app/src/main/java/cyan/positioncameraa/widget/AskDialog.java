package cyan.positioncameraa.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextThemeWrapper;

import cyan.positioncameraa.R;

/**
 * 带输入框的dialog
 * Created by wx on 2017/7/6.
 */
public class AskDialog {
    public interface AnswerListener {
        //void onCancle();

        void answer(boolean yesOrNo);
    }

    public void show(String show, final Context ctx, final AnswerListener listener) {
//        final EditText et = new EditText(ctx);
//        et.setBackgroundResource(R.drawable.edit_text_bg_gray);
//        et.setTextColor(Color.parseColor("#000000"));
//        LayoutInflater inflater = LayoutInflater.from(ctx);
//        final View view = inflater.inflate(R.layout.customview_input_dialog, null);
//        ((EditText) view.findViewById(R.id.cid_et)).setHint(show);
        new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.AppTheme_for_input_dialog)).setTitle("请确认").setMessage(show)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.answer(true);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.answer(false);
                    }
                })
                .show();
    }

    public void showConnectFailed(final Context ctx, final AnswerListener listener) {

        new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.AppTheme_for_input_dialog)).setTitle("请确认").setMessage("网络似乎有点小问题")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.answer(true);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.answer(false);
                    }
                })
                .show();
    }

    public void showAPIFailed(String str, final Context ctx, final AnswerListener listener) {

        new AlertDialog.Builder(new ContextThemeWrapper(ctx, R.style.AppTheme_for_input_dialog)).setTitle("请确认").setMessage(str + ",是否重试")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        listener.answer(true);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.answer(false);
                    }
                })
                .show();
    }
}
